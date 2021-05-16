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

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IpfsLink

@InternalSerializationApi
inline fun <reified T : Any> T.toJson(json: Json = Json): String =
  json.encodeToString(T::class.serializer(), this)


class DagLinkSerializer<T : Any>(val serializer: KSerializer<T>) : KSerializer<DagLink<T>> {

  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("DagLink") {
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
  suspend fun value(api: IPFS): T = api.dag.get<T>(cid).invoke().text().let {
    api.json.decodeFromString(serializer, it)
  }
}

suspend inline fun <reified T : Any> T.dagCid(api: IPFS, pin: Boolean = false): String =
  api.dag.putObject(this, pin = pin).json().Cid.path


inline suspend fun <reified T : Any> T.dagLink(api: IPFS, pin: Boolean = false): DagLink<T> {
  return DagLink(dagCid(api, pin), T::class.serializer())
}

inline fun <reified T : Any> dagLink(cid: String): DagLink<T> = DagLink(cid, serializer())

typealias Serializable = kotlinx.serialization.Serializable
typealias Transient = kotlinx.serialization.Transient
