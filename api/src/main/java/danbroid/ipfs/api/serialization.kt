package danbroid.ipfs.api

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IpfsLink

@InternalSerializationApi
inline fun <reified T : Any> T.toJson(): String = Json.encodeToString(T::class.serializer(), this)

typealias Serializable = kotlinx.serialization.Serializable
typealias Transient = kotlinx.serialization.Transient

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


