package danbroid.ipfs

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4Builder
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import danbroid.ipfs.api.API
import danbroid.ipfs.api.OkHttpCallExecutor
import danbroid.ipfsd.service.IPFS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IPFSTest {
  companion object {
    lateinit var ipfs: IPFS
    val executor = OkHttpCallExecutor()

    @BeforeClass
    @JvmStatic
    fun setup() {
      log.info("setup()")
      val context = InstrumentationRegistry.getInstrumentation().targetContext
      runBlocking(Dispatchers.IO) {
        log.debug("creating ipfs")
        ipfs = IPFS(context)
        log.debug("starting ipfs")
        ipfs.start()

      }
    }

    @AfterClass
    @JvmStatic
    fun tearDown() {
      log.info("tearDown()")
      runBlocking {
        ipfs.stop()
        log.debug("ipfs stopped")
      }
    }
  }

  @Test
  fun test1() {
    log.debug("publishing something to poiqwe098123")
    runBlocking {
      log.debug("calling id")
      executor.exec(API.id()) {
        log.debug("GOT RESULT: $it")
      }
      log.debug("calling publish")
      executor.exec(API.PubSub.publish("poiqwe098123", "Hello World")) {
        log.debug("GOT RESULT: $it")
      }
    }
  }

}

private val log = org.slf4j.LoggerFactory.getLogger(IPFSTest::class.java)
