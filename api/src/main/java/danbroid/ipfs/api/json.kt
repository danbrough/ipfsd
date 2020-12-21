package danbroid.ipfs.api

import com.google.gson.JsonStreamParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

inline fun <reified T : Any> String.parseJson(): T = Json.decodeFromString(this)

inline fun <reified T : Any> IPFS.ApiResponse<T>.json(): T =
  reader.readText().parseJson()

inline fun <reified T : Any> JsonElement.parse(): T = Json.decodeFromJsonElement(this)

inline fun <reified T : Any> IPFS.ApiResponse<T>.flow() = flow<T> {
  val parser = JsonStreamParser(this@flow.reader)
  while (parser.hasNext()) {
    val next = parser.next()
    emit(next.toString().parseJson())
    if (!currentCoroutineContext().isActive) {
      return@flow
    }
  }

}.flowOn(Dispatchers.IO)



