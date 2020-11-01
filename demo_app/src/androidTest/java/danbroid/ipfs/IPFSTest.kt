package danbroid.ipfs

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import danbroid.ipfsd.app.appRegistry
import danbroid.ipfsd.client.ServiceApiClient
import danbroid.ipfsd.client.ipfsClient
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IPFSTest {

  lateinit var context: Context
  lateinit var apiClient: ServiceApiClient


  @Before
  fun setup() {
    log.info("setup()")
    context = InstrumentationRegistry.getInstrumentation().targetContext
    apiClient = ServiceApiClient.getInstance(context)
  }

  @Test
  fun test1() {
    log.info("test1()")
    context.ipfsClient.isServiceInstalled()


  }

}

private val log = org.slf4j.LoggerFactory.getLogger(IPFSTest::class.java)
