package danbroid.ipfs.api.test

import org.junit.Test
import java.util.*

class BlockTest {

  @Test
  fun blockTest() {
    log.info("blockTest()")

    val msg = "BlockTest put message at ${Date()}"

    ipfs.blocking {
      block.put(data = msg).get().valueOrThrow().also {
        log.debug("result: $it")
      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(AddTest::class.java)
