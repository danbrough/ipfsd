package danbroid.ipfsd.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Messenger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis


class IPFSService : Service() {
  companion object {
    const val MSG_POLL_STATS = 10241
  }

  var ipfs: IPFS? = null

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
            if (ipfs?.isStarted == true)
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

    messengerHandler = Handler(Looper.myLooper()!!, messengerCallback)
    messenger = Messenger(messengerHandler)

    GlobalScope.launch(Dispatchers.IO) {
      measureTimeMillis {
        ipfs = IPFS(this@IPFSService).apply {
          enableNamesysPubsub()
          enablePubsubExperiment()
          start()
          log.debug("started: ${isStarted}")
        }
      }.also {
        log.debug("created ipfs in $it")
      }

      readStats()
      withContext(Dispatchers.Main) {

        if (ipfs?.isStarted == true) {
          val msg = IPFSMessage.SERVICE_STARTED.toMessage()
          clients.forEach {
            it.send(msg)
          }
        }
      }
    }


  }

  private fun readStats() {
    GlobalScope.launch(Dispatchers.IO) {
      ipfs?.newRequest("stats/bw")?.send()?.also {
        it.decodeToString().also {
          log.trace(it)
        }
        messengerHandler.sendEmptyMessageDelayed(MSG_POLL_STATS, 10000)
      }
    }
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    log.warn("onStartCommand() $intent flags:$flags startId: $startId")
    return super.onStartCommand(intent, flags, startId)
  }


  override fun onDestroy() {
    log.warn("onDestroy()")
    super.onDestroy()
    ipfs?.also {
      ipfs = null
      GlobalScope.launch(Dispatchers.IO) {
        measureTimeMillis {
          it.stop()
        }.also {
          log.debug("stopped in $it")
        }
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
