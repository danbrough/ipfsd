package danbroid.ipfs.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.slf4j.LoggerFactory

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IpfsLink

@InternalSerializationApi
inline fun <reified T : Any> T.toJson(): String = Json.encodeToString(T::class.serializer(), this)

typealias Serializable = kotlinx.serialization.Serializable
typealias Transient = kotlinx.serialization.Transient

@Serializable(with = DagNodeSerializer::class)
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

}

private val log = LoggerFactory.getLogger(DagNode::class.java)

class DagNodeSerializer<T : Any>(val serializer: KSerializer<T>) : KSerializer<DagNode<T>> {
  override val descriptor: SerialDescriptor = serializer.descriptor

  private val linkSerializer = Types.Link.serializer()

  override fun deserialize(decoder: Decoder): DagNode<T> =
    DagNode(null, decoder.decodeSerializableValue(linkSerializer).path, serializer)

  override fun serialize(encoder: Encoder, value: DagNode<T>) {
    encoder.encodeSerializableValue(linkSerializer, Types.Link(value.cidBlocking()))
  }

}


inline suspend fun <reified T : Any> T.cid(
  api: IPFS
): String =
  DagNode(this, null, serializer(), api).cid()

@JvmName("cidNullable")
suspend inline fun <reified T : Any> T?.cid(api: IPFS): String =
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
  DagNode(null, cid, serializer, api)

//private val log = LoggerFactory.getLogger(DagNode::class.java)
