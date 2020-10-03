package danbroid.ipfs.service.test

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import danbroid.ipfs.api.utils.addUrlArgs
import danbroid.ipfsd.service.IPFS
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IPFSServiceTest {


  companion object {
    lateinit var ipfs: IPFS
    val context: Context by lazy {
      InstrumentationRegistry.getInstrumentation().targetContext
    }

    @BeforeClass
    @JvmStatic
    fun setup() {
      log.info("setup()")
      ipfs = IPFS(context)
      log.debug("starting ipfs ..")
      ipfs.enablePubsubExperiment()
      ipfs.enableNamesysPubsub()
      ipfs.start()
      log.debug("ipfs started: ${ipfs.isStarted}")
    }

    @AfterClass
    @JvmStatic
    fun teardown() {
      log.info("teardown()")
      log.debug("stopping ipfs ..")
      ipfs.stop()
      log.debug("done")
    }
  }

  @Test
  fun test1() {
    log.info("test1()")
    val request =
      ipfs.newRequest("pubsub/sub".addUrlArgs("arg" to "poiqwe098123", "discover" to true))
        .withBody("").send()

  }

  @Test
  fun test2() {
    log.info("test2()")
  }


}

private val log = org.slf4j.LoggerFactory.getLogger(IPFSServiceTest::class.java)
