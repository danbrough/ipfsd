package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.flow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import org.junit.Test
import java.util.logging.Level
import java.util.logging.Logger

class PubSubTest {
  init {
    Logger.getLogger(OkHttpClient::javaClass.name).setLevel(Level.FINE)
  }

  @Test
  fun test1() {
    val topic = "test"
    ipfs.blocking {
      log.warn("starting job")

      pubsub.sub(topic).invoke { response ->
        val flow = response.flow().flowOn(Dispatchers.IO)
        supervisorScope {
          launch {
            flow.collect {
              log.debug("message: $it data:${it.dataString}")
              if (it.dataString == "stop") {
                log.warn("received stop.. cancelling")
                response.close()
                cancel("Received stop message")
              }
            }
          }.invokeOnCompletion {
            log.error("FINISHED: $it")
          }
        }
      }
    }
  }

  @Test
  fun test2() {
    val topic = "test"
    ipfs.blocking {

      val job = supervisorScope {
        launch {
          log.warn("starting job")
          var running = true
          while (running) {
            log.debug("subscribing to $topic")

            pubsub.sub(topic).invoke().use { response ->
              val flow = response.flow().cancellable()
              //flow.catch { log.trace("an error happened: ${it.message}") }
              flow.collect {

                log.info("msg: ${it} data:${it.dataString} isActive: $isActive")
                if (it.dataString == "stop") {
                  log.debug("received stop")
                  running = false
                  cancel("Received stop")
                  delay(1)
                  yield()
                }
              }
              log.info("at this bit")
            }
          }
        }
      }
    }

    log.info("THE END")
  }

}


private val log = org.slf4j.LoggerFactory.getLogger(PubSubTest::class.java)
