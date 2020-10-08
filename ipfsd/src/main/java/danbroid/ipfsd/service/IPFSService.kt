package danbroid.ipfsd.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Messenger
import androidx.annotation.MainThread
import androidx.core.net.toUri
import danbroid.ipfsd.R
import danbroid.util.format.humanReadableByteCount
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.system.measureTimeMillis


class IPFSService : Service() {

  companion object {
    const val MSG_POLL_STATS = 1
    const val MSG_TIMEOUT = 2

    const val CHANNEL_ID = "ipfs_channel"
    const val CHANNEL_NAME = "IPFS Service"
    const val CHANNEL_DESCRIPTION = "IPFS Service notification channel"

    private const val pkg = "danbroid.ipfsd.service"
    const val ACTION_SETTINGS = "$pkg.ACTION_SETTINGS"
    const val ACTION_STOP = "$pkg.ACTION_STOP"
    const val ACTION_CLEAR_NET_STATS = "$pkg.ACTION_CLEAR_NET_STATS"


    @Volatile
    private var _ipfs: IPFS? = null

    fun getIPFS(context: Context) = _ipfs ?: synchronized(IPFSService::class.java) {
      _ipfs ?: IPFS(context.applicationContext).also {
        it.enableNamesysPubsub()
        it.enablePubsubExperiment()
        _ipfs = it
      }
    }
  }


  val prefs: IPFSServicePrefs by lazy {
    IPFSServicePrefs(this)
  }

  private val ipfs: IPFS
    get() = getIPFS(this)

  private var coroutineScope = CoroutineScope(Dispatchers.IO)

  val clients = mutableSetOf<Messenger>()
  lateinit var messengerHandler: Handler
  lateinit var messenger: Messenger
  lateinit var notificationManager: NotificationManager

  private val messengerCallback = Handler.Callback {
    when (it.what) {
      MSG_POLL_STATS -> {
        readStats()
      }
      MSG_TIMEOUT -> {
        stopService()
      }
      else -> {
        val msg = it.toIPFSMessage()
        log.debug("SERVICE MESSAGE: $msg")
        when (msg) {
          IPFSMessage.CLIENT_CONNECT -> {
            clients.add(it.replyTo)
            if (ipfs.isStarted == true)
              it.replyTo.send(IPFSMessage.SERVICE_STARTED.toMessage())
          }
          IPFSMessage.CLIENT_DISCONNECT -> {
            clients.remove(it.replyTo)
          }
          IPFSMessage.TIMEOUT_RESET -> {
            resetTimeout()
          }
        }
      }
    }

    true
  }

  private fun resetTimeout() {
    log.trace("resetTimeout()")
    messengerHandler.apply {
      removeMessages(MSG_TIMEOUT)
      sendEmptyMessageDelayed(MSG_TIMEOUT, 60000)
    }

  }

  private val actionHandler: (Intent) -> Unit = {
    when (it.action) {
      ACTION_SETTINGS -> {
        log.debug("showing settings")
        startActivity(Intent(Intent.ACTION_VIEW).setData("ipfsdemo://settings".toUri()))
      }
    }
    log.error("action: $it")
  }

  override fun onCreate() {
    log.warn("onCreate()")
    super.onCreate()

    NetworkStats.startDataIn = prefs.dataIn
    NetworkStats.startDataOut = prefs.dataOut

    notificationManager = NotificationManager(
      this, CHANNEL_ID,
      CHANNEL_NAME, CHANNEL_DESCRIPTION,
      actionHandler = actionHandler
    )

    messengerHandler = Handler(Looper.myLooper()!!, messengerCallback)
    messenger = Messenger(messengerHandler)
    notificationManager.showNotification(getString(R.string.lbl_service_starting))

    coroutineScope.launch {
      measureTimeMillis {
        if (!ipfs.isStarted)
          ipfs.start()
      }.also {
        log.debug("created ipfs in $it")
      }
      withContext(Dispatchers.Main) {
        notificationManager.showNotification(getString(R.string.lbl_service_running))
      }
      readStats()
      withContext(Dispatchers.Main) {
        if (ipfs.isStarted == true) {
          val msg = IPFSMessage.SERVICE_STARTED.toMessage()
          clients.forEach {
            it.send(msg)
          }
        }
      }
    }
  }


  private object NetworkStats {
    var totalDataIn: Long = 0
    var startDataIn: Long = 0
    var startDataOut: Long = 0
    var totalDataOut: Long = 0
  }

  private fun readStats() {
    coroutineScope.launch {
      ipfs.newRequest("stats/bw")?.send()?.also {
        messengerHandler.sendEmptyMessageDelayed(MSG_POLL_STATS, 10000)
        val stats = JSONObject(it.decodeToString())
        withContext(Dispatchers.Main) {
          notificationManager.showNotification {

            val rateIn = stats.getDouble("RateIn")
            val rateOut = stats.getDouble("RateOut")

            NetworkStats.totalDataIn = stats.getLong("TotalIn")
            NetworkStats.totalDataOut = stats.getLong("TotalOut")

            val currentDataIn = NetworkStats.totalDataIn + NetworkStats.startDataIn
            val currentDataOut = NetworkStats.totalDataOut + NetworkStats.startDataOut

            sendMessage(
              IPFSMessage.BANDWIDTH(
                currentDataIn,
                currentDataOut,
                rateIn,
                rateOut
              )
            )

            //val msg = "In: %02d Out:%02d (%.0f,%.0f)".format(totalIn, totalOut, rateIn, rateOut)
            val msg =
              "${currentDataIn.humanReadableByteCount()} in ${currentDataOut.humanReadableByteCount()} out. Total: ${(currentDataIn + currentDataOut).humanReadableByteCount()}"
            setContentText(msg)
          }
        }
      }
    }
  }

  @MainThread
  private fun sendMessage(msg: IPFSMessage) {
    val toRemove = mutableListOf<Messenger>()
    clients.forEach { client ->
      runCatching {
        client.send(msg.toMessage())
      }.exceptionOrNull()?.also {
        log.error(it.message, it)
        toRemove.add(client)
      }
    }
    clients.removeAll(toRemove)
  }

  override fun onRebind(intent: Intent?) {
    log.warn("onRebind() $intent")
    super.onRebind(intent)
  }


  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    log.warn("onStartCommand() $intent flags:$flags startId: $startId")

    if (intent?.action == ACTION_STOP) {
      stopService(startId)
      return START_STICKY_COMPATIBILITY
    }


/*    if (intent?.getBooleanExtra(EXTRA_DELETE, false) == true) {
      log.error("NEED TO STOP!!!")
      stopSelf()
    }*/
    return super.onStartCommand(intent, flags, startId)
  }

  private fun stopService(startId: Int = -1) {
    log.warn("stopService()")
    notificationManager.cancelNotification()
    clients.forEach {
      val msg = IPFSMessage.SERVICE_STOPPING.toMessage()
      try {
        it.send(msg)
      } catch (err: Exception) {
        log.error(err.message, err)
      }
    }
    clients.clear()
    coroutineScope.cancel("Stopping")
    stopSelf(startId)
  }

  override fun onDestroy() {
    log.warn("onDestroy()")
    prefs.dataIn = NetworkStats.startDataIn + NetworkStats.totalDataIn
    prefs.dataOut = NetworkStats.startDataOut + NetworkStats.totalDataOut
    notificationManager.cancelNotification()
    coroutineScope.cancel("Service stopped")

    runBlocking(Dispatchers.IO) {
      measureTimeMillis {
        if (_ipfs?.isStarted == true)
          _ipfs?.stop()
      }.also {
        log.debug("stopped in $it")
      }
    }

    _ipfs = null

    messengerHandler.removeCallbacksAndMessages(null)
    clients.clear()
    super.onDestroy()

  }

  override fun onBind(intent: Intent?): IBinder? {
    log.debug("onBind() $intent")
    return messenger.binder
  }


}

private val log = org.slf4j.LoggerFactory.getLogger(IPFSService::class.java)
