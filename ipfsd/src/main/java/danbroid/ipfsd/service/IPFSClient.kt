package danbroid.ipfsd.service

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 *
 */
open class IPFSClient(val context: Context, var timeout: Long = 60000L) {

  enum class ConnectionState {
    DISCONNECTED, CONNECTING, CONNECTED, STARTED;
  }

  init {
    log.error("CREATED IPFS CLIENT")
  }

  private val _connectionState: MutableLiveData<ConnectionState> =
    MutableLiveData(ConnectionState.DISCONNECTED)

  val connectionState: LiveData<ConnectionState> = _connectionState


  private val messageCallback = Handler.Callback {
    when (it.what) {
      MSG_TIMEOUT -> {
        log.warn("MSG_TIMEOUT")
        disconnect()
      }
      else -> {
        val msg = it.toIPFSMessage()
        log.debug("inMessage: $msg")

        when (msg) {
          IPFSMessage.SERVICE_STARTED -> {
            _connectionState.value = ConnectionState.STARTED
            setTimeout()
          }
          IPFSMessage.SERVICE_STOPPING -> {
            log.warn("SERVICE STOPPING")
            disconnect()
          }
          else -> log.error("unhandled message: $msg")
        }
      }
    }

    true
  }

  private val messageHandler = Handler(Looper.getMainLooper(), messageCallback)

  @MainThread
  fun clearTimeout() = messageHandler.removeMessages(MSG_TIMEOUT)

  @MainThread
  fun setTimeout() {
    log.trace("setTimeout() $timeout")
    clearTimeout()
    messageHandler.sendEmptyMessageDelayed(MSG_TIMEOUT, timeout)
  }

  private val inMessenger = Messenger(messageHandler)

  private var outMessenger: Messenger? = null

  private val serviceConnection = IPFSServiceConnection()

  @MainThread
  fun connect() {
    log.debug("connect() state: ${_connectionState.value}")
    if (_connectionState.value == ConnectionState.DISCONNECTED) {
      log.debug("connecting ..")
      _connectionState.value = ConnectionState.CONNECTING

      log.warn("starting service")
      val intent = Intent(context, IPFSService::class.java)

      log.warn("binding to service ..")
      context.bindService(
        intent,
        serviceConnection,
        Context.BIND_AUTO_CREATE
      )

    }
  }

  @MainThread
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
    _connectionState.value = ConnectionState.DISCONNECTED

  }

  protected fun finalize() {
    log.debug("finalize()")
    disconnect()
  }


  companion object {
    const val MSG_TIMEOUT = 12301
  }

  private var callCount = 0

  @MainThread
  fun incrementCallCount() {
    if (callCount == 0)
      clearTimeout()
    callCount++
    log.trace("callCount: $callCount")
    connect()
  }

  @MainThread
  fun decrementCallCount() {
    callCount--
    log.trace("call complete: $callCount")
    if (callCount == 0)
      setTimeout()
  }

  suspend fun runWhenConnected(job: suspend () -> Unit) {
    withContext(Dispatchers.Main) {
      runCatching {
        incrementCallCount()

        if (connectionState.value != ConnectionState.STARTED) {
          log.trace("waiting to be connected ..")
          connectionState.asFlow().filter { it == ConnectionState.STARTED }.first()
          log.trace("connection state: ${connectionState.value}")
        }

        if (connectionState.value == ConnectionState.STARTED) job.invoke()
        else throw IllegalStateException("Failed to connect")

      }.also {
        decrementCallCount()
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

