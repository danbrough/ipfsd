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

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IpfsLink

@InternalSerializationApi
inline fun <reified T : Any> T.toJson(): String = Json.encodeToString(T::class.serializer(), this)

typealias Serializable = kotlinx.serialization.Serializable
typealias Transient = kotlinx.serialization.Transient

class DagOptions<T : Any>(
  var api: IPFS,
  var serializer: KSerializer<T>,
  var json: Json = Json,
)

@Serializable(with = DagNodeSerializer::class)
class DagNode<T : Any> constructor(
  private var value: T? = null,
  private var cid: String? = null,
  @Transient
  private val options: DagOptions<T>
) {

  suspend fun cid(): String =
    cid ?: options.json.encodeToString(options.serializer, value!!).let {
      options.api.dag.put(it).json().Cid.path.also {
        cid = it
      }
    }

  suspend fun value(): T = value ?: options.api.dag.get<T>(cid!!).invoke().text.let {
    options.json.decodeFromString(options.serializer, it).also {
      value = it
    }
  }

  override fun equals(other: Any?) = other is DagNode<*> && other.cidBlocking() == cidBlocking()

  override fun hashCode() = cidBlocking().hashCode()

  fun cidBlocking() = cid ?: runBlocking(Dispatchers.IO) { this@DagNode.cid() }
  fun valueBlocking() = value ?: runBlocking(Dispatchers.IO) { this@DagNode.value() }


  override fun toString(): String = "DagNode<${cid ?: value}>"

}

class DagNodeSerializer<T : Any>(val serializer: KSerializer<T>) : KSerializer<DagNode<T>> {
  override val descriptor: SerialDescriptor = serializer.descriptor

  private val linkSerializer = Types.Link.serializer()

  override fun deserialize(decoder: Decoder): DagNode<T> =
    DagNode(
      null,
      decoder.decodeSerializableValue(linkSerializer).path,
      options = DagOptions(api = IPFS.getInstance(), serializer)
    )


  override fun serialize(encoder: Encoder, value: DagNode<T>) =
    encoder.encodeSerializableValue(linkSerializer, Types.Link(value.cidBlocking()))

}


inline suspend fun <reified T : Any> T.cid(
  api: IPFS,
  serializer: KSerializer<T> = serializer()
): String =
  cid(DagOptions(api, serializer))


suspend fun <T : Any> T?.cid(options: DagOptions<T>): String =
  if (this == null) CID_DAG_NULL else
    DagNode(this, options = options).cid()


inline fun <reified T : Any> T.dagNode(
  api: IPFS = IPFS.getInstance(),
  options: DagOptions<T> = DagOptions(
    api,
    serializer = serializer()
  )
): DagNode<T> = DagNode(this, options = options)

inline fun <reified T : Any> dagNode(
  cid: String,
  api: IPFS,
  serializer: KSerializer<T> = serializer()
): DagNode<T> = dagNode(cid, DagOptions(api = api, serializer = serializer))

inline fun <reified T : Any> dagNode(cid: String, options: DagOptions<T>): DagNode<T> =
  DagNode(null, cid, options)

//private val log = LoggerFactory.getLogger(DagNode::class.java)
