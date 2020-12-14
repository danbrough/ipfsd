package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
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
        fileName = TestData.HelloWorld.name,
        wrapWithDirectory = true
      ).apply {

      }.invoke()
        .use {
          val data: String = it.reader.readText()
          log.error("data: <${data}>")
        }
    }
  }
}


private val log = org.slf4j.LoggerFactory.getLogger(AddTest::class.java)
