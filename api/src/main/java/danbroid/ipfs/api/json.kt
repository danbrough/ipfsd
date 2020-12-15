package danbroid.ipfs.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


private class ListSerializer<T>(val serializer: KSerializer<T>) : KSerializer<List<T>> {
  override val descriptor: SerialDescriptor
    get() = TODO("Not yet implemented")

  override fun deserialize(decoder: Decoder): List<T> {
    val a = mutableListOf<T>()
    runCatching {
      while (true) {
        a.add(serializer.deserialize(decoder))
      }
    }
    return a
  }

  override fun serialize(encoder: Encoder, value: List<T>) {
    TODO("Not yet implemented")
  }
}

fun <T : Any> String.parseJsonList(serializer: KSerializer<T>): List<T> =
  Json.decodeFromString(ListSerializer(serializer), this)

inline fun <reified T : Any> String.parseJsonList(): List<T> = parseJsonList(T::class.serializer())

fun <T : Any> String.parseJson(serializer: KSerializer<T>): T =
  Json.decodeFromString(serializer, this)

inline fun <reified T : Any> String.parseJson(): T = parseJson(T::class.serializer())


//inline fun <reified T : Any> IPFS.ApiResponse.json(block:(T)->Unit):

inline fun <reified T : Any> IPFS.ApiResponse<T>.json(block: (T) -> Unit): Unit =
  reader.readText().parseJsonList<T>().forEach(block)


inline fun <reified T : Any> IPFS.ApiResponse<T>.json(): List<T> =
  reader.readText().parseJsonList()

inline fun <reified T : Any> Request<T>.json(block: (T) -> Unit) =
  invoke().json(block)



