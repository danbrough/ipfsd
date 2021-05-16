package danbroid.ipfs.api.test

import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import danbroid.ipfs.api.okhttp.OkHttpExecutor
import danbroid.ipfs.api.toJson
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.util.*

class InfuraTest {

  companion object {
    const val apiUrl = "https://ipfs.infura.io:5001/api/v0"
    lateinit var api: IPFS

    @BeforeClass
    @JvmStatic
    fun setup() {
      log.info("setup()")
      api = IPFS(OkHttpExecutor(apiUrl))
    }

    @AfterClass
    @JvmStatic
    fun tearDown() {
      log.info("tearDown()")
    }
  }

  @Test
  fun test1() {
    runBlocking {
      val msg = "Hello World on the ${Date()}"
      val cid = api.dag.put(msg.toJson()).json().Cid.path
      log.debug("received cid: $cid")

      val msg2 = api.dag.get<String>(cid).json()
      require(msg2 == msg) {
        "Invalid message: $msg2"
      }
      /*val cid = api.dag.put(data = msg).json().Cid.path
      log.debug("cid is $cid")*/
    }
  }

  @Test
  fun test2() {
    danbroid.ipfs.api.test.api.blocking {
      log.debug("id: ${network.id().json()}")
    }
  }
}

