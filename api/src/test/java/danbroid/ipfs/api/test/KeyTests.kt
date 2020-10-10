package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import kotlinx.coroutines.runBlocking
import org.junit.Test

class KeyTests : CallTest() {

  @Test
  fun listKeys() = callTest(API.Key.ls()) {
    log.debug("RESULT: $it")
  }

}

private val log = org.slf4j.LoggerFactory.getLogger(KeyTests::class.java)
