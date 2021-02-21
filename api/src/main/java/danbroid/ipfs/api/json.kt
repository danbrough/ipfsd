package danbroid.ipfs.api

import com.google.gson.JsonStreamParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

inline fun <reified T : Any> String.parseJson(json: Json = Json): T = json.decodeFromString(this)

inline suspend fun <reified T : Any> Request<T>.json(json: Json = Json): T = invoke().text().parseJson(json)


//inline fun <reified T : Any> JsonElement.parse(): T = Json.decodeFromJsonElement(this)


inline fun <reified T : Any> Request<T>.flow(): Flow<T> = flow<T> {
  val response = invoke()
  val parser = JsonStreamParser(response.reader)
  while (currentCoroutineContext().isActive && parser.hasNext()) {
    val next = parser.next()
    emit(next.toString().parseJson())
  }
}.flowOn(Dispatchers.IO)
