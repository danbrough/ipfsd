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
) : PartContainer(path) {

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


  fun flow(): Flow<ApiResponse<T>> = executor.exec(this)

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

open class ApiCall2(val path: String) : PartContainer() {

  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(ApiCall2::class.java)
  }

  interface ApiResponse2 : Closeable {
    val isSuccessful: Boolean
    val errorMessage: String
    val stream: InputStream
    val reader: Reader
  }

}

fun ApiCall2.ApiResponse2.success() =
  if (!isSuccessful) throw IOException(errorMessage) else this

fun <T> ApiCall2.ApiResponse2.success(then: ApiCall2.ApiResponse2.() -> T) =
  if (!isSuccessful) throw IOException(errorMessage) else then()