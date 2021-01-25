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
  var serializer: KSerializer<T>,
  var json: Json = Json,
  var api: IPFS = ipfs
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
      options = DagOptions(serializer)
    )

  override fun serialize(encoder: Encoder, value: DagNode<T>) =
    encoder.encodeSerializableValue(linkSerializer, Types.Link(value.cidBlocking()))

}


inline suspend fun <reified T : Any> T.cid(serializer: KSerializer<T>): String =
  cid(DagOptions(serializer))


inline suspend fun <reified T : Any> T?.cid(options: DagOptions<T> = DagOptions(serializer())): String =
  if (this == null) CID_DAG_NULL else
    DagNode(this, options = options).cid()


inline fun <reified T : Any> T.toDagNode(options: DagOptions<T> = DagOptions(serializer())): DagNode<T> =
  DagNode(this, options = options)

inline fun <reified T : Any> dagNode(
  cid: String,
  serializer: KSerializer<T> = serializer()
): DagNode<T> = dagNode(cid, DagOptions(serializer))

inline fun <reified T : Any> dagNode(cid: String, options: DagOptions<T>): DagNode<T> =
  DagNode(null, cid, options)

//private val log = LoggerFactory.getLogger(DagNode::class.java)

/*
interface DLink<T : Any> {
  val value: T
}

internal class ODLink<T : Any>(override val value: T) : DLink<T>

internal class IDLink<T : Any>(val type: KClass<T>, val cid: String) : DLink<T> {
  override val value: T
    get() = ipfs.blocking {
      dag.get<T>(cid).invoke().text.let {
        Json.decodeFromString(type.serializer(), it)
      }
    }
}

//@Serializable(with = DagLinkSerializer::class)
class DagLink<T : Any>(
  @Transient val type: KClass<T>,
  @SerialName("/")
  val cid: String
)

fun <T : Any> T.dagLink(): DLink<T> = ODLink(this)
*/


/*class DagLinkSerializer<T : Any>(private val dataSerializer: KSerializer<T>) :
  KSerializer<DagLink<T>> {

  val linkSerializer = Types.Link.serializer()
  override val descriptor: SerialDescriptor = dataSerializer.descriptor

  companion object {
    val log = LoggerFactory.getLogger(DagLinkSerializer::class.java)
  }

  override fun serialize(encoder: Encoder, value: DagLink<T>) {
    log.trace("serialize() $value")
*//*    val format = Json {
      serializersModule = encoder.serializersModule
    }
    // encoder.encodeSerializableValue(dataSerializer,value.value)
    val json = format.encodeToString(dataSerializer, value.value)
    log.trace("json: $json")
    ipfs.blocking {
      val cid = dag.put(json).json()
      log.trace("cid: $cid")
      linkSerializer.serialize(encoder, Types.Link(cid.Cid.path))
    }*//*

  }

  override fun deserialize(decoder: Decoder): DagLink<T> {
    val format = Json {
      serializersModule = decoder.serializersModule
    }
    val link = linkSerializer.deserialize(decoder)
    return ipfs.blocking {
      dag.get<T>(link.path).json()
    }
    // return DagLink(format.decodeFromString(dataSerializer, json))
    TODO("implement")
    //DagLink(dataSerializer.deserialize(decoder))
  }
}*/


