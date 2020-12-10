package danbroid.ipfs.api.test

import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.PartContainer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before

abstract class CallTest {

  object SharedData {
    var cid: String? = null
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
