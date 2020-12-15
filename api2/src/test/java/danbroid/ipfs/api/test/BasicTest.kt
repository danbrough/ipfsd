package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import org.junit.Test

class BasicTest {
  @Test
  fun test1() {
    ipfs.blocking {
      val arg = "/ipfs/QmZh1JZaEsNjGS1CcSEPPohitG9oEVdYb9uUsmhveSAsAn"
      basic.ls(arg,stream = false).json {
        log.debug("objects: $it length: ${it.Objects.size}")
        it.Objects[0].Links[2].Hash.also {
          require(it == "QmT83ehGdr7s7oSfrVP759xJnK8kWjYNqc71HJDN7DgUu7") {
            "Invalid hash: $it"
          }
        }
      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(BasicTest::class.java)
