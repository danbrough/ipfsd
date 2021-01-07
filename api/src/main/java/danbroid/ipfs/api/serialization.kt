package danbroid.ipfs.api

import kotlinx.serialization.ExperimentalSerializationApi
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

@Serializable(with = DagLinkSerializer::class)
data class DagLink<T>(val value: T)


inline fun <reified T> T.dagLink() = DagLink(this)

class DagLinkSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<DagLink<T>> {

  val linkSerializer = Types.Link.serializer()
  override val descriptor: SerialDescriptor = dataSerializer.descriptor

  companion object {
    val log = LoggerFactory.getLogger(DagLinkSerializer::class.java)
  }

  override fun serialize(encoder: Encoder, value: DagLink<T>) {
    log.trace("serialize() $value")
    val format = Json {
      serializersModule = encoder.serializersModule
    }
    // encoder.encodeSerializableValue(dataSerializer,value.value)
    val json = format.encodeToString(dataSerializer, value.value)
    log.trace("json: $json")
    ipfs.blocking {
      val cid = dag.put(json).json()
      log.trace("cid: $cid")
      linkSerializer.serialize(encoder, Types.Link(cid.Cid.path))
    }

  }

  override fun deserialize(decoder: Decoder): DagLink<T> {
    val format = Json {
      serializersModule = decoder.serializersModule
    }
    val link = linkSerializer.deserialize(decoder)
    val json = ipfs.blocking {
      dag.get<Any>(link.path).invoke().text
    }
    return DagLink(format.decodeFromString(dataSerializer, json))
    //DagLink(dataSerializer.deserialize(decoder))
  }
}


