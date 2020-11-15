package danbroid.ipfs.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

abstract class Part(var name: String)

//const private val DEFAULT_ABS_PATH = "/dev/stdin"

abstract internal class DataPart(
  name: String
) : Part(name) {
  abstract fun length(): Long
  abstract fun read(): InputStream
}


abstract class DirectoryPart<T>(name: String) : Part(name), Iterable<Part>

open class PartContainer<T>(call: ApiCall<T>?, name: String = "") : DirectoryPart<T>(name) {

  val call: ApiCall<T> = call ?: this as ApiCall<T>

  private var _parts = mutableListOf<Part>()

  fun addDirectory(path: String): PartContainer<T> {
    return PartContainer(call, if (name == "") path else name + '/' + path).also { addPart(it) }
  }

  fun add(file: File) = apply {
    addPart(if (file.isDirectory) FileDirectoryPart<T>(file) else FilePart(file))
  }

  fun addData(data: ByteArray, path: String = "") = apply {
    addPart(object : DataPart(if (name == "") path else name + '/' + path) {
      override fun length() = data.size.toLong()
      override fun read() = ByteArrayInputStream(data)
    })
  }

  fun addPart(part: Part) = apply {
    _parts.add(part)
  }

  fun exec(executor: CallExecutor): Flow<ApiCall.ApiResponse<T>> = executor.exec(call)

  suspend fun get(executor: CallExecutor): ApiCall.ApiResponse<T> = exec(executor).first()

  override fun iterator(): Iterator<Part> = _parts.iterator()
}

private fun removeBasePath(fileName: String, basePath: String): String {
  log.error("removeBasePath: $fileName : $basePath")
  if (fileName.startsWith(basePath)) return fileName.substring(basePath.length).let {
    if (it.startsWith('/')) it.substring(1) else it
  }
  return fileName
}

internal class FilePart(val file: File, val basePath: String = file.parent) :
  DataPart(removeBasePath(file.absolutePath, basePath)) {
  override fun length() = file.length()
  override fun read() = FileInputStream(file)
}

internal class FileDirectoryPart<T>(val file: File, val basePath: String = file.parent) :
  DirectoryPart<T>(removeBasePath(file.absolutePath, basePath)) {
  override fun iterator() = file.listFiles()?.asSequence()?.map {
    if (it.isFile) FilePart(it, basePath) else FileDirectoryPart<T>(it, basePath)
  }?.iterator() ?: emptyList<Part>().iterator()


}

private val log = org.slf4j.LoggerFactory.getLogger(Part::class.java)
