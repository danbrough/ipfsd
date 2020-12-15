package danbroid.ipfs.api

import danbroid.ipfs.api.utils.SingletonHolder
import danbroid.ipfs.api.utils.addUrlArgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.*

class IPFS(callContext: CallContext) {

  interface ApiResponse<T> : Closeable {
    val isSuccessful: Boolean
    val errorMessage: String
    val stream: InputStream
    val reader: Reader
  }

  companion object : SingletonHolder<IPFS, CallContext>(::IPFS)

  interface Executor {
    operator fun <T> invoke(request: Request<T>): ApiResponse<T>

  }

  class CallContext(
    val executor: Executor,
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
  )


  class Basic(val callContext: CallContext) {


    /**
     * Add data to ipfs.
     * @param data String data to add
     * @param pin Whether to pin the content
     * @param wrapWithDirectory Wrap files with a directory object
     * @param onlyHash Only chunk and hash - do not write to disk
     * @param chunker Chunking algorithm, size-<bytes>, rabin-<min>-<avg>-<max> or buzhash. Default: size-262144
     * @param trickle  Use trickle-dag format for dag generation. Required: no.
     * @param rawLeaves Use raw blocks for leaf nodes. (experimental). Required: no.
     * @param inline Inline small blocks into CIDs. (experimental). Required: no.
     * @param inlineLimit Maximum block size to inline. (experimental). Default: 32. Required: no.
     * @param fsCache: Check the filestore for pre-existing blocks. (experimental). Required: no.
     * @param noCopy Add the file using filestore. Implies raw-leaves. (experimental). Required: no.
     */
    @JvmOverloads
    fun add(
      data: String? = null,
      file: File? = null,
      recurseDirectory: Boolean? = null,
      fileName: String? = null,
      wrapWithDirectory: Boolean? = null,
      chunker: String? = null,
      pin: Boolean = true,
      progress: Boolean? = false,
      onlyHash: Boolean? = null,
      trickle: Boolean? = null,
      rawLeaves: Boolean? = null,
      inline: Boolean? = null,
      inlineLimit: Int? = null,
      fsCache: Boolean? = null,
      noCopy: Boolean? = null,
    ) =
      DirectoryRequest<Types.CID>(
        callContext,
        "add",
        "progress" to progress,
        "wrap-with-directory" to wrapWithDirectory,
        "pin" to pin,
        "only-hash" to onlyHash,
        "chunker" to chunker,
        "trickle" to trickle,
        "raw-leaves" to rawLeaves,
        "inline" to inline,
        "inline-limit" to inlineLimit,
        "fscache" to fsCache,
        "nocopy" to noCopy
      ).also { request ->
        if (data != null)
          request.add(data.toByteArray(), fileName ?: "")
        else if (file != null) {
          if (file.isDirectory && recurseDirectory != true)
            throw IllegalArgumentException("${file.path} is a directory. You must set recurseDirectory = true")
          request.add(file)
        }
      }

  }

  @JvmField
  val basic = Basic(callContext)


  class Dag(val callContext: CallContext) {

    /**
     * /api/v0/dag/put
     * Add a dag node to ipfs.
     *
     * @param format  Format that the object will be added as. Default: cbor. Required: no.
     * @param inputEnc Format that the input object will be. Default: json. Required: no.
     * @param pin Pin this object when adding. Required: no.
     * @param hashFunc Hash function to use. Default: . Required: no.
     *
     * <h3>cURL Example</h3>
     * <pre>curl -X POST -F file=@myfile http://127.0.0.1:5001/api/v0/dag/put?format=cbor&input-enc=json&pin=<value>&hash=<value></pre>
     */


    fun put(
      format: String? = null,
      inputEnc: String? = null,
      pin: Boolean? = null,
      hashFunc: String? = null,
      data: Any? = null,
      dataPath: String? = null
    ) = DirectoryRequest<Types.CID>(
      callContext,
      "dag/put",
      "format" to format,
      "input-enc" to inputEnc,
      "pin" to pin,
      "hash" to hashFunc
    )


    /**
     * api/v0/dag/get
     * Get a dag node from ipfs.
     **/

    fun get(arg: String) = Request<Any>(callContext, "dag/get", "arg" to arg)

  }

  @JvmField
  val dag = Dag(callContext)


  class Network(val callContext: CallContext) {
    fun id(peerID: String? = null, peerIDBase: String? = null) =
      Request<Types.ID>(
        callContext, "id",
        "arg" to peerID,
        "peerid-base" to peerIDBase
      ).parseJson<Types.ID>()
  }

  @JvmField
  val network = Network(callContext)


}

interface Call<T> {
  fun invoke(): T
}

open class Request<T>(
  val callContext: IPFS.CallContext,
  path: String,
  vararg args: Pair<String, Any?>
) :
  Call<IPFS.ApiResponse<T>> {
  val path = path.addUrlArgs(*args)

  override fun invoke() = callContext.executor(this)

  inline fun <U> invoke(block: (IPFS.ApiResponse<T>) -> U): U = invoke().use(block)


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


class Types {

  @Serializable
  data class ID(
    val ID: String,
    val AgentVersion: String,
    val ProtocolVersion: String,
    val PublicKey: String,
    val Protocols: Array<String>,
    val Addresses: Array<String>,
  )


  @Serializable
  data class File(
    val Name: String,
    val Hash: String,
    val Size: Long,
  )

  @Serializable
  data class Link(@SerialName("/") val link: String)

  @Serializable
  data class CID(val Cid: Link)
}

private object api


fun <T> IPFS.blocking(block: suspend IPFS.() -> T): T = runBlocking {
  block.invoke(this@blocking)
}