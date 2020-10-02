package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import danbroid.ipfs.api.OkHttpCallExecutor
import org.junit.Before
import org.junit.BeforeClass

abstract class CallTest {

  object SharedData {
    var cid: String? = null
  }

  companion object {

    lateinit var api: API

    @BeforeClass
    @JvmStatic
    fun beforeClass() {
      log.error("before class")
      api = API(executor = OkHttpCallExecutor())
    }

  }

  @Before
  open fun setup() {

  }

}

private val log = org.slf4j.LoggerFactory.getLogger(CallTest::class.java)
