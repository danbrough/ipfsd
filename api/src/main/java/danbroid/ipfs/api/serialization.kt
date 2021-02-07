package danbroid.ipfs.api

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.slf4j.LoggerFactory

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IpfsLink

@InternalSerializationApi
inline fun <reified T : Any> T.toJson(json: Json = Json): String =
  json.encodeToString(T::class.serializer(), this)


class DagLinkSerializer<T : Any>(val serializer: KSerializer<T>) : KSerializer<DagLink<T>> {

  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Link") {
    element<String>("/")
  }

  override fun deserialize(decoder: Decoder) =
    decoder.decodeStructure(descriptor) {
      var cid: String? = null
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> cid = decodeStringElement(descriptor, 0)
          CompositeDecoder.DECODE_DONE -> break
          else -> error("Unexpected index: $index")
        }
      }
      require(cid != null)
      DagLink(cid, serializer)
    }

  override fun serialize(encoder: Encoder, value: DagLink<T>) {
    encoder.encodeStructure(descriptor) {
      encodeStringElement(descriptor, 0, value.cid)
    }
  }
}

@Serializable(with = DagLinkSerializer::class)
data class DagLink<out T : Any>(
  @SerialName("/") val cid: String,
  private val serializer: KSerializer<T>
) {
  suspend fun value(api: IPFS): T = api.dag.get<T>(cid).invoke().text.let {
    api.json.decodeFromString(serializer, it)
  }
}

suspend inline fun <reified T : Any> T.dagCid(api: IPFS, pin: Boolean = false): String =
  api.dag.putObject(this, pin = pin).json().Cid.path

private val log = LoggerFactory.getLogger(DagLink::class.java)
inline suspend fun <reified T : Any> T.dagLink(api: IPFS, pin: Boolean = false): DagLink<T> {
  return DagLink(dagCid(api, pin), T::class.serializer())
}

inline fun <reified T : Any> dagLink(cid: String): DagLink<T> = DagLink(cid, serializer())

typealias Serializable = kotlinx.serialization.Serializable
typealias Transient = kotlinx.serialization.Transient

/*@Serializable(with = DagNodeSerializer::class)
class DagNode<T : Any> constructor(
  private var value: T? = null,
  private var cid: String? = null,
  @Transient private val serializer: KSerializer<T>,
  @Transient private val _api: IPFS? = null,
  @Transient private val json: Json = Json
) {

  suspend fun cid(api: IPFS = _api!!): String =
    cid ?: json.encodeToString(serializer, value!!).let {
      api.dag.put(it).json().Cid.path.also {
        cid = it
      }
    }

  suspend fun value(api: IPFS = _api!!): T =
    value ?: api.dag.get<T>(cid!!).invoke().text.let {
      json.decodeFromString(serializer, it).also {
        value = it
      }
    }


  fun cidBlocking(api: IPFS = _api!!) =
    cid ?: runBlocking(Dispatchers.IO) { this@DagNode.cid(api) }

  fun valueBlocking(api: IPFS = _api!!) =
    value ?: runBlocking(Dispatchers.IO) { this@DagNode.value(api) }


  override fun equals(other: Any?): Boolean {
    log.debug("equals()")
    log.debug("cid: ${cid ?: value}")

    return when {
      other == null -> cidBlocking() == CID_DAG_NULL
      other !is DagNode<*> -> false
      else -> other.cidBlocking() == cidBlocking()
    }
  }

  override fun hashCode() = cidBlocking().hashCode()

  override fun toString(): String = "DagNode<${cid}>"

}*/

//private val log = LoggerFactory.getLogger(Dag::class.java)

/*class DagNodeSerializer<T : Any>(val serializer: KSerializer<T>) : KSerializer<DagNode<T>> {
  override val descriptor: SerialDescriptor = serializer.descriptor

  private val linkSerializer = Types.Link.serializer()

  override fun deserialize(decoder: Decoder): DagNode<T> =
    DagNode(null, decoder.decodeSerializableValue(linkSerializer).path, serializer)

  override fun serialize(encoder: Encoder, value: DagNode<T>) {
    encoder.encodeSerializableValue(linkSerializer, Types.Link(value.cidBlocking()))
  }

}*/


/*inline suspend fun <reified T : Any> T.dagCid(
  api: IPFS
): String =
  DagNode(this, null, serializer(), api).cid()*/

/*@JvmName("cidNullable")
suspend inline fun <reified T : Any> T?.dagCid(api: IPFS): String =
  if (this == null) CID_DAG_NULL else
    DagNode(this, null, serializer(), api).cid()


inline fun <reified T : Any> T.dagNode(
  api: IPFS? = null,
  serializer: KSerializer<T> = serializer()
): DagNode<T> = DagNode(this, null, serializer, api)

inline fun <reified T : Any> dagNode(
  cid: String,
  api: IPFS? = null,
  serializer: KSerializer<T> = serializer()
): DagNode<T> =
  DagNode(null, cid, serializer, api)*/

//private val log = LoggerFactory.getLogger(DagNode::class.java)
