package danbroid.ipfs.api

import com.google.gson.JsonElement
import danbroid.ipfs.api.utils.parseJson
import danbroid.ipfs.api.utils.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.Reader


/**
 * Receives the result of an API call
 */

typealias  ResponseProcessor<T> = (response: ApiCall.ApiResponse<T>) -> Flow<ApiCall.ApiResponse<T>>


open class ApiCall<T>(
  val executor: CallExecutor,
  val path: String,
  val responseProcessor: ResponseProcessor<T>,
) : PartContainer<T>(null) {

  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(ApiCall::class.java)
  }

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
    fun valueOrThrow(): T =
      if (isSuccessful) value else throw IOException("Failure: $responseCode:$responseMessage")

  }

  override fun toString() = "ApiCall<$path:${hashCode()}>"
  fun flow() = executor.exec(this)

  suspend fun get(): ApiResponse<T> = flow().first()
  suspend fun collect(collector: suspend (ApiResponse<T>) -> Unit) = flow().collect(collector)

  interface JavaResultCallback<T> {
    fun onStart() = Unit
    fun onError(t: Throwable) = log.error(t.message, t)
    fun onResult(result: ApiResponse<T>)
  }

  fun exec(callback: JavaResultCallback<T>) {
    runBlocking(Dispatchers.IO) {
      flow().onStart {
        callback.onStart()
      }.catch {
        callback.onError(it)
      }.collect {
        callback.onResult(it)
      }
    }
  }

  fun get(callback: JavaResultCallback<T>) {
    runBlocking(Dispatchers.IO) {
      flow().onStart {
        callback.onStart()
      }.catch {
        callback.onError(it)
      }.collect {
        callback.onResult(it)
      }
    }
  }

  fun getBlocking(): ApiResponse<T> = runBlocking {
    get()
  }

}

