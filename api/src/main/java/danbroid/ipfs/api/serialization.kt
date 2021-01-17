package danbroid.ipfs.api

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


@Serializable(with = DagNodeSerializer::class)
class DagNode<T : Any> constructor(
  private val _value: T? = null,
  private val serializer: KSerializer<T>? = null,
  private val _cid: String? = null,
  private val json: Json = Json,
  private val api: IPFS = ipfs
) {

  private suspend fun _cid(t: T): String = json.encodeToString(serializer!!, t).let {
    api.dag.put(it).json().Cid.path
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

  val value: T by lazy {
    _cid?.let { c ->
      api.blocking {
        _value(c)
      }
    } ?: _value!!
  }

  suspend fun value(): T? = _cid?.let { c -> _value(c) } ?: _value

  suspend fun cid(): String = _cid ?: _value?.let { _cid(it) } ?: CID_NULL_DATA

  override fun toString(): String = "DagNode<${_cid ?: _value}>"

  override fun equals(other: Any?): Boolean = other is DagNode<*> && cid == other.cid

  override fun hashCode(): Int = cid.hashCode()
}

class DagNodeSerializer<T : Any>(val serializer: KSerializer<T>) : KSerializer<DagNode<T>> {
  override val descriptor: SerialDescriptor = serializer.descriptor

  private val linkSerializer = Types.Link.serializer()

  override fun deserialize(decoder: Decoder): DagNode<T> =
    DagNode(null, serializer, decoder.decodeSerializableValue(linkSerializer).path, Json)

  override fun serialize(encoder: Encoder, value: DagNode<T>) =
    encoder.encodeSerializableValue(linkSerializer, Types.Link(value.cid))
}

inline fun <reified T : Any> T?.toDag(json: Json = Json): DagNode<T> =
  DagNode(this, T::class.serializer())

inline fun <T : Any> T?.toDag(serializer: KSerializer<T>, json: Json = Json): DagNode<T> =
  DagNode(this, serializer, null, json)

inline fun <reified T : Any> String.cid(
  serializer: KSerializer<T> = serializer(),
  json: Json = Json
): DagNode<T> = DagNode(null, serializer, this, json)


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


