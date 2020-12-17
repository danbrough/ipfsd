package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
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
      log.warn("STARTED")

      pubsub.sub(topic).invoke { response ->
        val flow = response.flow().flowOn(Dispatchers.IO)
        supervisorScope {
          launch {
            flow.collect {
              log.debug("message: $it data:${it.dataString}")
              if (it.dataString == "stop") {
                log.warn("received stop.. cancelling")
                response.close()
              }
            }
          }.invokeOnCompletion {
            log.error("FINISHED: $it")
          }
        }
      }
    }
  }
}


private val log = org.slf4j.LoggerFactory.getLogger(PubSubTest::class.java)
