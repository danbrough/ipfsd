package danbroid.ipfs.api

import com.google.gson.JsonStreamParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.yield
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import org.slf4j.LoggerFactory


private object _log

val log = LoggerFactory.getLogger(_log::class.java)

inline fun <reified T : Any> String.parseJson(): T = Json.decodeFromString(this)


inline fun <reified T : Any> IPFS.ApiResponse<T>.json(): T =
  reader.readText().parseJson()


inline fun <reified T : Any> JsonElement.parse(): T = Json.decodeFromJsonElement(this)

inline fun <reified T : Any> IPFS.ApiResponse<T>.flow() = flow<T> {
  JsonStreamParser(this@flow.reader).forEach {
    emit(it.toString().parseJson())
  }
}.flowOn(Dispatchers.IO)



