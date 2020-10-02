package danbroid.ipfs.api.test

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.File
import java.util.*

class AddTest : CallTest() {

  @Test
  fun idTest() {
    log.debug("test1()")
    runBlocking {
      api.id().asFlow().first()?.also {
        log.info("id: $it")
      }
    }
  }

  @Test
  fun addMessage() {
    log.info("addTest()")
    val msg = "${javaClass.simpleName} addMessage at ${Date()}\n"
    runBlocking {
      api.add(msg, fileName = "test_message.txt").exec {
        log.debug("result: $it")
        SharedData.cid = it?.hash
      }
    }
  }

  @Test
  fun addDirectory() {
    log.info("addDirectory()")
    runBlocking {
      api.add(file = File("/tmp/test_dir"), recurseDirectory = true).exec {
        log.debug("result: $it")
      }
    }
  }


}

private val log = org.slf4j.LoggerFactory.getLogger(AddTest::class.java)
