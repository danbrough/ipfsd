package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import danbroid.ipfs.api.ResponseProcessors
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class BlockTest : CallTest() {

  @Test
  fun blockTest() {
    log.info("blockTest()")

    val msg = "BlockTest put message at ${Date()}"

    callTest(API.Block.put().addData(msg)) {
      log.debug("response: $it")
      val key = it.getOrThrow().key

      callTest(API.Block.get(key, ResponseProcessors.stringReader())) {
        log.debug("response: $it")
      }
    }
  }


}

private val log = org.slf4j.LoggerFactory.getLogger(AddTest::class.java)
