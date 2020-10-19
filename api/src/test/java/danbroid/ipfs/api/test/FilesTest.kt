package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FilesTest : CallTest() {

  @Test
  fun test() {
  }

  fun listFiles() = callTest(API.Files.ls("/")) {
    log.debug("RESULT: $it")
  }

}

private val log = org.slf4j.LoggerFactory.getLogger(KeyTests::class.java)
