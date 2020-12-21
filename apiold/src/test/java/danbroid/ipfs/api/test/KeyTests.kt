package danbroid.ipfs.api.test

import org.junit.Test

class KeyTests : CallTest() {

  @Test
  fun listKeys() {
    ipfs.blocking {
      key.ls().get().valueOrThrow().also {
        log.debug("RESULT: $it")
      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(KeyTests::class.java)