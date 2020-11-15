package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths
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

  val msg1 = "message.txt in directory a\n"
  val msg2 = "message.txt in directory a/b\n"
  val test_dir_cid = "QmZ5GtGGqYdcFm3GA5dWfiWjuvdBnQJtkSTVsNeRuFRQoL"

  val testPath =
    Paths.get(System.getProperty("java.io.tmpdir")).resolve("ipfsd_test").resolve("a")

  @Test
  fun addDirectory() {
    log.info("addDirectory()")


    fun deleteDir(path: Path) {
      if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
        Files.newDirectoryStream(path).forEach {
          deleteDir(it)
        }
      }
      Files.deleteIfExists(path)
    }

    deleteDir(testPath)
    val testDir = Files.createDirectories(testPath)

    Files.write(testDir.resolve("message.txt"), msg1.toByteArray())

    Files.write(
      Files.createDirectory(testDir.resolve("b")).resolve("message.txt"),
      msg2.toByteArray()
    )

    var cid: String? = null
    callTest(API.Basic.add(file = testDir.toFile(), recurseDirectory = true, onlyHash = true)) {
      log.debug("result: $it")
      cid = it.value.hash
    }

    Assert.assertEquals("Invalid CID", test_dir_cid, cid)
  }

  @Test
  fun addDirectory2() {
    callTest(
      API.Basic.add(onlyHash = true).addDirectory("a").apply {
        addData(msg1.toByteArray(), "message.txt")
        addDirectory("b").addData(msg2.toByteArray(), "message.txt")
      }
    ) {
      log.error("RESULT: $it")
    }
  }
}


private val log = org.slf4j.LoggerFactory.getLogger(AddTest::class.java)
