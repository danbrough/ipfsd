package danbroid.ipfs.api.test

import OkHttpCallExecutor
import danbroid.ipfs.api.API
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfs.api.dag
import danbroid.ipfs.api.utils.toJson
import kotlinx.coroutines.*
import org.junit.Test
import java.util.*


class DagTest : CallTest() {

  data class Thang(val name: String, val age: Int, val date: Date = Date())


  open class IPFSContext(protected val executor: CallExecutor = OkHttpCallExecutor()) {

    private var coroutineScope = CoroutineScope(Dispatchers.IO)

    class Basic(ctx: IPFSContext) {
      val version: suspend () -> API.Basic.VersionResponse = {
        API.Basic.version().get(ctx.executor).valueOrThrow()
      }
    }

    val basic = Basic(this)


    operator fun invoke(block: suspend IPFSContext.() -> Unit) {
      coroutineScope.launch {
        block.invoke(this@IPFSContext)
      }
    }
  }


  object ipfs : DagTest.IPFSContext()

  @Test
  fun versionTest() {

    ipfs {
      runBlocking {

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
      val cid = API.Dag.put().addData("\"Hello World\"".toByteArray()).get(executor).value.cid.cid
      log.info("DAG: $cid")
      executor.dag<String>(cid).value.also {
        log.info("msg is $it")
      }

      val cid2 = API.Dag.put().addData(Thang("Mr Man", 111).toJson().toByteArray())
        .get(executor).value.cid.cid
      log.info("added dag: $cid2")

      executor.dag<Thang>(cid2).also {
        log.info("THANG IS $it")
      }
    }

  }

}

private val log = org.slf4j.LoggerFactory.getLogger(KeyTests::class.java)
