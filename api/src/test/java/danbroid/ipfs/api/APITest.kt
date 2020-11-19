package danbroid.ipfs.api

import OkHttpCallExecutor
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Test

open class IPFSAPI(val executor: CallExecutor = OkHttpCallExecutor()) {

  class Basic(private val executor: CallExecutor) {
    fun version() = apiCall<API.Basic.VersionResponse>(executor, "version")
  }

  @JvmField
  val basic = Basic(executor)

  protected val coroutineScope = CoroutineScope(Dispatchers.IO)

  operator fun invoke(block: suspend IPFSAPI.() -> Unit): Job {
    return coroutineScope.launch {
      block.invoke(this@IPFSAPI)
    }
  }
}


class APITest {

  @Before
  fun setUp() {
  }

  object ipfs : IPFSAPI(OkHttpCallExecutor())

  @Test
  fun test() {
    log.info("running test")
    runBlocking(Dispatchers.IO) {
      ipfs {
        log.debug("getting version..")
        basic.version().get().valueOrThrow().also {
          log.info("VERSION: $it")
        }
      }.join()

      log.info("runBlocking finished")
    }
  }

  @After
  fun tearDown() {
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(APITest::class.java)
