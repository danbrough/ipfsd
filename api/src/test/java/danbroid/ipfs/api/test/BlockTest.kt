package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import org.junit.Test
import java.util.*

class BlockTest : CallTest() {

  @Test
  fun blockTest() {
    log.info("blockTest()")

    val msg = "BlockTest put message at ${Date()}"

    callTest(API.Block.put().addData(msg)) {
      log.debug("response: $it")


     /* callTest(API.Block.get(key)) {
        log.debug("response: $it")
      }*/
    }
  }


}

private val log = org.slf4j.LoggerFactory.getLogger(AddTest::class.java)
