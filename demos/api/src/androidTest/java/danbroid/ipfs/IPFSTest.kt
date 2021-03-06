package danbroid.ipfs

import OkHttpCallExecutor
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import danbroid.ipfs.api.ApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
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

  suspend fun <T> callTest(call: ApiCall<T>) {
    call.get(executor).also {
      log.debug("result: $it")
    }
  }

  @Test
  fun test1() {
    log.debug("publishing something to poiqwe098123")
    runBlocking {
      log.debug("calling id")
      callTest(IPFS.Network.id())

      log.debug("calling publish")
      callTest(IPFS.PubSub.publish("poiqwe098123", "Hello World"))
      log.debug("calling repo/stat")
      callTest(IPFS.Repo.stat())

    }
  }

}

private val log = org.slf4j.LoggerFactory.getLogger(IPFSTest::class.java)
