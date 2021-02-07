package danbroid.ipfs.api.test

import danbroid.ipfs.api.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
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
    api.blocking {
      val zoo = ZOO3("Happy Place", 1972)
      zoo.s = Something("Something", 123)
      log.debug("zoo: $zoo")
      val link:DagLink<ZOO3> = zoo.dagLink(this)
      log.debug("link: $link")
      val json = Json.encodeToString(link)
      log.debug("json: $json")
      val zoo2Link = Json.decodeFromString<DagLink<ZOO3>>(json)
      log.info("zoo2Link: $zoo2Link")
      val zoo2 = zoo2Link.value(this)
      log.debug("zoo2: $zoo2")
      require(zoo == zoo2) {
        "zoo != zoo2"
      }


      val bigNumberString = "12344444444444444444444444444444444444444444444444445"
      val bigInt = bigNumberString.toBigInteger()
      val bigNumberCID = "bafyreihydhtymenpvt4mrwrt35jqmf64fua6ius65o72wjs7jnuu6sxlra"
      val cid123 = "bafyreihbb6wszf7ordq4vfd3ab65wxjygixfgqe3qqc2qwbbdnzy4zifj4"
      val bigIntLink: DagLink<BigInteger> =
        DagLink(bigNumberCID, serializer = BigIntSerializer)
      log.info("BIG CID: ${bigInt} value: ${bigIntLink.value(this)}")


      val n = 123
      val nLink:DagLink<Int> = n.dagLink(this)
      log.debug("getting cid for $n")
      val nCid = nLink.cid
      log.debug("cid is $nCid")
      require(nCid == cid123) {
        "nLink.cid:$nCid != $cid123"
      }
      require(nLink.value(this) == 123) {
        "nLink.value: ${nLink.value(this)} != 123"
      }

      require(dagLink<Int>(cid123).value(this) == 123) {
        "cid123.cid<Int>().value != 123"
      }
    }
  }

}

private val log = org.slf4j.LoggerFactory.getLogger(SerialTest2::class.java)
