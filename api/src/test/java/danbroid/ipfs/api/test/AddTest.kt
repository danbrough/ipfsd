package danbroid.ipfs.api.test

import kotlinx.coroutines.flow.collectLatest
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
    log.info("ID: ${ipfs.blocking { network.id().get().valueOrThrow() }}")
  }

  @Test
  fun addMessage() {
    log.info("addTest()")
    val msg = "${javaClass.simpleName} addMessage at ${Date()}\n"
    ipfs.blocking {
      SharedData.cid = basic.add(msg, fileName = "test_message.txt").get().valueOrThrow().hash
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
    runBlocking {
      ipfs.basic.add(file = testDir.toFile(), recurseDirectory = true, onlyHash = false).flow()
        .collectLatest {
          log.debug("got $it")
          cid = it.valueOrThrow().hash
        }


      log.info("finished collect: cid: $cid")
      Assert.assertEquals("Invalid CID", test_dir_cid, cid)
      deleteDir(testPath)
    }
  }

  @Test
  fun addDirectory2() {
    runBlocking {
      var cid: String? = null

      log.debug("doing add..")
      ipfs.basic.add(onlyHash = true).apply {
        addDirectory("a").apply {
          addData(msg1.toByteArray(), "message.txt")
          addDirectory("b").addData(msg2.toByteArray(), "message.txt")
        }
      }.collect {
        log.info("RESULT: $it")
        cid = it.valueOrThrow().hash
      }
      log.info("finished collect: cid: $cid")
      Assert.assertEquals("Invalid CID", test_dir_cid, cid)
    }
  }
}


private val log = org.slf4j.LoggerFactory.getLogger(AddTest::class.java)
