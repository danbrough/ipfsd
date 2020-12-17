package danbroid.ipfs.api

import danbroid.ipfs.api.utils.addUrlArgs
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

interface Call<T> {
  suspend fun invoke(): T
}

open class Request<T>(
  val callContext: IPFS.CallContext,
  path: String,
  vararg args: Pair<String, Any?>
) :
  Call<IPFS.ApiResponse<T>> {
  val path = path.addUrlArgs(*args)

  inline suspend fun <U> invoke(block: (IPFS.ApiResponse<T>) -> U): U = invoke().use(block)

  override suspend fun invoke(): IPFS.ApiResponse<T> = callContext.invoke(this)

}

interface PartList : Iterable<Part> {
  fun add(part: Part)
  fun add(file: File) = add(FilePart(file))

  fun addDirectory(name: String) = DirectoryPart(name).also {
    add(it)
  }

  fun add(data: String, name: String = "") = add(data.toByteArray(), name)
  fun add(data: ByteArray, name: String = "") = add(DataPart(data, name))
}

class DataPart(val data: ByteArray, name: String) : Part(name, false) {
  override fun length() = data.size.toLong()
  override fun getInput() = ByteArrayInputStream(data)
}

class FilePart(val file: File, name: String = file.name) : Part(name, file.isDirectory) {

  override fun iterator(): Iterator<Part> =
    file.listFiles()?.map {
      val fileName = if (name != "") name + "/" + it.name else it.name
      FilePart(it, fileName)
    }?.iterator() ?: emptyList<Part>().iterator()

  override fun length(): Long = file.length()
  override fun getInput() = FileInputStream(file)
}

class DirectoryPart(name: String = "") : Part(name, true)

open class Part(var name: String = "", val isDirectory: Boolean) : PartList {

  protected val parts = mutableListOf<Part>()

  override fun add(part: Part) {
    if (name != "") part.name = name + "/" + part.name
    parts.add(part)
  }

  override fun iterator(): Iterator<Part> = parts.iterator()

  open fun length(): Long = throw Exception("Not implemented")
  open fun getInput(): InputStream = throw Exception("Not implemented")
}

class DirectoryRequest<T : Any>(
  callContext: IPFS.CallContext,
  path: String,
  vararg args: Pair<String, Any?>
) : Request<T>(callContext, path, *args), PartList {

  private var root = Part("", true)

  override fun add(part: Part) {
    root.add(part)
  }

  override fun iterator() = root.iterator()
}
