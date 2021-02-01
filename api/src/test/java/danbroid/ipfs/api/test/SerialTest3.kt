package danbroid.ipfs.api.test

import danbroid.ipfs.api.DagNode
import danbroid.ipfs.api.Serializable
import danbroid.ipfs.api.dagNode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.junit.Test
import java.math.BigInteger


@Serializable
private data class Something(var name: String = "", var age: Int)

@Serializable
private data class ZOO3(var name: String = "", var age: Int, var s: Something? = null)

object BigIntSerializer : KSerializer<BigInteger> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("BigInt", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): BigInteger {
    log.debug("deserialize()")
    val s = decoder.decodeString()
    log.debug("s: $s")
    return s.toBigInteger()
  }

  override fun serialize(encoder: Encoder, value: BigInteger) {
    encoder.encodeString(value.toString())
  }

}

class SerialTest3 {


  @Test
  fun test() {
    log.info("test()")
    runBlocking {
      val zoo = ZOO3("Happy Place", 1972)
      zoo.s = Something("Something", 123)
      log.debug("zoo: $zoo")
      val link = zoo.dagNode()
      log.debug("link: $link")
      val json = Json.encodeToString(link)
      log.debug("json: $json")
      val zoo2Link = Json.decodeFromString<DagNode<ZOO3>>(json)
      log.info("zoo2Link: $zoo2Link")
      val zoo2 = zoo2Link.valueBlocking()
      log.debug("zoo2: $zoo2")
      require(zoo == zoo2) {
        "zoo != zoo2"
      }


      val bigNumberString = "12344444444444444444444444444444444444444444444444445"
      val bigInt = bigNumberString.toBigInteger()
      val bigNumberCID = "bafyreihydhtymenpvt4mrwrt35jqmf64fua6ius65o72wjs7jnuu6sxlra"
      val cid123 = "bafyreihbb6wszf7ordq4vfd3ab65wxjygixfgqe3qqc2qwbbdnzy4zifj4"
      val bigIntDag = dagNode(bigNumberCID, api, BigIntSerializer)
      log.info("BIG CID: ${bigIntDag.cid()} value: ${bigIntDag.value()}")


      val n = 123
      val nLink = n.dagNode()
      require(nLink.cid() == cid123) {
        "nLink.cid:${nLink.cid()} != $cid123"
      }
      require(nLink.value() == 123) {
        "nLink.value: ${nLink.value()} != 123"
      }

      require(dagNode<Int>(cid123, api).value() == 123) {
        "cid123.cid<Int>().value != 123"
      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(SerialTest2::class.java)
