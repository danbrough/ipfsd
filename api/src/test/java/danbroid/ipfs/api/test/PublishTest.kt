package danbroid.ipfs.api.test

import kotlinx.coroutines.runBlocking
import org.junit.Test

class PublishTest : CallTest() {
  @Test
  fun publishTest() {
    log.info("publishTest()")
    val cid = SharedData.cid
    if (cid == null) {
      log.error("cid is null")
      return
    }

    runBlocking {
      api.name.publish(cid,key = "test").exec {
        log.warn("published $it")
      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(PublishTest::class.java)
