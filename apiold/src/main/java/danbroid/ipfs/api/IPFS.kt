package danbroid.ipfs.api

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import danbroid.ipfs.api.okhttp.OkHttpCallExecutor
import danbroid.ipfs.api.utils.Base58
import danbroid.ipfs.api.utils.addUrlArgs
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import okio.ByteString.Companion.decodeBase64
import java.io.File


/*interface ResultHandler<T> {
  fun onResult(result: Result<T>)
}*/

interface CallExecutor {
  fun <T> exec(call: ApiCall<T>): Flow<ApiCall.ApiResponse<T>>

  fun exec2(call: ApiCall2): ApiCall2.ApiResponse2
}

inline fun <reified T> apiCall(
  executor: CallExecutor,
  path: String,
  vararg args: Pair<String, Any?>,
) = ApiCall(executor, path.addUrlArgs(*args), ResponseProcessors.jsonParser(T::class.java))


/**
 * API for calls to an IPFS node
 */
open class IPFS(val executor: CallExecutor) {
  constructor() : this(OkHttpCallExecutor())

  init {
    org.slf4j.LoggerFactory.getLogger("danbroid.ipfs.api").error("CREATING IPFS")
  }

  val coroutineScope = CoroutineScope(Dispatchers.IO)


  operator fun <T> invoke(block: suspend IPFS.() -> T): Deferred<T> = coroutineScope.async {
    block.invoke(this@IPFS)
  }

  fun <T> blocking(block: suspend IPFS.() -> T): T =
    runBlocking {
      block.invoke(this@IPFS)
    }


  class Basic(val api: IPFS) {
    data class Object(
      val hash: String,
      val links: Array<Link>,
    ) {

      data class Link(
        val hash: String,
        val name: String,
        val size: Long,
        val target: String,
        val type: Int,
      ) {
        val isDirectory = type == 1
        val isFile = type == 2
      }
    }

    data class LsResponse(
      val objects: Array<Object>,
    )


    @JvmOverloads
    fun ls(path: String, stream: Boolean = false) =
      apiCall<LsResponse>(api.executor, "ls", "arg" to path, "stream" to stream)

    data class FileResponse(
      val bytes: Long,
      val hash: String,
      val name: String,
      val size: Long,
    )

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
    ): ApiCall<FileResponse> =
      apiCall<FileResponse>(
        api.executor,
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
      ).also { call ->
        if (data != null)
          call.addData(data.toByteArray(), fileName ?: "")
        else if (file != null) {
          if (file.isDirectory && recurseDirectory != true)
            throw IllegalArgumentException("${file.path} is a directory. You must set recurseDirectory = true")
          call.add(file)
        }
      }


    data class VersionResponse(
      val commit: String,
      val goLang: String,
      val repo: String,
      val version: String,
      val system: String,
    )

    fun version() = apiCall<VersionResponse>(api.executor, "version")
  }

  @JvmField
  val basic = Basic(this)

  class Block(val api: IPFS) {
    /**
     * /api/v0/block/get
     * Get a raw IPFS block.
     * @param cid The base58 multihash of an existing block to get.
     *
     * <h3>cURL Example</h3>
     * <pre>curl -X POST "http://127.0.0.1:5001/api/v0/block/get?arg=<key>"</pre>
     */

    fun get(cid: String): ApiCall<Void> =
      ApiCall(
        api.executor,
        "block/get".addUrlArgs("arg" to cid), ResponseProcessors.raw()
      )


    data class PutResponse(
      val key: String,
      val size: Int,
    )

    /**
     *  /api/v0/block/put
     *  Store input as an IPFS block.
     *
     *  @param data Data to add
     *  @param format cid format for blocks to be created with. Required: no.
     *  @param mhLen multihash hash length. Default: -1. Required: no.
     *  @param mhType multihash hash function. Default: sha2-256. Required: no.
     *  @param pin pin added blocks recursively. Default: false. Required: no.
     *
     *  <h3>cURL Example</h3>
     *  curl -X POST -F file=@myfile "http://127.0.0.1:5001/api/v0/block/put?format=<value>&mhtype=sha2-256&mhlen=-1&pin=false"
     **/

    @JvmOverloads
    fun put(
      data: String? = null,
      fileName: String? = null,
      format: String? = null,
      mhType: String? = null,
      mhLen: Int? = null,
      pin: Boolean? = null,
    ) = apiCall<PutResponse>(
      api.executor,
      "block/put",
      "format" to format,
      "mhtype" to mhType,
      "mhlen" to mhLen,
      "pin" to pin
    ).also {
      if (data != null) it.addData(data.toByteArray(), fileName ?: "")
    }

  }

  @JvmField
  val block = Block(this)

  class Config(val api: IPFS) {

    class Profile(val api: IPFS) {

      data class ApplyResponse(
        val newCfg: JsonObject,
        val oldCfg: JsonObject,
      )

      /**
       * @param profile The profile to apply to the config.
       * @param dryRun Print difference between the current config and the config that would be generated.
       */
      @JvmOverloads
      fun apply(profile: String, dryRun: Boolean? = null) = apiCall<ApplyResponse>(
        api.executor,
        "config/profile/apply",
        "arg" to profile,
        "dry-run" to dryRun
      )
    }

    @JvmField
    val profile = Profile(api)

  }

  @JvmField
  val config = Config(this)

  class Dag(val api: IPFS) {

    data class CID(@SerializedName("/") val cid: String)

    /**
     * /api/v0/dag/get
     * Get a dag node from ipfs.
     *
     * @param arg The object to get Required: yes.
     */


    inline fun <reified T> get(arg: String) =
      apiCall<T>(
        api.executor,
        "dag/get".addUrlArgs("arg" to arg)
      )

    fun <T> get(arg: String, type: Class<T>) =
      ApiCall(
        api.executor,
        "dag/get".addUrlArgs("arg" to arg),
        responseProcessor = ResponseProcessors.jsonParser(type)
      )
    /*data class DagImportResponse(
      @SerializedName("Root") val root: Root,
      @SerializedName("PinErrorMsg") val pinErrorMsg: String?
    ) {
      data class Root(@SerializedName("Cid") val cid: CID)
    }*/


    data class PutResponse(val cid: CID)

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
    ) = apiCall<PutResponse>(
      api.executor,
      "dag/put",
      "format" to format,
      "input-enc" to inputEnc,
      "pin" to pin,
      "hash" to hashFunc
    ).also { call ->
      data?.also {
        val json = createDagGson(api, it).toJson(it)
        call.addData(json.toByteArray(), dataPath ?: "")
      }
    }

    data class ResolveResponse(
      val cid: CID,
      val remPath: String,
    )

    /**
     * /api/v0/dag/resolve
     * Resolve ipld block
     *
     * @param path The path to resolve Required: yes.
     *
     * <h3>cURL Example</h3>
     * <pre>curl -X POST http://127.0.0.1:5001/api/v0/dag/resolve?arg=<ref></pre>
     */


    fun resolve(path: String): ApiCall<ResolveResponse> =
      apiCall(api.executor, "dag/resolve", "arg" to path)

    data class StatResponse(
      val numBlocks: Long,
      val size: Long,
    )

    /**
     * /api/v0/dag/stat
     * Gets stats for a DAG
     * @param cid CID of a DAG root to get statistics for Required: yes.
     * @param progress Return progressive data while reading through the DAG. Default: true. Required: no.
     *
     * <h3>cURL Example</h3>
     * <pre>curl -X POST http://127.0.0.1:5001/api/v0/dag/stat?arg=<root>&progress=true</pre>
     **/


    @JvmOverloads
    fun stat(cid: String, progress: Boolean? = null): ApiCall<StatResponse> =
      apiCall(api.executor, "dag/stat", "arg" to cid, "progress" to progress)

  }

  @JvmField
  val dag = Dag(this)


  class Files(val api: IPFS) {

    /**
     *   ipfs files cp <source> <dest> - Copy any IPFS files and directories into MFS
     *   (or copy within MFS).
     *
     *   @param source Source IPFS or MFS path to copy. Required: yes.
     *   @param dest Destination within MFS. Required: yes.
     *
     * cURL Example
     * <pre>curl -X POST http://127.0.0.1:5001/api/v0/files/cp?arg=[source]&arg=[dest]</pre>
     */


    fun cp(source: String, dest: String) =
      ApiCall(
        api.executor,
        "files/cp".addUrlArgs(
          "arg" to source,
          "arg" to dest
        ),
        ResponseProcessors.raw()
      )


    data class LsResponse(
      val entries: Array<Entry>,
    ) {
      data class Entry(
        val hash: String,
        val name: String,
        val size: Long,
        val type: Int,
      )

      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LsResponse

        if (!entries.contentEquals(other.entries)) return false

        return true
      }

      override fun hashCode(): Int {
        return entries.contentHashCode()
      }
    }

    /**
     * api/v0/files/ls
     * List directories in the local mutable namespace.
     * cURL Example
     * curl -X POST "http://127.0.0.1:5001/api/v0/files/ls?arg=<path>&long=<value>&U=<value>"
     *
     *   Arguments
     *   @param path Path to show listing for.
     *   @param longListing Use long listing format. Required: no. Default true
     *   @param directoryOrder Do not sort; list entries in directory order. Required: no. Default true
     */

    @JvmOverloads
    fun ls(path: String, longListing: Boolean = true, directoryOrder: Boolean = true) =
      apiCall<LsResponse>(
        api.executor,
        "files/ls",
        "arg" to path,
        "long" to longListing,
        "U" to directoryOrder
      )


    /**
     * /api/v0/files/read
     * Read a file in a given MFS.
     *
     * @param path Path to file to be read. Required: yes.
     * @param offset  Byte offset to begin reading from. Required: no.
     * @param count  Maximum number of bytes to read. Required: no.
     *
     * Response
     * On success, the call to this endpoint will return with 200 and the following body:
     * This endpoint returns a `text/plain` response body.
     * cURL Example
     * `curl -X POST "http://127.0.0.1:5001/api/v0/files/read?arg=<path>&offset=<value>&count=<value>`
     **/


    @JvmOverloads
    fun read(
      path: String? = null,
      offset: Long? = null,
      count: Long? = null,
    ): ApiCall<Void> =
      ApiCall(
        api.executor,
        "files/read".addUrlArgs(
          "arg" to path,
          "offset" to offset,
          "count" to count
        ),
        ResponseProcessors.raw()
      )


    data class StatResponse(
      val blocks: Int,
      val cumulativeSize: Long,
      val hash: String,
      val local: Boolean,
      val size: Long,
      val sizeLocal: Long,
      val type: String,
      val withLocality: Boolean,
    )

    /**
     * /api/v0/files/stat
     * Display file status.
     *
     * @param path Path to node to stat. Required: yes.
     * @param format Print statistics in given format. Allowed tokens: <hash> <size> <cumulsize> <type> <childs>. Conflicts with other format options. Default: <hash> Size: <size> CumulativeSize: <cumulsize> ChildBlocks: <childs> Type: <type>. Default: <hash> Size: <size> CumulativeSize: <cumulsize> ChildBlocks: <childs> Type: <type>. Required: no.
     * @param hash Print only hash. Implies '--format=<hash>'. Conflicts with other format options. Required: no.
     * @param size Print only size. Implies '--format=<cumulsize>'. Conflicts with other format options. Required: no.
     * @param withLocal Compute the amount of the dag that is local, and if possible the total size. Required: no.
     *
     * cURL Example
     * ```curl -X POST "http://127.0.0.1:5001/api/v0/files/stat?arg=<path>&format=<hash> Size: <size> CumulativeSize: <cumulsize> ChildBlocks: <childs> Type: <type>&hash=<value>&size=<value>&with-local=<value>"```
     */


    @JvmOverloads
    fun stat(
      path: String,
      format: String? = null,
      hash: Boolean? = null,
      size: Boolean? = null,
      withLocal: Boolean? = null,
    ): ApiCall<StatResponse> = apiCall(
      api.executor,
      "files/stat",
      "arg" to path,
      "format" to format,
      "hash" to hash,
      "size" to size,
      "with-local" to withLocal
    )

    /**
     *  /api/v0/files/write
     *  Write to a mutable file in a given filesystem.
     *
     *  @param path Path to write to. Required: yes.
     *  @param offset Byte offset to begin writing at. Required: no.
     *  @param create Create the file if it does not exist. Required: no.
     *  @param parents Make parent directories as needed. Required: no.
     *  @param truncate Truncate the file to size zero before writing. Required: no.
     *  @param count Maximum number of bytes to read. Required: no.
     *  @param rawLeaves Use raw blocks for newly created leaf nodes. (experimental). Required: no.
     *  @param cidVersion Cid version to use. (experimental). Required: no.
     *  @param hash Hash function to use. Will set Cid version to 1 if used. (experimental). Required: no.
     *
     *
     * Argument data is of file type. This endpoint expects one or several files (depending on the command) in the body of the request as 'multipart/form-data'.
     * Response
     * On success, the call to this endpoint will return with 200 and the following body:
     * This endpoint returns a `text/plain` response body.
     * cURL Example
     * curl -X POST -F file=@myfile "http://127.0.0.1:5001/api/v0/files/write?arg=<path>&offset=<value>&create=<value>&parents=<value>&truncate=<value>&count=<value>&raw-leaves=<value>&cid-version=<value>&hash=<value>"
     */

    @JvmOverloads
    fun write(
      path: String,
      offset: Long? = null,
      create: Boolean? = null,
      parents: Boolean? = null,
      truncate: Boolean? = null,
      count: Long? = null,
      rawLeaves: Boolean? = null,
      cidVersion: String? = null,
      hash: String? = null,
    ) = ApiCall(
      api.executor,
      "files/write".addUrlArgs(
        "arg" to path,
        "offset" to offset,
        "create" to create,
        "parents" to parents,
        "truncate" to truncate,
        "count" to count,
        "raw-leaves" to rawLeaves,
        "cid-version" to cidVersion,
        "hash" to hash
      ), ResponseProcessors.raw()
    )
  }

  @JvmField
  val files = Files(this)


  class Key(val api: IPFS) {

    data class GenResponse(
      val id: String,
      val name: String,
    )

    /**
     * /api/v0/key/gen
     * Create a new keypair
     * @param name name of key to create Required: yes.
     * @param type type of the key to create: rsa, ed25519. Default: ed25519. Required: no.
     * @param size size of the key to generate. Required: no.
     * @param ipnsBase Encoding used for keys: Can either be a multibase encoded CID or a base58btc encoded multihash. Takes {b58mh|base36|k|base32|b...}. Default: base36. Required: no.
     *
     * On success, the call to this endpoint will return with 200 and the following body:
     * <pre>{
     * "Id": "<string>",
     * "Name": "<string>"
     * }
     *
     * cURL Example
     * curl -X POST "http://127.0.0.1:5001/api/v0/key/gen?arg=<name>&type=ed25519&size=<value>&ipns-base=base36"
     * </pre>
     */

    @JvmOverloads
    fun gen(
      name: String,
      type: String? = null,
      size: Int? = null,
      ipnsBase: String? = null,
    ): ApiCall<GenResponse> =
      apiCall(
        api.executor,
        "key/gen".addUrlArgs(
          "arg" to name,
          "type" to type,
          "size" to size,
          "ipns-base" to ipnsBase
        )
      )


    data class Key(

      val name: String,

      val id: String,
    )

    data class LsResponse(val keys: Array<Key>)


    /**
     * List all local keypairs
     *
     * @param ipnsBase Encoding used for keys: Can either be a multibase encoded CID or a base58btc encoded multihash. Takes {b58mh|base36|k|base32|b...}. Default: base36. Required: no.
     */

    @JvmOverloads
    fun ls(ipnsBase: String? = null, extraInfo: Boolean? = null): ApiCall<LsResponse> =
      apiCall(api.executor, "key/list", "ipns-base" to ipnsBase, "l" to extraInfo)

  }

  @JvmField
  val key = Key(this)

  class Name(val api: IPFS) {

    data class PublishResponse(
      val name: String,
      val value: String,
    )

    /**
     *  Publish IPNS names
     *
     * @cid The content id to publish
     * @param path  ipfs path of the object to be published.
     *
     * @param lifetime Time duration that the record will be valid for.
     * This accepts durations such as "300s", "1.5h" or "2h45m". Valid time units are
     * "ns", "us" (or "µs"), "ms", "s", "m", "h". Default: 24h. Required: no.
     *
     * @param key Name of the key to be used or a valid PeerID, as listed by 'ipfs key list -l'.
     * Default: self.
     */
    @JvmOverloads

    fun publish(
      path: String,
      resolve: Boolean = true,
      lifetime: String? = null,
      key: String? = null,
    ) = apiCall<PublishResponse>(
      api.executor,
      "name/publish",
      "arg" to path,
      "resolve" to resolve,
      "lifetime" to lifetime,
      "key" to key
    )

  }

  @JvmField
  val name = Name(this)


  class Network(val api: IPFS) {

    data class ID(
      @SerializedName("ID")
      val id: String,
      val agentVersion: String,
      val protocolVersion: String,
      val publicKey: String,
      val protocols: Array<String>,
      val addresses: Array<String>,
    )

    /**
     * Show ipfs node id info.
     * @param peerID Peer.ID of node to look up. Required: no.
     * @param format   Supports the format option for output with the following keys:
     * <id> : The peers id.
     * <aver>: Agent version.
     * <pver>: Protocol version.
     * <pubkey>: Public key.
     * <addrs>: Addresses (newline delimited).

     * @param peerIDBase Encoding used for peer IDs: Can either be a multibase encoded CID or a base58btc encoded multihash.
     * Takes {b58mh|base36|k|base32|b...}. Default: b58mh. Required: no.
     */

    @JvmOverloads
    fun id(peerID: String? = null, peerIDBase: String? = null) = api.executor.exec2(
      ApiCall2(
        "id".addUrlArgs(
          "arg" to peerID, "peerid-base" to peerIDBase,
        )
      )
    )

  }

  @JvmField
  val network = Network(this)

  class Object(val api: IPFS) {
    data class Link(
      val hash: String,
      val name: String,
      val size: Long
    )

    data class Object(
      val hash: String,
      val links: Array<Link>,
      val data: String
    )

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
      apiCall<Object>(api.executor, "object/get", "arg" to arg, "data-encoding" to dataEncoding)

    /**
     * /api/v0/object/put
     * Store input as a DAG object, print its key.
     *
     * pin [bool]: Pin this object when adding. Required: no.
     *
     */
    fun put(data: Any? = null, pin: Boolean = true) =
      apiCall<Object>(api.executor, "object/put", "pin" to pin).also {
        TODO("implement this")
      }

    class Patch(val api: IPFS) {
      /**
       * /api/v0/object/patch/add-link
       * Add a link to a given object.
      arg [string]: The hash of the node to modify. Required: yes.
      arg [string]: Name of link to create. Required: yes.
      arg [string]: IPFS object to add link to. Required: yes.
      create [bool]: Create intermediary nodes. Required: no.
      #
       */
      fun addLink(objectHash: String, linkHash: String, linkName: String) =
        apiCall<Object>(
          api.executor,
          "object/patch/add-link",
          "arg" to objectHash,
          "arg" to linkHash,
          "arg" to linkName
        )

      /**
       * /api/v0/object/patch/set-data
       * Set the data field of an IPFS object.
       * @param hash The hash of the node to modify
       * @param data Data to add
       */
      fun setData(hash: String, data: ByteArray? = null) =
        apiCall<Object>(api.executor, "object/patch/set-data", "arg" to hash).also {
          if (data != null) it.addData(data)
        }

      /**
       * /api/v0/object/patch/set-data
       * Set the data field of an IPFS object.
       * @param hash The hash of the node to modify
       * @param data Data to add
       */
      fun setData(hash: String, data: String) = setData(hash, data.toByteArray())

      /**
       * /api/v0/object/patch/set-data
       * Set the data field of an IPFS object.
       * @param hash The hash of the node to modify
       * @param data Data to add
       */
      fun setData(hash: String, data: File) = setData(hash).apply {
        add(data)
      }
    }

    @JvmField
    val patch = Patch(api)
  }

  @JvmField
  val `object` = Object(this)


  class PubSub(val api: IPFS) {

    data class Message(
      val from: String,
      val data: String,
      val seqno: String,
      val topicIDs: Array<String>,
    ) {
      val sequenceID: Long
        get() = seqno.decodeBase64()!!.asByteBuffer().long

      val dataString: String
        get() = data.decodeBase64()!!.string(Charsets.UTF_8)

      val fromID: String
        get() = Base58.encode(from.decodeBase64()!!.toByteArray())

      override fun toString() = "Message[from=$fromID,sequenceID:$sequenceID,$dataString]"
    }

    @JvmOverloads
    fun subscribe(topic: String, discover: Boolean? = true) = apiCall<Message>(
      api.executor,
      "pubsub/sub", "arg" to topic, "discover" to discover
    )


    fun publish(topic: String, data: String) = ApiCall(
      api.executor,
      "pubsub/pub".addUrlArgs("arg" to topic, "arg" to data), ResponseProcessors.raw()
    )

  }

  @JvmField
  val pubSub = PubSub(this)

  class Repo(val api: IPFS) {

    data class GcResponse(
      val key: GcError,
    ) {
      data class GcError(
        @SerializedName("/")
        val cid: String,
      )
    }

    /**
     * @param stream-error Stream errors
     * @param quiet Write minimal output

     */
    @JvmOverloads
    fun gc(streamErrors: Boolean? = null, quiet: Boolean? = null) =
      apiCall<GcResponse>(
        api.executor,
        "repo/gc",
        "stream-errors" to streamErrors,
        "quiet" to quiet
      )


    data class StatResponse(
      val numObjects: Long,
      val repoPath: String,
      val version: String,
      val repoSize: Long,
      val storageMax: Long,
    )

    /**
     * Get stats for the currently used repo.
     * @param sizeOnly Only report RepoSize and StorageMax. Required: no.
     * @param human Print sizes in human readable format (e.g., 1K 234M 2G). Required: no.
     */
    @JvmOverloads
    fun stat(sizeOnly: Boolean? = null, human: Boolean? = null) = apiCall<StatResponse>(
      api.executor,
      "repo/stat",
      "size-only" to sizeOnly, "human" to human
    )

    data class VerifyResponse(
      val msg: String,
      val progress: Int,
    )

    /**
     * Verify all blocks in repo are not corrupted.
     */
    fun verify() = apiCall<VerifyResponse>(api.executor, "repo/verify")

    data class VersionResponse(val version: String)

    /**
     * Show the repo version.
     * curl -X POST "http://127.0.0.1:5001/api/v0/repo/version?quiet=<value>"
     * @param quiet Write minimal output. Required: no.
     **/

    fun version(quiet: Boolean? = null) = apiCall<VersionResponse>(
      api.executor,
      "repo/version", "quiet" to quiet
    )
  }

  @JvmField
  val repo = Repo(this)

  class Stats(val api: IPFS) {

    data class BandwidthResponse(
      val totalIn: Long,
      val totalOut: Long,
      val rateIn: Double,
      val rateOut: Double,
    )

    fun bw() = apiCall<BandwidthResponse>(api.executor, "stats/bw")
  }

  @JvmField
  val stats = Stats(this)

  object Swarm {

  }

//private val log = org.slf4j.LoggerFactory.getLogger(API::class.java)

}


