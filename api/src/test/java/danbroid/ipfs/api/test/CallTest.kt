package danbroid.ipfs.api.test

import OkHttpCallExecutor
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.CallExecutor
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


  protected val executor: CallExecutor
    get() = _executor!!

  companion object {
    private var _executor: CallExecutor? = null

    @BeforeClass
    @JvmStatic
    fun beforeClass() {
      _executor = OkHttpCallExecutor()
    }

    @AfterClass
    @JvmStatic
    fun afterClass() {
      _executor = null
    }
  }

  open fun <T> callTest(call: PartContainer<T>, handler: suspend (ApiCall.ApiResponse<T>) -> Unit) =
    runBlocking {
      call.exec(executor).collect {
        handler.invoke(it)
      }
    }


  @Before
  open fun setup() {

  }

}

private val log = org.slf4j.LoggerFactory.getLogger(CallTest::class.java)
