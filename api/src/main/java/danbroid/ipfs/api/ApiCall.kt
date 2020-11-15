package danbroid.ipfs.api

import com.google.gson.JsonElement
import danbroid.ipfs.api.utils.parseJson
import danbroid.ipfs.api.utils.toJson
import kotlinx.coroutines.flow.Flow
import java.io.Closeable
import java.io.InputStream
import java.io.Reader


/**
 * Receives the result of an API call
 */

typealias  ResponseProcessor<T> = (response: ApiCall.ApiResponse<T>) -> Flow<ApiCall.ApiResponse<T>>


open class ApiCall<T>(
  val path: String,
  val responseProcessor: ResponseProcessor<T>
) : PartContainer<T>(null) {

  interface ApiResponse<T> : Closeable {
    val isSuccessful: Boolean
    val responseCode: Int
    val responseMessage: String
    var value: T
    val bodyText: String?
    fun getStream(): InputStream
    fun getReader(): Reader
    fun copy(t: T?): ApiResponse<T>
    fun <T> parseJson(type: Class<T>) = bodyText!!.parseJson(type)
    fun toJson(): JsonElement = bodyText!!.toJson()
  }

  constructor(path: String, type: Class<T>) : this(path, ResponseProcessors.jsonParser(type))

  override fun toString() = "ApiCall<$path:${hashCode()}>"


}
