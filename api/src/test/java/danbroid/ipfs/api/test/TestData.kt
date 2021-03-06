package danbroid.ipfs.api.test

import danbroid.ipfs.api.IpfsLink
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Serializable
@IpfsLink
data class UNI(
  var name: String,
  var year: Int,
  @Contextual @IpfsLink var departments: Set<Department>
) {

  @Serializable
  data class Address(var street: String, var suburb: String, var postcode: Int, var city: String)

  @Serializable
  @IpfsLink
  data class Department(var name: String)

  @Serializable
  @Contextual
  @IpfsLink
  var address: Address? = null

  @Contextual
  var date: Date? = null

  @Serializable
  var message: String? = null
}


object TestData {
  const val CID_EMPTY_OBJECT = "QmdfTbBqBPQ7VNxZEYEj14VmRuZBkqFbiwReogJgS1zR1n"

  object DagTest {

    @Serializable
    data class Person(val name: String, val age: Int)

    val dan = Person("Dan", 12)


  }


  object HelloWorld {
    const val cid = "QmWATWQ7fVPP2EFGu71UkfnqhYXDYH566qy47CnJDgvs8u"
    const val cid_with_directory = "QmStcF58DBTXnJDEWeobXbCeTGiM6NxfLmcz9S95HJ5Br7"
    const val data = "Hello World\n"
    const val name = "hello_world.txt"
    const val dag_cid = "bafyreictpomgsgsseyyvxq7vagj5womjfc3rjbczb2rj4tbjybe3t3s7xa"

  }

  @Suppress("NewApi")
  object TestDirectory {
    const val msg1 = "message.txt in directory a\n"
    const val msg2 = "message.txt in directory a/b\n"
    const val CID_TEST_DIR = "QmZ5GtGGqYdcFm3GA5dWfiWjuvdBnQJtkSTVsNeRuFRQoL"
    val testPath =
      Paths.get(System.getProperty("java.io.tmpdir")).resolve("add_test").resolve("a")


    @Suppress("NewApi")
    fun createTestDir() {
      @Suppress("NewApi")
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
    }

  }
}

//private val log = org.slf4j.LoggerFactory.getLogger(TestData::class.java)
