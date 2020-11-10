package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DagTest : CallTest() {


  @Test
  fun test() {
    log.info("test()")
    runBlocking {
      API.Dag.put().addData("\"Hello World\"").get(executor).value
    }

  }

}

private val log = org.slf4j.LoggerFactory.getLogger(KeyTests::class.java)
