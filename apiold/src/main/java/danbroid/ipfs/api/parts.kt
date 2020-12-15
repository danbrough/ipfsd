package danbroid.ipfs.api

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


abstract class DirectoryPart(name: String) : Part(name), Iterable<Part>

open class PartContainer(name: String = "") : DirectoryPart(name) {


  private var _parts = mutableListOf<Part>()

  fun addDirectory(path: String): PartContainer {
    return PartContainer(if (name == "") path else name + '/' + path).also {
      addPart(it)
    }
  }

  fun add(file: File) = apply {
    addPart(if (file.isDirectory) FileDirectoryPart(file) else FilePart(file))
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

  override fun iterator(): Iterator<Part> = _parts.iterator()
}

private fun removeBasePath(fileName: String, basePath: String): String {
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

internal class FileDirectoryPart(val file: File, val basePath: String = file.parent) :
  DirectoryPart(removeBasePath(file.absolutePath, basePath)) {
  override fun iterator() = file.listFiles()?.asSequence()?.map {
    if (it.isFile) FilePart(it, basePath) else FileDirectoryPart(it, basePath)
  }?.iterator() ?: emptyList<Part>().iterator()


}

//private val log = org.slf4j.LoggerFactory.getLogger(Part::class.java)
