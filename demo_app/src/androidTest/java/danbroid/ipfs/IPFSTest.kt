package danbroid.ipfs

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import danbroid.ipfs.api.API
import danbroid.ipfsd.app.appRegistry
import danbroid.ipfsd.client.ApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IPFSTest {

  lateinit var context: Context
  lateinit var apiClient: ApiClient


  @Before
  fun setup() {
    log.info("setup()")
    context = InstrumentationRegistry.getInstrumentation().targetContext
    apiClient = ApiClient.getInstance(context)
  }

  @Test
  fun test1() {
    log.info("test1()")

    runBlocking {
      context.appRegistry.test()
    }
  }

}

private val log = org.slf4j.LoggerFactory.getLogger(IPFSTest::class.java)
