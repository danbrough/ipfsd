package danbroid.ipfs.api.test

import danbroid.ipfs.api.*
import danbroid.ipfs.api.okhttp.OkHttpCallExecutor
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass

abstract class CallTest {

  object SharedData {
    var cid: String? = null
  }

  companion object {

    var executor: CallExecutor? = null

    @BeforeClass
    @JvmStatic
    fun beforeClass() {
      executor = OkHttpCallExecutor()
    }

    @AfterClass
    @JvmStatic
    fun afterClass() {
      executor = null
    }
  }

  open fun <T> callTest(call: ApiCall<T>, handler: ResultHandler<T>) = runBlocking {
    executor!!.exec(call, handler)
  }


  @Before
  open fun setup() {

  }

}

private val log = org.slf4j.LoggerFactory.getLogger(CallTest::class.java)
