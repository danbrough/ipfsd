package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.nio.file.Files
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
    val testDir = Files.createDirectories(Files.createTempDirectory("addTest").resolve("a"))

    val msg1 = "message.txt in directory a\n"
    val msg2 = "message.txt in directory a/b\n"
    Files.write(testDir.resolve("message.txt"), msg1.toByteArray())

    Files.write(
      Files.createDirectory(testDir.resolve("b")).resolve("message.txt"),
      msg2.toByteArray()
    )

    var cid: String? = null
    callTest(
      API.Basic.add(file = testDir.toFile(), recurseDirectory = true)
    ) {
      cid = it.value.hash
      log.debug("result: ${it.value}")
    }
    log.warn("DIRECTORY: $cid")
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(AddTest::class.java)
