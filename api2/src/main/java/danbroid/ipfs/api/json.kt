package danbroid.ipfs.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


class JsonResponse<T : Any>(val request: Request, val serializer: KSerializer<T>) : Call<List<T>> {
  override fun invoke(): List<T> = request.invoke {
    it.reader.readText().parseJsonList(serializer)
  }
}

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

fun <T : Any> Request.parseJson(serializer: KSerializer<T>): JsonResponse<T> =
  JsonResponse(this, serializer)

inline fun <reified T : Any> Request.parseJson(): JsonResponse<T> =
  parseJson(T::class.serializer())