package danbroid.ipfs.api

import com.google.gson.JsonStreamParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import org.slf4j.LoggerFactory
import java.io.Reader
import kotlin.reflect.KClass


private class SeriesSerializer<T : Any>(val serializer: KSerializer<T>) : KSerializer<List<T>> {
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
  Json.decodeFromString(SeriesSerializer(serializer), this)

fun <T : Any> String.parseJsonList(type: KClass<T>): List<T> =
  Json.decodeFromString(SeriesSerializer(type.serializer()), this)

private object _log

val log = LoggerFactory.getLogger(_log::class.java)

inline fun <reified T : Any> String.jsonSequence(): List<T> = parseJsonList(T::class)

inline fun <reified T : Any> String.parseJson(): T = Json.decodeFromString(this)

inline fun <reified T : Any> IPFS.ApiResponse<T>.jsonSequence(block: (T) -> Unit): Unit =
  reader.readText().jsonSequence<T>().forEach(block)

inline fun <reified T : Any> IPFS.ApiResponse<T>.jsonSequence(): List<T> =
  reader.readText().jsonSequence()

inline fun <reified T : Any> IPFS.ApiResponse<T>.json(): T =
  reader.readText().parseJson()

inline suspend fun <reified T : Any> Request<T>.jsonSequence(block: (T) -> Unit) =
  invoke().jsonSequence(block)

fun Reader.jsonSequence(): Sequence<JsonElement> = sequence {
  JsonStreamParser(this@jsonSequence).forEach {
    val json = it.toString()
    log.debug("yielding $json")
    yield(Json.parseToJsonElement(json))
  }
}

fun Reader.flow(): Flow<JsonElement> = flow {
  JsonStreamParser(this@flow).forEach {
    val json = it.toString()
    log.debug("emitting: $json")
    emit(Json.parseToJsonElement(json))
  }
}
