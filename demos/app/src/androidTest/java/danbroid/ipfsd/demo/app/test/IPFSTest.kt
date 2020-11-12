package danbroid.ipfsd.demo.app.test

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.client.ServiceApiClient
import danbroid.ipfsd.client.ipfsClient
import danbroid.ipfsd.demo.app.appRegistry
import danbroid.ipfsd.demo.app.shopping.ShoppingList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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
    Assert.assertEquals(
      "${IPFSD.SERVICE_PKG} is not installed",
      true,
      apiClient.serviceClient.isServiceInstalled()
    )


  }

  @Test
  fun test1() {
    log.info("test1()")
    context.ipfsClient.isServiceInstalled()
    runBlocking {
      context.appRegistry.get(ShoppingList::class.java).forEach {
        log.warn("SHOPPING LIST: $it")
      }
    }

  }

}

private val log = org.slf4j.LoggerFactory.getLogger(IPFSTest::class.java)