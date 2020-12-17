package danbroid.ipfs.api

import danbroid.ipfs.api.utils.SingletonHolder
import kotlinx.coroutines.*
import java.io.*

open class IPFS(val callContext: CallContext) : CoroutineScope by callContext.coroutineScope {

  interface ApiResponse<T> : Closeable {
    val isSuccessful: Boolean
    val errorMessage: String
    val stream: InputStream
    val reader: Reader
    val text: String
      get() = if (isSuccessful) reader.readText() else throw Exception(errorMessage)
  }

  companion object : SingletonHolder<IPFS, CallContext>(::IPFS)

  interface Executor {
    fun <T> invoke(request: Request<T>): ApiResponse<T>
  }

  class CallContext(
    val executor: Executor,
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
  ) : Executor {
    override fun <T> invoke(request: Request<T>): ApiResponse<T> = executor.invoke(request)

  }


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
      DirectoryRequest<Types.File>(
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


    /**
     * /api/v0/ls
     * List directory contents for Unix filesystem objects.
     *
     * @param path The path to the IPFS object(s) to list links from. Required: yes.
     * @param resolveType Resolve linked objects to find out their types. Default: true. Required: no.
     * @param resolveSize Resolve linked objects to find out their file size. Default: true. Required: no.
     * @param stream Enable experimental streaming of directory entries as they are traversed. Required: no.
     */

    @JvmOverloads
    fun ls(
      path: String,
      stream: Boolean = false,
      resolveType: Boolean? = null,
      resolveSize: Boolean? = null
    ) =
      Request<Types.Object.ObjectArray>(
        callContext, "ls", "arg" to path, "stream" to stream,
        "resolve-type" to resolveType, "size" to resolveSize
      )

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
      data: String? = null,
      format: String? = null,
      inputEnc: String? = null,
      pin: Boolean? = null,
      hashFunc: String? = null,
    ) = DirectoryRequest<Types.CID>(
      callContext,
      "dag/put",
      "format" to format,
      "input-enc" to inputEnc,
      "pin" to pin,
      "hash" to hashFunc
    ).also {
      if (data != null) it.add(data)
    }

    /**
     * api/v0/dag/get
     * Get a dag node from ipfs.
     **/

    fun <T> get(arg: String) = Request<T>(callContext, "dag/get", "arg" to arg)

  }

  @JvmField
  val dag = Dag(callContext)


  class Network(val callContext: CallContext) {
    fun id(peerID: String? = null, peerIDBase: String? = null) =
      Request<Types.ID>(
        callContext, "id",
        "arg" to peerID,
        "peerid-base" to peerIDBase
      )
  }

  @JvmField
  val network = Network(callContext)


  class PubSub(val callContext: CallContext) {

    /**
     * /api/v0/pubsub/sub
     * Subscribe to messages on a given topic.
     * @param arg name of topic to subscribe to. Required: yes.
     *
     */

    fun sub(arg: String) = Request<Types.Message>(
      callContext,
      "pubsub/sub", "arg" to arg
    )

    fun pub(topic: String, data: String) = Request<Any>(
      callContext,
      "pubsub/pub", "arg" to topic, "arg" to data
    )

  }

  @JvmField
  val pubsub = PubSub(callContext)


}


private object api


fun <T> IPFS.blocking(block: suspend IPFS.() -> T): T = runBlocking {
  block.invoke(this@blocking)
}