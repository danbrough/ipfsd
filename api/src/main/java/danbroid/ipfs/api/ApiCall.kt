package danbroid.ipfs.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonStreamParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.Reader


/**
 * Receives the result of an API call
 * @returns true to continue processing
 */
typealias ResultHandler<T> = (suspend (T?) -> Unit)

typealias  ResponseProcessor<T> = suspend (reader: Reader, handler: ResultHandler<T>) -> Unit

open class ApiCall<T>(
  val executor: API.CallExecutor,
  val path: String,
  val responseProcessor: ResponseProcessor<T>
) {

  constructor(executor: API.CallExecutor, path: String, type: Class<T>) : this(
    executor,
    path,
    jsonParser(type)
  )

  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(ApiCall::class.java)

    fun <T> jsonParser(jsonType: Class<T>): ResponseProcessor<T> = { reader, handler ->
      val parser = JsonStreamParser(reader)
      while (parser.hasNext()) {
        val json = GsonBuilder().create().fromJson(parser.next(), jsonType)
        handler.invoke(json)
      }
    }
  }


  interface Part

  class StringPart(
    val data: String,
    val name: String? = null,
    val absPath: String = "/dev/stdin",
  ) : Part

  class FilePart(val file: File) : Part

  internal val parts = mutableListOf<Part>()

  override fun toString() = "ApiCall<$path:${hashCode()}>"


  fun addData(data: String, fileName: String? = null, absPath: String = "/dev/stdin") = apply {
    parts.add(StringPart(data, fileName, absPath))
  }

  fun addFile(file: File) = apply {
    parts.add(FilePart(file))
  }

  var resultHandler: ResultHandler<T> = {
    log.info("result: $it")
    it != null
  }

  var errorHandler: (Throwable) -> Unit = {
    log.error(it.message, it)
  }

  fun onResult(handler: ResultHandler<T>) = apply {
    resultHandler = handler
  }

  fun onError(handler: (Throwable) -> Unit) = apply {
    errorHandler = handler
  }


  suspend fun exec(handler: ResultHandler<T>? = null) {
    if (handler != null) resultHandler = handler
    withContext(Dispatchers.IO) {
      executor.exec(this@ApiCall, resultHandler)
    }
  }

  interface FlowCallback<T> {
    @JvmDefault
    fun onStart() = Unit

    @JvmDefault
    fun onCompletion(thr: Throwable?) = Unit

    fun onResult(result: T?)
  }

  @ExperimentalCoroutinesApi
  fun exec(flowCallback: FlowCallback<T>) {
    runBlocking(Dispatchers.IO) {
      errorHandler = flowCallback::onCompletion
      asFlow().onStart { flowCallback.onStart() }
        .onCompletion { flowCallback.onCompletion(it) }
        .collect(flowCallback::onResult)
    }
  }

  fun asFlow(): Flow<T?> = flow {
    executor.exec(this@ApiCall, ::emit)
  }.flowOn(Dispatchers.IO)


}
