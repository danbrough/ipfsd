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


@Serializable(with = DagNodeSerializer::class)
private class DagNode<T : Any> constructor(
  private val _value: T? = null,
  private val serializer: KSerializer<T>? = null,
  private val _cid: String? = null,
  private val json: Json = Json
) {

  private suspend fun _cid(t: T): String = json.encodeToString(serializer!!, t).let {
    ipfs.dag.put(it).json().Cid.path
  }

  private suspend fun _value(cid: String) = ipfs.dag.get<T>(cid).invoke().text.let {
    json.decodeFromString(serializer!!, it)
  }

  val cid: String by lazy {
    _cid ?: _value?.let {
      ipfs.blocking {
        _cid(it)
      }
    } ?: CID_NULL_DATA
  }

  val value: T? by lazy {
    _cid?.let { c ->
      ipfs.blocking {
        _value(c)
      }
    } ?: _value
  }

  suspend fun value(): T? = _cid?.let { c ->
    _value(c)
  } ?: _value

  suspend fun cid(): String = _cid ?: _value?.let {
    _cid(it)
  } ?: CID_NULL_DATA


  override fun toString(): String = "DAG<${_cid ?: _value}>"
}

private class DagNodeSerializer<T : Any>(val serializer: KSerializer<T>) : KSerializer<DagNode<T>> {
  override val descriptor: SerialDescriptor = serializer.descriptor

  private val linkSerializer = Types.Link.serializer()

  override fun deserialize(decoder: Decoder): DagNode<T> {
    log.info("deserialize()")
    val link = decoder.decodeSerializableValue(linkSerializer)
    log.debug("link: $link")
    return DagNode(null, serializer, link.path, Json)
  }

  override fun serialize(encoder: Encoder, value: DagNode<T>) {
    encoder.encodeSerializableValue(linkSerializer, Types.Link(value.cid))
  }
}


private inline fun <reified T : Any> T?.toDag(json: Json = Json): DagNode<T> =
  DagNode(this, serializer(), null)

private inline fun <reified T : Any> String.cid(
  serializer: KSerializer<T> = serializer(),
  json: Json = Json
): DagNode<T> =
  DagNode(null, serializer, this, json)

private val format = Json


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
    val zoo = ZOO3("Happy Place", 1972)
    zoo.s = Something("Something", 123)
    log.debug("zoo: $zoo")
    val link = zoo.toDag()
    log.debug("link: $link")
    val json = format.encodeToString(link)
    log.debug("json: $json")
    val zoo2Link = format.decodeFromString<DagNode<ZOO3>>(json)
    log.info("zoo2Link: $zoo2Link")
    val zoo2 = zoo2Link.value
    log.debug("zoo2: $zoo2")
    require(zoo == zoo2) {
      "zoo != zoo2"
    }

    var n: Int? = null
    val nullLink = n.toDag()
    require(nullLink.cid == CID_NULL_DATA) {
      "nullLink.cid != CID_NULL_DATA"
    }
    val bigNumberString = "12344444444444444444444444444444444444444444444444445"
    val bigInt = bigNumberString.toBigInteger()
    val bigNumberCID = "bafyreihydhtymenpvt4mrwrt35jqmf64fua6ius65o72wjs7jnuu6sxlra"
    val cid123 = "bafyreihbb6wszf7ordq4vfd3ab65wxjygixfgqe3qqc2qwbbdnzy4zifj4"
    val bigIntDag = bigNumberCID.cid(BigIntSerializer)
    log.info("BIG CID: ${bigIntDag.cid} value: ${bigIntDag.value}")


    n = 123
    val nLink = n.toDag()
    require(nLink.cid == cid123) {
      "nLink.cid:${nLink.cid} != $cid123"
    }
    require(nLink.value == 123) {
      "nLink.value: ${nLink.value} != 123"
    }


    require(cid123.cid<Int>().value == 123) {
      "cid123.cid<Int>().value != 123"
    }

  }
}

private val log = org.slf4j.LoggerFactory.getLogger(SerialTest2::class.java)
