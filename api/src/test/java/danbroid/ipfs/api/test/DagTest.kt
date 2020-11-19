package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import danbroid.ipfs.api.dag
import danbroid.ipfs.api.utils.toJson
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*


class DagTest : CallTest() {

  data class Thang(val name: String, val age: Int, val date: Date = Date())


  @Test
  fun versionTest() {

    runBlocking {
      ipfs {

        log.debug("we are here..")
        basic.version().also {
          log.debug("version is $it")
        }
        delay(1000)
        log.debug("finished")
      }
    }


  }


  @Test
  fun test() {
    log.info("test()")
    runBlocking {
      ipfs {
        val cid = dag.put().addData("\"Hello World\"".toByteArray()).get(executor).value.cid.cid
        log.info("DAG: $cid")
        executor.dag<String>(cid).value.also {
          log.info("msg is $it")
        }

        val cid2 = dag.put().addData(Thang("Mr Man", 111).toJson().toByteArray())
          .get(executor).value.cid.cid
        log.info("added dag: $cid2")

        executor.dag<Thang>(cid2).also {
          log.info("THANG IS $it")
        }
      }
    }

  }

}

private val log = org.slf4j.LoggerFactory.getLogger(KeyTests::class.java)
