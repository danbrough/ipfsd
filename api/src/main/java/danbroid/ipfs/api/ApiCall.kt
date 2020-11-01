package danbroid.ipfs.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.InputStream
import java.io.Reader


/**
 * Receives the result of an API call
 */
typealias ResultHandler<T> = (suspend (Result<T>) -> Unit)

typealias  ResponseProcessor<T> = (response: ApiCall.ApiResponse<T>) -> Flow<ApiCall.ApiResponse<T>>


open class ApiCall<T>(
  val path: String,
  val responseProcessor: ResponseProcessor<T>
) {

  interface ApiResponse<T> {
    val isSuccessful: Boolean
    val responseCode: Int
    val responseMessage: String
    var value: T
    val bodyText: String?
    fun getStream(): InputStream
    fun getReader(): Reader
  }


  constructor(path: String, type: Class<T>) : this(path, ResponseProcessors.jsonParser(type))


/*
  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(ApiCall::class.java)
  }
*/

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

  fun exec(executor: CallExecutor): Flow<ApiResponse<T>> = executor.exec(this)

  suspend fun get(executor: CallExecutor): ApiResponse<T> = exec(executor).first()

}
