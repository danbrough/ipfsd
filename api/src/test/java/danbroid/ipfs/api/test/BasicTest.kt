package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.flow
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.junit.Test

class BasicTest {
  @InternalCoroutinesApi
  @Test
  fun ls() {
    val path = "/ipfs/QmZh1JZaEsNjGS1CcSEPPohitG9oEVdYb9uUsmhveSAsAn"
    log.info("listing $path")

    api.blocking {

      basic.ls(path, resolveSize = false, resolveType = true, stream = true).flow()
        .collect {
          log.info("objects: $it length: ${it.Objects.size}")
          // log.info("objects: $it length: ${it.Objects.size}")
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
