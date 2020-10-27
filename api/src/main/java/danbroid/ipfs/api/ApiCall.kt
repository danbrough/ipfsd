package danbroid.ipfs.api

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.InputStream
import java.io.Reader


/**
 * Receives the result of an API call
 */
typealias ResultHandler<T> = (suspend (Result<T>) -> Unit)

typealias  ResponseProcessor<T> = (input: ApiCall.InputSource) -> Flow<T>


open class ApiCall<T>(
  val path: String,
  val responseProcessor: ResponseProcessor<T>
) {

  interface InputSource {
    fun getStream(): InputStream
    fun getReader(): Reader
  }

  constructor(path: String, type: Class<T>) : this(path, ResponseProcessors.jsonParser(type))

  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(ApiCall::class.java)
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

  lateinit var resultHandler: ResultHandler<T>

  @Suppress("UNCHECKED_CAST")
  suspend fun get(executor: CallExecutor) = coroutineScope {
    executor.exec(this@ApiCall).first()
  }
}
