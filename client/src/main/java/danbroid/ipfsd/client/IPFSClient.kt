package danbroid.ipfsd.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Messenger
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import danbroid.ipfsd.IPFSD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

/**
 *
 */
open class IPFSClient(val context: Context) {

  enum class ConnectionState {
    DISCONNECTED, CONNECTING, CONNECTED, STARTED;
  }

  init {
    log.info("CREATED IPFS CLIENT")
  }

  companion object {
    @Volatile
    private var instance: WeakReference<IPFSClient>? = null

    @JvmStatic
    fun getInstance(context: Context) = instance?.get() ?: synchronized(IPFSClient::class.java) {
      instance?.get() ?: IPFSClient(context).also {
        instance = WeakReference(it)
      }
    }
  }

  private val _connectionState: MutableLiveData<ConnectionState> =
    MutableLiveData(ConnectionState.DISCONNECTED)

  val connectionState: LiveData<ConnectionState> = _connectionState

  private val messageCallback = Handler.Callback {

    val msg = it.toIPFSMessage(this@IPFSClient.javaClass.classLoader)
    log.debug("inMessage: $msg")

    when (msg) {
      IPFSMessage.SERVICE_STARTED -> {
        _connectionState.value = ConnectionState.STARTED
      }
      IPFSMessage.SERVICE_STOPPING -> {
        log.warn("SERVICE STOPPING")
        disconnect()
      }
      is IPFSMessage.BANDWIDTH -> {

      }
      else -> log.error("unhandled message: $msg")
    }

    true
  }

  private val messageHandler = Handler(Looper.getMainLooper(), messageCallback)

  private val inMessenger = Messenger(messageHandler)

  private var outMessenger: Messenger? = null

  private val serviceConnection = IPFSServiceConnection()

  @MainThread
  fun connect() {
    log.debug("connect() state: ${_connectionState.value}")
    if (_connectionState.value == ConnectionState.DISCONNECTED) {
      log.debug("connecting ..")
      _connectionState.value = ConnectionState.CONNECTING


      //val intent = Intent(context, IPFSService::class.java)
/*
      log.warn("starting service")
      context.startService(intent)*/

      log.warn("binding to service ..")
      context.bindService(
        IPFSD.intent.service_intent,
        serviceConnection,
        Context.BIND_AUTO_CREATE
      )

    }
  }

  fun disconnect() {
    log.info("disconnect() connectionState: ${_connectionState.value}")
    messageHandler.removeCallbacksAndMessages(null)

    if (_connectionState.value != ConnectionState.DISCONNECTED) {
      outMessenger?.send(IPFSMessage.CLIENT_DISCONNECT.toMessage())
    }

    if (_connectionState.value != ConnectionState.DISCONNECTED && _connectionState.value != ConnectionState.DISCONNECTED) {
      // sendMessage(RegisterEvent.UNREGISTER)
      log.trace("unbinding service ..")
      context.unbindService(serviceConnection)
    }
    _connectionState.postValue(ConnectionState.DISCONNECTED)

  }

  protected fun finalize() {
    log.debug("finalize()")
    disconnect()
  }


  private var callCount = 0

  @MainThread
  fun incrementCallCount() {
    callCount++
    log.trace("callCount: $callCount")
    connect()
  }

  @MainThread
  fun decrementCallCount() {
    callCount--
    log.trace("call complete: $callCount")
  }

  suspend fun runWhenConnected(job: suspend () -> Unit) {

    withContext(Dispatchers.Main) {
      log.trace("runWhenConnected()")
      runCatching {
        incrementCallCount()


        if (connectionState.value != ConnectionState.STARTED) {
          log.trace("waiting to be connected ..")
          connectionState.asFlow().flowOn(Dispatchers.IO).filter { it == ConnectionState.STARTED }
            .first()
          log.trace("connection state: ${connectionState.value}")
        }



        if (connectionState.value == ConnectionState.STARTED) {
          // sendMessage(IPFSMessage.TIMEOUT_RESET)
          job.invoke()
        } else throw IllegalStateException("Failed to connect")

      }.also {
        decrementCallCount()
        it.exceptionOrNull()?.also {
          log.error(it.message, it)
        }
      }
    }
  }


  suspend fun sendMessage(msg: IPFSMessage) = runWhenConnected {
    outMessenger?.send(msg.toMessage())
  }


  protected inner class IPFSServiceConnection : ServiceConnection {
    override fun onBindingDied(name: ComponentName?) {
      log.warn("onBindindDied() name:$name")
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
      log.info("onServiceConnected()")
      outMessenger = Messenger(service)
      _connectionState.value = ConnectionState.CONNECTED

      log.debug("sending CLIENT_CONNECT")
      outMessenger?.send(IPFSMessage.CLIENT_CONNECT.toMessage(inMessenger))
    }

    override fun onServiceDisconnected(name: ComponentName) {
      log.warn("onServiceDisconnected()")
      _connectionState.value = ConnectionState.DISCONNECTED
      outMessenger = null
      connect()
    }
  }


}

private val log = org.slf4j.LoggerFactory.getLogger(IPFSClient::class.java)

