package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import org.junit.Test

class NetworkTest {
  @Test
  fun idTest() {
    log.info(
      "ID: ${
        ipfs.blocking {
          network.id().invoke().also {
            log.info("RESULT: $it")
          }
        }
      }"
    )
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(NetworkTest::class.java)
