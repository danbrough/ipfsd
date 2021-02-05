package danbroid.ipfs.api.test

import danbroid.ipfs.api.*
import kotlinx.serialization.SerialName
import org.junit.Test





@Serializable
data class Stuff(val name: String, var msg: DagLink? = null)


class Serial2 {
  @Test
  fun test() {
    log.info("test()")
    api.blocking {
      val msg = "Hello World"
      val cid = msg.dagCid(this)
      log.debug("cid: $cid")
      val stuff = Stuff("Some things")
      log.debug("stuff: ${stuff.toJson()}")
      stuff.msg = DagLink(cid)
      log.debug("stuff: ${stuff.toJson()}")
      val cid2 = stuff.dagCid(this)
      log.debug("cid2: ${cid2}")

      val msg2 =
        dag.get<String>("bafyreibhdgu56i3la7jxmo4ayfet6z3nsgty2ocjp77paxg5chglgbfpci/msg").json()
      log.warn("MSG2: ${msg2}")

      val stuff2 = dag.get<Stuff>(cid2).json()
      log.debug("stuff2: ${stuff2}")
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(Serial2::class.java)
