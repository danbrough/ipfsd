package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import org.junit.Test

class Misc {
  @Test
  fun basicAuth() {
    log.info("basicAuth()")
    api.blocking {
      val id = network.id().json()
      log.debug("id: $id")
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(Misc::class.java)
