package danbroid.ipfs.api.test

import OkHttpCallExecutor
import danbroid.ipfs.api.API
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.PartContainer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass

abstract class CallTest {

  object SharedData {
    var cid: String? = null
  }


  protected val ipfs: API
    get() = _ipfs!!

  companion object {
    private var _ipfs: API? = null

    @BeforeClass
    @JvmStatic
    fun beforeClass() {
      _ipfs = API(OkHttpCallExecutor())
    }

    @AfterClass
    @JvmStatic
    fun afterClass() {
      _ipfs = null
    }
  }

  open fun <T> callTest(call: PartContainer<T>, handler: suspend (ApiCall.ApiResponse<T>) -> Unit) =
    runBlocking {
      ipfs {
        executor.exec(call as ApiCall<T>).collect(handler)
      }

    }


  @Before
  open fun setup() {

  }

}

private val log = org.slf4j.LoggerFactory.getLogger(CallTest::class.java)
