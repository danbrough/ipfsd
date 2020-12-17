package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import danbroid.ipfs.api.jsonSequence
import org.junit.Test

class BasicTest {
  @Test
  fun ls() {
    val path = "/ipfs/QmZh1JZaEsNjGS1CcSEPPohitG9oEVdYb9uUsmhveSAsAn"
    log.info("listing $path")

    ipfs.blocking {

      basic.ls(path, resolveSize = false, resolveType = true,stream = true).jsonSequence {
        log.info("objects: $it length: ${it.Objects.size}")
     /*   it.Objects[0].Links[0].Hash.also {
          require(it == "QmT83ehGdr7s7oSfrVP759xJnK8kWjYNqc71HJDN7DgUu7") {
            "Invalid hash: $it"
          }
        }*/
      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(BasicTest::class.java)
