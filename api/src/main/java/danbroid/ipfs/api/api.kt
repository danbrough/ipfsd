package danbroid.ipfs.api

import danbroid.ipfs.api.okhttp.OkHttpExecutor
import danbroid.ipfs.api.utils.SingletonHolder
import kotlinx.coroutines.*
import java.io.*

val ipfs: IPFS
  inline get() = IPFS.getInstance()

open class IPFS(val callContext: CallContext) : CoroutineScope by callContext.coroutineScope {

  interface ApiResponse<T> : Closeable {
    val isSuccessful: Boolean
    val errorMessage: String
    val stream: InputStream
    val reader: Reader
    val text: String
      get() = if (isSuccessful) reader.readText() else throw Exception(errorMessage)
  }

  companion object :
    SingletonHolder<IPFS, CallContext>({ IPFS(it ?: CallContext(OkHttpExecutor())) })

  interface Executor {
    suspend fun <T> invoke(request: Request<T>): ApiResponse<T>
    fun <T> invoke(request: Request<T>, callback: Callback<T>)
    interface Callback<T> {
      fun onResponse(request: Request<T>, response: ApiResponse<T>?, err: Exception? = null)
    }
  }

  class CallContext(
    val executor: Executor,
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
  ) : Executor by executor


  inner class Basic {

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


  inner class Dag {

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

    inline fun <reified T : Any> put(
      data: T, format: String? = null,
      inputEnc: String? = null,
      pin: Boolean? = null,
      hashFunc: String? = null
    ) = put(
      data = data.toJson(),
      format = format,
      inputEnc = inputEnc,
      pin = pin,
      hashFunc = hashFunc
    )

    //

    /**
     * api/v0/dag/get
     * Get a dag node from ipfs.
     **/

    fun <T> get(arg: String) = Request<T>(callContext, "dag/get", "arg" to arg)

  }


  inner class Network {
    fun id(peerID: String? = null, peerIDBase: String? = null) =
      Request<Types.ID>(
        callContext, "id",
        "arg" to peerID,
        "peerid-base" to peerIDBase
      )
  }


  inner class Object {
    /**
     *
     * /api/v0/object/get
     * Get and serialize the DAG node named by <key>.
     *   'ipfs object get' is a plumbing command for retrieving DAG nodes.
     *   It serializes the DAG node to the format specified by the "--encoding" flag.
     *   It outputs to stdout, and <key> is a base58 encoded multihash.
     *
     * The encoding of the object's data field can be specified by using the
     * --data-encoding flag
     * Supported values are:
     * "text" (default),"base64"

     * @param arg  Key of the object to retrieve, in base58-encoded multihash format. Required: yes.
     * @param dataEncoding  Encoding type of the data field, either "text" or "base64". Default: text. Required: no.
     */
    fun get(arg: String, dataEncoding: String? = null) =
      Request<Types.Object>(
        callContext,
        "object/get",
        "arg" to arg,
        "data-encoding" to dataEncoding
      )


    inner class Patch {
      /**
       * /api/v0/object/patch/add-link
       * Add a link to a given object.
       * @param objectHash IPFS object to add link to.
       * @param arg The hash of the node to modify4
       * @param linkName Name of link to create.
      //create [bool]: Create intermediary nodes. Required: no.
       */
      fun addLink(objectHash: String, linkHash: String, linkName: String) =
        Request<Types.Hash>(
          callContext,
          "object/patch/add-link",
          "arg" to objectHash,
          "arg" to linkHash,
          "arg" to linkName
        )

      /**
       * /api/v0/object/patch/set-data
       * Set the data field of an IPFS object.
       * @param hash The hash of the node to modify
       * @param data Data to set
       */
      fun setData(hash: String, data: ByteArray? = null) =
        DirectoryRequest<Types.Hash>(callContext, "object/patch/set-data", "arg" to hash).also {
          if (data != null) it.add(data)
        }

      /**
       * /api/v0/object/patch/set-data
       * Set the data field of an IPFS object.
       * @param hash The hash of the node to modify
       * @param data Data to set
       */
      fun setData(hash: String, data: String) = setData(hash, data.toByteArray())

    }

    @JvmField
    val patch = Patch()
  }


  inner class PubSub {

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
  val dag = Dag()

  @JvmField
  val obj = Object()

  @JvmField
  val pubsub = PubSub()

  @JvmField
  val basic = Basic()

  @JvmField
  val network = Network()
}

interface Dag

private object api


fun <T> IPFS.blocking(block: suspend IPFS.() -> T): T = runBlocking {
  block.invoke(this@blocking)
}

const val CID_EMPTY_OBJECT = "QmdfTbBqBPQ7VNxZEYEj14VmRuZBkqFbiwReogJgS1zR1n"
const val CID_NULL_DATA = "bafyreifqwkmiw256ojf2zws6tzjeonw6bpd5vza4i22ccpcq4hjv2ts7cm"