package danbroid.ipfs.api.test

import danbroid.ipfs.api.Types
import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.flow
import danbroid.ipfs.api.parseJson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.junit.Before
import org.junit.Test

class AddTest {

  @Before
  fun setup() {
    TestData.TestDirectory.createTestDir()
  }

  @Test
  fun test1() {
    log.debug("test1() adding hello world message..")
    ipfs.blocking {
      basic.add(
        data = TestData.HelloWorld.data,
        fileName = TestData.HelloWorld.name
      ).invoke {
        val data: String = it.reader.readText()
        log.debug("data: <${data}>")
        val file = data.parseJson<Types.File>()
        log.debug("file: $file")
        require(file.Hash == TestData.HelloWorld.cid) {
          "file.Hash != TestData.HelloWorld.cid"
        }
        require(file.Name == TestData.HelloWorld.name) {
          "file.Name != TestData.HelloWorld.name"
        }
      }
    }
  }

  @Test
  fun test2() {
    log.debug("test2() adding hello world message in directory..")
    ipfs.blocking {
      basic.add(
        data = TestData.HelloWorld.data,
        fileName = TestData.HelloWorld.name,
        wrapWithDirectory = true
      ).invoke().flow().toList().also {
        require(it.size == 2) { "invalid size: ${it.size}" }
        val file = it[0]
        require(file.Hash == TestData.HelloWorld.cid) {
          "file.Hash != TestData.HelloWorld.cid"
        }
        require(file.Name == TestData.HelloWorld.name) {
          "file.Name != TestData.HelloWorld.name"
        }
        require(it[1].Hash == TestData.HelloWorld.cid_with_directory) {
          "Invalid cid ${it[1].Hash} expecting ${TestData.HelloWorld.cid_with_directory}"
        }

      }
    }
  }

  @Test
  fun test3() {
    log.debug("test3() directory test")
    ipfs.blocking {
      basic.add().apply {
        addDirectory("a").apply {
          add(TestData.TestDirectory.msg1, "message.txt")
          addDirectory("b").add(TestData.TestDirectory.msg2, "message.txt")
        }
      }.invoke().flow().collect {
        log.debug("file: $it")
      }
    }
  }

  @Test
  fun test5() {
    log.info("test5() adding test dir: ${TestData.TestDirectory.testPath}")
    ipfs.blocking {
      basic.add(file = TestData.TestDirectory.testPath.toFile(), recurseDirectory = true).invoke {
        it.flow().collect {
          log.debug("file: $it")
        }
      }
      Unit
    }
  }

}


private val log = org.slf4j.LoggerFactory.getLogger(AddTest::class.java)
