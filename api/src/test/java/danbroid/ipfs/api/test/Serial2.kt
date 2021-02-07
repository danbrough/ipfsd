package danbroid.ipfs.api.test

import danbroid.ipfs.api.*
import kotlinx.serialization.serializer
import org.junit.Test


@Serializable
data class Stuff(val name: String, var msg: DagLink<String>? = null)


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
      stuff.msg = dagLink<String>(cid)
      log.debug("stuff: ${stuff.toJson()}")
      val cid2 = stuff.dagCid(this)
      log.debug("cid2: ${cid2}")

      val msg2 =
        dag.get<String>("$cid2/msg").json()
      log.warn("MSG2: ${msg2}")

      val stuff2 = dag.get<Stuff>(cid2).json()
      log.debug("stuff2: ${stuff2}")
      require(stuff == stuff2){
        "stuff != stuff2"
      }

    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(Serial2::class.java)
