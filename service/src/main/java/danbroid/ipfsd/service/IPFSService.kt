package danbroid.ipfsd.service


import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Messenger
import android.widget.Toast
import androidx.annotation.MainThread
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.client.IPFSMessage
import danbroid.ipfsd.client.toIPFSMessage
import danbroid.ipfsd.service.settings.IPFSServicePrefs
import danbroid.logging.AndroidLog
import danbroid.logging.LogConfig
import danbroid.util.format.humanReadableByteCount
import ipfs.gomobile.android.IPFS
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

    //const val ACTION_SETTINGS = "$pkg.ACTION_SETTINGS"
    const val ACTION_START = "$pkg.ACTION_START"
    const val ACTION_STOP = "$pkg.ACTION_STOP"
    const val ACTION_CLEAR_NET_STATS = "$pkg.ACTION_CLEAR_NET_STATS"

    val log = LogConfig.let {
      val log = AndroidLog("IPFSD_SERVICE")
      it.DEBUG = BuildConfig.DEBUG
      it.COLOURED = BuildConfig.DEBUG
      it.GET_LOG = { log }
      log.debug("created log")
      log
    }


    @Volatile
    private var _ipfs: IPFS? = null

    fun getIPFS(context: Context) = _ipfs ?: synchronized(IPFSService::class.java) {
      _ipfs ?: IPFS(context.applicationContext).also {
        _ipfs = it
      }
    }

    fun resetStats(context: Context) = serviceAction(context, ACTION_CLEAR_NET_STATS)

    fun serviceAction(context: Context, action: String) =
      context.startService(Intent(action).setPackage(IPFSD.SERVICE_PKG))
/*      context.startService(
        Intent(
          context,
          IPFSService::class.java
        ).setAction(action)
      )*/

    fun startService(context: Context) = serviceAction(context, ACTION_START)

    fun stopService(context: Context) = serviceAction(context, ACTION_STOP)


  }


  val prefs: IPFSServicePrefs by lazy {
    IPFSServicePrefs(this)
  }

    get() = getIPFS(this)

  private var coroutineScope = CoroutineScope(Dispatchers.IO)

  val clients = mutableSetOf<Messenger>()
  lateinit var messengerHandler: Handler
  lateinit var messenger: Messenger
  lateinit var notificationManager: NotificationManager

  private val messengerCallback = Handler.Callback {
    when (it.what) {
      MSG_POLL_STATS ->
        readStats()

      MSG_TIMEOUT ->
        onTimeout()

      else -> {
        val msg = it.toIPFSMessage(classLoader)
        log.debug("SERVICE MESSAGE: $msg")
        when (msg) {
          IPFSMessage.CLIENT_CONNECT -> {
            clients.add(it.replyTo)
            if (ipfs.isStarted == true)
              it.replyTo.send(IPFSMessage.SERVICE_STARTED.toMessage())
          }
          IPFSMessage.CLIENT_DISCONNECT ->
            clients.remove(it.replyTo)

          IPFSMessage.TIMEOUT_RESET ->
            resetTimeout()

          IPFSMessage.REPO_STAT -> repoStat()
          IPFSMessage.STATS_RESET -> readStats(true)
        }
      }
    }

    true
  }

  private fun resetTimeout() {
    log.trace("resetTimeout()")
    messengerHandler.apply {
      removeMessages(MSG_TIMEOUT)
      sendEmptyMessageDelayed(MSG_TIMEOUT, prefs.timeout)
    }
  }

  private val notificationListener = object : NotificationManager.NotificationListener {
    override fun onAction(intent: Intent) {
      log.trace("action: $intent")

      when (intent.action) {
        /*   ACTION_SETTINGS -> {
             startActivity(Intent(Intent.ACTION_VIEW).setData("ipfsdemo://settings".toUri()))
           }*/
        ACTION_CLEAR_NET_STATS -> readStats(true)
      }
    }
  }

  private fun onTimeout() {
    log.warn("onTimeout()")
    stopService()
  }

  protected fun startFlow() = flow {
    log.warn("START FLOW")

    if (ipfs.isStarted) {
      emit(true)
      return@flow
    }

    withContext(Dispatchers.Main) {
      notificationManager.showNotification(getString(R.string.lbl_service_starting))
    }

    measureTimeMillis {
      log.warn("starting ipfs")
      ipfs.start()
    }.also {
      log.warn("created ipfs in $it")
    }

    if (!ipfs.isStarted) {
      emit(false)
      return@flow
    }

    log.warn("SHOWING NOTIFICATION")
    withContext(Dispatchers.Main) {
      notificationManager.showNotification(getString(R.string.lbl_service_running))

      readStats()

      clients.forEach {
        it.send(IPFSMessage.SERVICE_STARTED.toMessage())
      }
    }

  }.flowOn(Dispatchers.IO)


  override fun onCreate() {
    log.warn("onCreate()")
    super.onCreate()

    val connectMessage = IPFSMessage.CLIENT_CONNECT.toMessage()
    log.error("CONNECT MESSAGE: $connectMessage")

    notificationManager = NotificationManager(
      this, CHANNEL_ID,
      CHANNEL_NAME, CHANNEL_DESCRIPTION,
      listener = notificationListener
    )

    messengerHandler = Handler(Looper.myLooper()!!, messengerCallback)
    messenger = Messenger(messengerHandler)


    coroutineScope.launch {
      startFlow().firstOrNull {
        log.warn("STARTED: $it")
        true
      }
      log.warn("LAUNCH FINISHED")
    }
  }


  private object NetworkStats {
    var dataIn: Long = 0
    var dataOut: Long = 0
  }


  private fun readStats(resetStats: Boolean = false) {
    coroutineScope.launch {
      ipfs.newRequest("stats/bw")?.send()?.also {
        messengerHandler.sendEmptyMessageDelayed(MSG_POLL_STATS, 20000)
        val stats = JSONObject(it.decodeToString())
        withContext(Dispatchers.Main) {
          notificationManager.showNotification {

            val rateIn = stats.getDouble("RateIn")
            val rateOut = stats.getDouble("RateOut")


            val totalDataIn = stats.getLong("TotalIn")
            val totalDataOut = stats.getLong("TotalOut")
            var newDataIn = totalDataIn - NetworkStats.dataIn
            var newDataOut = totalDataOut - NetworkStats.dataOut
            NetworkStats.dataIn = totalDataIn
            NetworkStats.dataOut = totalDataOut

            if (resetStats) {
              newDataIn = 0
              newDataOut = 0
              Toast.makeText(
                this@IPFSService,
                R.string.msg_stats_reset,
                Toast.LENGTH_SHORT
              ).show()
            } else {
              newDataIn = prefs.dataIn + newDataIn
              newDataOut = prefs.dataOut + newDataOut
            }

            prefs.dataIn = newDataIn
            prefs.dataOut = newDataOut



            sendMessage(
              danbroid.ipfsd.client.IPFSMessage.BANDWIDTH(
                newDataIn,
                newDataOut,
                rateIn,
                rateOut
              )
            )

            //val msg = "In: %02d Out:%02d (%.0f,%.0f)".format(totalIn, totalOut, rateIn, rateOut)
            val msg =
              "${newDataIn.humanReadableByteCount()} in ${newDataOut.humanReadableByteCount()} out. Total: ${(newDataIn + newDataOut).humanReadableByteCount()}"
            setContentText(msg)
          }
        }
      }
    }
  }

  @MainThread
  private fun sendMessage(msg: danbroid.ipfsd.client.IPFSMessage) {
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

    if (intent?.action == ACTION_CLEAR_NET_STATS) {
      readStats(true)
      return START_STICKY_COMPATIBILITY
    }


/*    if (intent?.getBooleanExtra(EXTRA_DELETE, false) == true) {
      log.error("NEED TO STOP!!!")
      stopSelf()
    }*/
    return super.onStartCommand(intent, flags, startId)
  }

  @MainThread
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
    messengerHandler.removeCallbacksAndMessages(null)
    notificationManager.cancelNotification()
    coroutineScope.cancel("Service stopped")

    runBlocking(Dispatchers.IO) {
      measureTimeMillis {
        if (_ipfs?.isStarted == true) {
          _ipfs?.stop()
        }
        _ipfs = null
      }.also {
        log.trace("stopped in $it")
      }
    }


    clients.clear()
    super.onDestroy()

  }

  override fun onBind(intent: Intent?): IBinder? {
    log.debug("onBind() $intent")
    return messenger.binder
  }


  private fun repoStat() {
    log.debug("repoStat()")
    coroutineScope.launch {
      ipfs.newRequest("repo/stat").send().also {
        log.warn("RESULT: ${it.toString()}")
      }
    }
  }

}


