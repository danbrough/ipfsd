package danbroid.ipfsd.demo.app.test

import danbroid.ipfs.api.*
import org.junit.Test

@Serializable
data class Thang(var name: String, var age: Int)

class Test1 {
  @Test
  fun test1() {
    log.info("test1()")

    ipfs.blocking {
      log.debug("running test")
      val thang = Thang("Dude", 23)
      val data = thang.toJson()
      log.debug("list: $thang data: $data")
      dag.put(thang).json().also {
        log.info("CID: $it")
      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(Test1::class.java)
