package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import org.junit.Test

class NetworkTest {
  @Test
  fun idTest() {
    log.info(
      "ID: ${
        ipfs.blocking {
          network.id().invoke().json().first().also { 
            log.debug("ID: $it")
          }
        }
      }"
    )
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(NetworkTest::class.java)
