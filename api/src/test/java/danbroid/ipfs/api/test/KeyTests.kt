package danbroid.ipfs.api.test

import kotlinx.coroutines.runBlocking
import org.junit.Test

class KeyTests : CallTest() {

  @Test
  fun listKeys() = runBlocking {
    api.key.ls().exec {
      log.debug("RESULT: $it")
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(KeyTests::class.java)
