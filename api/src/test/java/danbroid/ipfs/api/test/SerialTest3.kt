package danbroid.ipfs.api.test

import danbroid.ipfs.api.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import org.junit.Test


@Serializable(with = DagNodeSerializer::class)
private class DagNode<T : Any> constructor(
  private val _value: T? = null,
  private val serializer: KSerializer<T>? = null,
  private val _cid: String? = null,
) {

  constructor(serializer: KSerializer<T>, cid: String) : this(null, serializer, cid)
  constructor(value: T) : this(value, null, null)

  val cid: String by lazy {
    _cid ?: _value?.let {
      ipfs.blocking {
        Json.encodeToString(serializer!!, it).let {
          dag.put(it).json().Cid.path
        }
      }
    } ?: CID_NULL_DATA
  }

  val value: T? by lazy {
    _cid?.let { c ->
      ipfs.blocking {
        dag.get<T>(c).invoke().text.let {
          Json.decodeFromString(serializer!!, it)
        }
      }
    } ?: _value
  }


  override fun toString(): String = "DAG<${_cid ?: _value}>"
}

private class DagNodeSerializer<T : Any>(val serializer: KSerializer<T>) : KSerializer<DagNode<T>> {
  override val descriptor: SerialDescriptor = serializer.descriptor

  private val linkSerializer = Types.Link.serializer()

  override fun deserialize(decoder: Decoder): DagNode<T> {
    log.info("deserialize()")
    val link = decoder.decodeSerializableValue(linkSerializer)
    log.debug("link: $link")
    return DagNode(serializer, link.path)
  }

  override fun serialize(encoder: Encoder, value: DagNode<T>) {
    encoder.encodeSerializableValue(linkSerializer, Types.Link(value.cid))
  }

}


private inline fun <reified T : Any> T.toDag(): DagNode<T> = DagNode(this, T::class.serializer())

private val format = Json {
  serializersModule = SerializersModule {

/*    polymorphic(DAG::class) {
      subclass(IDag::class)
      subclass(ODag::class)
    }*/
  }
}


@Serializable
private data class Something(var name: String = "", var age: Int)

@Serializable
private data class ZOO3(var name: String = "", var age: Int, var s: Something? = null)

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

  }
}

private val log = org.slf4j.LoggerFactory.getLogger(SerialTest2::class.java)
