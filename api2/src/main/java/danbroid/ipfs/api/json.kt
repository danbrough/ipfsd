package danbroid.ipfs.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.slf4j.LoggerFactory


class JsonResponse<T>(val request: Request, val serializer: KSerializer<T>) : Call<T> {
  override fun invoke(): T = request.invoke().use {
    val json = it.reader.readText()
    log.trace("json: $json")
    Json.decodeFromString(serializer, json)
  }

  companion object {
    private val log = LoggerFactory.getLogger(JsonResponse::class.java)
  }
}


inline fun <reified T : Any> Request.parseJson(): JsonResponse<T> =
  JsonResponse(this, T::class.serializer())