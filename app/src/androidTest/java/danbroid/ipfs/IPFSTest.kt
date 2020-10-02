package danbroid.ipfs

import androidx.test.platform.app.InstrumentationRegistry
import danbroid.ipfs.api.API
import danbroid.ipfs.api.OkHttpCallExecutor
import danbroid.ipfsd.service.IPFS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

abstract class IPFSTest {
  lateinit var api: API
  lateinit var ipfs: IPFS

  @Before
  fun setup() {
    log.info("setup()")
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    runBlocking(Dispatchers.IO) {
      log.debug("creating ipfs")
      ipfs = IPFS(context)
      log.debug("starting ipfs")
      ipfs.start()
      api = API(OkHttpCallExecutor())
    }
  }

  @After
  fun tearDown() {
    log.info("tearDown()")
    runBlocking {
      ipfs.stop()
      log.debug("ipfs stopped")
    }
  }

}

private val log = org.slf4j.LoggerFactory.getLogger(IPFSTest::class.java)
