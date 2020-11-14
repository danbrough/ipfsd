package danbroid.ipfs.api

import com.google.gson.JsonElement
import danbroid.ipfs.api.utils.parseJson
import danbroid.ipfs.api.utils.toJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.*


/**
 * Receives the result of an API call
 */

typealias  ResponseProcessor<T> = (response: ApiCall.ApiResponse<T>) -> Flow<ApiCall.ApiResponse<T>>


open class ApiCall<T>(
  val path: String,
  val responseProcessor: ResponseProcessor<T>
) {

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


/*
  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(ApiCall::class.java)
  }
*/


  open class Part(
    val name: String,
    val absPath: String? = null
  ) {

    val isDirectory: Boolean = absPath == null

    open fun length(): Long = throw Exception("Not implemented")

    open fun read(): InputStream = throw Exception("Not implemented")

    open fun addPart(part: Part): Unit = throw Exception("Not implemented")

    open fun getChildren(): List<Part> = throw Exception("Not implemented")

    companion object {
      fun stripbase(fileName: String, basePath: String? = null): String {
        val base = basePath ?: File(fileName).parent
        if (fileName.startsWith(base)) return fileName.substring(base.length).let {
          if (it.startsWith('/')) it.substring(1) else it
        }
        return fileName
      }

      const val DEFAULT_ABS_PATH = "/dev/stdin"


      internal class DataPart(
        val data: ByteArray,
        fileName: String,
        absPath: String = DEFAULT_ABS_PATH
      ) :
        Part(fileName, absPath) {
        override fun length() = data.size.toLong()
        override fun read() = ByteArrayInputStream(data)
      }


      internal class DirectoryPart(path: String) : Part(path) {
        private val parts = mutableListOf<Part>()
        override fun addPart(part: Part) {
          parts.add(part)
        }

        override fun getChildren() = parts
      }

      internal class FilePart(val file: File, val basePath: String? = null) :
        Part(
          stripbase(file.absolutePath, basePath),
          if (file.isDirectory) null else file.absolutePath
        ) {

        override fun length() = file.length()

        override fun read() = FileInputStream(file)

        override fun getChildren() = if (!isDirectory) throw Exception("Not a directory")
        else file.listFiles()?.map {
          FilePart(it, basePath ?: file.parent)
        } ?: emptyList()
      }

      fun data(data: ByteArray, fileName: String = "", absPath: String = DEFAULT_ABS_PATH): Part =
        DataPart(data, fileName, absPath)

      fun string(data: String, fileName: String = "", absPath: String = DEFAULT_ABS_PATH): Part =
        DataPart(data.toByteArray(), fileName, absPath)

      fun directory(path: String): Part = DirectoryPart(path)

      fun file(file: File): Part = FilePart(file)
    }
  }

/*

  abstract class Part(val isDirectory: Boolean)

  class StringPart(
    val data: String,
    val name: String? = null,
    val absPath: String = "/dev/stdin",
  ) : Part(false)

  class FilePart(val file: File) : Part(file.isDirectory)
*/

  internal val parts = mutableListOf<Part>()

  fun add(
    data: String,
    name: String = "",
    absPath: String = Part.DEFAULT_ABS_PATH
  ) = apply {
    parts.add(Part.string(data, name, absPath))
  }

  fun add(
    data: ByteArray,
    name: String = "",
    absPath: String = Part.DEFAULT_ABS_PATH
  ) = apply {
    parts.add(Part.data(data, name, absPath))
  }

  fun addDirectory(name: String) = apply {
    parts.add(Part.directory(name))
  }

  fun add(file: File) = apply {
    parts.add(Part.file(file))
  }

  override fun toString() = "ApiCall<$path:${hashCode()}>"

  fun exec(executor: CallExecutor): Flow<ApiResponse<T>> = executor.exec(this)

  suspend fun get(executor: CallExecutor): ApiResponse<T> = exec(executor).first()

}
