package danbroid.ipfs.api.test

import org.junit.Test

class FilesTest : CallTest() {

  @Test
  fun listFiles() {
    ipfs.blocking {
      files.ls("/").get().valueOrThrow().also {
        log.debug("RESULT: $it")
      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(KeyTests::class.java)
