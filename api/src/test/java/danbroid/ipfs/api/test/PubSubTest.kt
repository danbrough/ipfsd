package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.flow
import danbroid.ipfs.api.ipfs
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.junit.Test
import java.util.logging.Level
import java.util.logging.Logger

class PubSubTest {
  init {
    Logger.getLogger(OkHttpClient::javaClass.name).setLevel(Level.FINE)
  }

  val topic = "test"


  @Test
  fun test1() {
    ipfs.blocking {

      val job = launch {

        log.warn("launched")

        while (isActive) {
          runCatching {
            log.info("subscribing to $topic")
            pubsub.sub(topic).flow().collect {
              log.info("msg: ${it} data:${it.dataString} isActive: $isActive")
              if (it.dataString == "stop") {
                log.debug("received stop")
                cancel("Received stop")
              }
            }
          }.exceptionOrNull()?.also {
            //like if you killed the ipfs daemon
            log.error("something bad happened: $it")
            //try again through the loop
            delay(1000)
          }
        }
      }


      job.invokeOnCompletion {
        log.warn("job finished: ${it?.message}")
      }

      for (n in 1..8) {
        delay(1000)
        if (!job.isActive) break
      }

      if (job.isActive) {
        log.info("job.isActive .. sending stop")
        pubsub.pub(topic, "stop").invoke()
        job.join()
      }

      log.info("THE END")
    }
  }

}


private val log = org.slf4j.LoggerFactory.getLogger(PubSubTest::class.java)
