package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.File
import java.util.*

class AddTest : CallTest() {

  @Test
  fun idTest() {
    log.debug("test1()")
    runBlocking {
      callTest(API.Network.id()) {
        log.info("id: $it")
      }
    }
  }

  @Test
  fun addMessage() {
    log.info("addTest()")
    val msg = "${javaClass.simpleName} addMessage at ${Date()}\n"
    callTest(API.Basic.add(msg, fileName = "test_message.txt")) {
      SharedData.cid = it.value.hash
    }
  }


  @Test
  fun addDirectory() {
    log.info("addDirectory()")
    callTest(
      API.Basic.add(file = File("/tmp/test_dir"), recurseDirectory = true)
    ) {
      log.debug("result: $it")
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(AddTest::class.java)
