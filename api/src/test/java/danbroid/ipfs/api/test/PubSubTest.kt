package danbroid.ipfs.api.test

import danbroid.ipfs.api.Types
import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.jsonSequence
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import okhttp3.OkHttpClient
import org.junit.Test
import java.util.*
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
      log.warn("STARTED")
      val def = launch(Dispatchers.IO) {
        pubsub.sub(topic).invoke {
          it.reader.jsonSequence().map { Json.decodeFromJsonElement<Types.Message>(it) }.forEach {
            log.debug("message: $it dataString: ${it.dataString}")
            if (it.dataString == "stop") {
              return@invoke
            }
          }
        }
      }
      def.invokeOnCompletion {
        log.error("DEF DONE: $it")
      }

      log.warn("HERE!!!!!!!")

      delay(1000)
      log.debug("sending message..")
      pubsub.pub(topic, "Hello from here at ${Date()}").invoke().also {
        log.info("response: $it")
      }
      delay(10000)

      if (def.isActive) {
        log.debug("sending stop ..")
        pubsub.pub(topic, "stop").invoke().also {
          log.info("response: $it")
        }

        log.debug("waiting for stop to be processed")
        def.join()
      }

      log.info("all done")
    }
  }

}


private val log = org.slf4j.LoggerFactory.getLogger(PubSubTest::class.java)
