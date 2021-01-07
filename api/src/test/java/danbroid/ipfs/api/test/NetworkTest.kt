package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.ipfs
import danbroid.ipfs.api.json
import org.junit.Test

class NetworkTest {
  @Test
  fun idTest() {
    val id = ipfs.blocking {
      network.id().json().also {
        log.debug("ID: $it")
      }
    }
    log.info("ID: $id")
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(NetworkTest::class.java)
