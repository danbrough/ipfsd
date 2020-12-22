package danbroid.ipfs.api

import com.google.gson.JsonStreamParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

inline fun <reified T : Any> String.parseJson(): T = Json.decodeFromString(this)

inline suspend fun <reified T : Any> Request<T>.json(): T = invoke().text.parseJson()

//inline fun <reified T : Any> JsonElement.parse(): T = Json.decodeFromJsonElement(this)


inline fun <reified T : Any> Request<T>.flow(): Flow<T> = flow<T> {
  val response = invoke()
  val parser = JsonStreamParser(response.reader)
  while (currentCoroutineContext().isActive && parser.hasNext()) {
    val next = parser.next()
    emit(next.toString().parseJson())
  }
}.flowOn(Dispatchers.IO)