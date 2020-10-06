package danbroid.ipfsd.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Messenger
import danbroid.ipfsd.R
import danbroid.util.format.FormatUtils
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.system.measureTimeMillis


class IPFSService : Service() {

  companion object {
    const val MSG_POLL_STATS = 10241
    const val CHANNEL_ID = "ipfs_channel"
    const val CHANNEL_NAME = "IPFS Channel"
    const val CHANNEL_DESCRIPTION = "IPFS Service notification channel"
    const val ACTION_STOP = "danbroid.ipfsd.service.ACTION_STOP"

    fun createStopIntent(context: Context) = Intent(
      context,
      IPFSService::class.java
    ).setAction(ACTION_STOP)

    @Volatile
    private var ipfs: IPFS? = null

    fun getIPFS(context: Context) = ipfs ?: synchronized(IPFSService::class.java) {
      ipfs ?: IPFS(context.applicationContext).also {
        it.enableNamesysPubsub()
        it.enablePubsubExperiment()
        ipfs = it
      }
    }
  }


  private val ipfs: IPFS
    get() = getIPFS(this)

  private var coroutineScope = CoroutineScope(Dispatchers.IO)

  val clients = mutableSetOf<Messenger>()
  lateinit var messengerHandler: Handler
  lateinit var messenger: Messenger

  private val messengerCallback = Handler.Callback {
    when (it.what) {
      MSG_POLL_STATS -> {
        readStats()
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
        }
      }
    }

    true
  }


  override fun onCreate() {
    log.warn("onCreate()")
    super.onCreate()

    log.trace("creating notification channel")
    createNotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_DESCRIPTION)

    messengerHandler = Handler(Looper.myLooper()!!, messengerCallback)
    messenger = Messenger(messengerHandler)
    showNotification(getString(R.string.lbl_service_starting))

    coroutineScope.launch {
      measureTimeMillis {
        if (!ipfs.isStarted)
          ipfs.start()
      }.also {
        log.debug("created ipfs in $it")
      }
      withContext(Dispatchers.Main) {
        showNotification(getString(R.string.lbl_service_running))
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


  private fun readStats() {
    coroutineScope.launch {
      ipfs.newRequest("stats/bw")?.send()?.also {
        messengerHandler.sendEmptyMessageDelayed(MSG_POLL_STATS, 10000)
        val stats = JSONObject(it.decodeToString())
        withContext(Dispatchers.Main) {
          showNotification {
            val totalIn = stats.getLong("TotalIn")
            val totalOut = stats.getLong("TotalOut")
            val rateIn = stats.getDouble("RateIn")
            val rateOut = stats.getDouble("RateOut")

            //val msg = "In: %02d Out:%02d (%.0f,%.0f)".format(totalIn, totalOut, rateIn, rateOut)
            val msg = "Network usage: ${FormatUtils.humanReadableByteCountBin(totalIn)} in ${FormatUtils.humanReadableByteCountBin(totalOut)} out. Total: ${FormatUtils.humanReadableByteCountBin(totalIn + totalOut)}"
            setContentText(msg)
          }
        }
      }
    }
  }

  override fun onRebind(intent: Intent?) {
    log.warn("onRebind() $intent")
    super.onRebind(intent)
  }


  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    log.warn("onStartCommand() $intent flags:$flags startId: $startId")
    if (intent?.action == ACTION_STOP) {
      log.error("NEED TO STOP!!!")

      cancelNotification()
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

      return START_STICKY_COMPATIBILITY
    }


/*    if (intent?.getBooleanExtra(EXTRA_DELETE, false) == true) {
      log.error("NEED TO STOP!!!")
      stopSelf()
    }*/
    return super.onStartCommand(intent, flags, startId)
  }


  override fun onDestroy() {
    log.warn("onDestroy()")
    super.onDestroy()
    cancelNotification()
    coroutineScope.cancel("Service stopped")

    runBlocking(Dispatchers.IO) {
      measureTimeMillis {
        if (ipfs.isStarted)
          ipfs.stop()
      }.also {
        log.debug("stopped in $it")
      }
    }

    messengerHandler.removeCallbacksAndMessages(null)
    clients.clear()
  }

  override fun onBind(intent: Intent?): IBinder? {
    log.debug("onBind() $intent")
    return messenger.binder
  }


}

private val log = org.slf4j.LoggerFactory.getLogger(IPFSService::class.java)
