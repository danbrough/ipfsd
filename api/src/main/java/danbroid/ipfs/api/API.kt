package danbroid.ipfs.api

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.sun.org.apache.xpath.internal.operations.Bool
import danbroid.ipfs.api.utils.Base58
import danbroid.ipfs.api.utils.addUrlArgs
import kotlinx.coroutines.flow.flowOf
import okio.ByteString.Companion.decodeBase64
import java.io.File

/**
 * API for calls to an IPFS node
 */
object API {
  private inline fun <reified T> apiCall(path: String, vararg args: Pair<String, Any?>) =
    ApiCall(path.addUrlArgs(*args), T::class.java)

  object Basic {
    data class Object(
      @SerializedName("Hash")
      val hash: String,
      @SerializedName("Links")
      val links: Array<Link>
    ) {

      data class Link(
        @SerializedName("Hash")
        val hash: String,
        @SerializedName("Name")
        val name: String,
        @SerializedName("Size")
        val size: Long,
        @SerializedName("Target")
        val target: String,
        @SerializedName("Type")
        val type: Int
      ) {
        val isDirectory = type == 1
        val isFile = type == 2
      }
    }

    data class LsResponse(
      @SerializedName("Objects")
      val objects: Array<Object>
    )


    @JvmOverloads
    @JvmStatic
    fun ls(path: String, stream: Boolean = false) =
      apiCall<LsResponse>("ls", "arg" to path, "stream" to stream)

    data class FileResponse(
      @SerializedName("Bytes")
      val bytes: Long,
      @SerializedName("Hash")
      val hash: String,
      @SerializedName("Name")
      val name: String,
      @SerializedName("Size")
      val size: Long
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
    @JvmStatic
    fun add(
      data: String? = null,
      file: File? = null,
      recurseDirectory: Boolean? = null,
      fileName: String? = null,
      wrapWithDirectory: Boolean? = null,
      chunker: String? = null,
      pin: Boolean = true,
      onlyHash: Boolean? = null,
      trickle: Boolean? = null,
      rawLeaves: Boolean? = null,
      inline: Boolean? = null,
      inlineLimit: Int? = null,
      fsCache: Boolean? = null,
      noCopy: Boolean? = null
    ) =
      apiCall<FileResponse>(
        "add",
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
          call.addData(data, fileName)
        else if (file != null) {
          if (file.isDirectory && recurseDirectory != true)
            throw IllegalArgumentException("${file.path} is a directory. You must set recurseDirectory = true")
          call.addFile(file)
        }
      }


    data class VersionResponse(
      @SerializedName("Commit")
      val commit: String,
      @SerializedName("Golang")
      val goLang: String,
      @SerializedName("Repo")
      val repo: String,
      @SerializedName("Version")
      val version: String,
      @SerializedName("System")
      val system: String
    )

    @JvmStatic
    fun version() = apiCall<VersionResponse>("version")
  }

  object Block {
    /**
     * /api/v0/block/get
     * Get a raw IPFS block.
     * @param cid The base58 multihash of an existing block to get.
     *
     * <h3>cURL Example</h3>
     * <pre>curl -X POST "http://127.0.0.1:5001/api/v0/block/get?arg=<key>"</pre>
     */

    fun get(cid: String, responseProcessor: ResponseProcessor<Any>): ApiCall<Any> =
      ApiCall(
        "block/get".addUrlArgs("arg" to cid), responseProcessor = responseProcessor
      )


    data class PutResponse(
      @SerializedName("Key") val key: String,
      @SerializedName("Size") val size: Int
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
      pin: Boolean? = null
    ) = apiCall<PutResponse>(
      "block/put",
      "format" to format,
      "mhtype" to mhType,
      "mhlen" to mhLen,
      "pin" to pin
    ).also {
      if (data != null) it.addData(data, fileName ?: "/dev/stdin")
    }

  }

  object Config {

    object Profile {

      data class ApplyResponse(
        @SerializedName("NewCfg")
        val newConfig: JsonObject,
        @SerializedName("OldCfg")
        val oldConfig: JsonObject
      )

      /**
       * @param profile The profile to apply to the config.
       * @param dryRun Print difference between the current config and the config that would be generated.
       */
      @JvmOverloads
      @JvmStatic
      fun apply(profile: String, dryRun: Boolean? = null) = apiCall<ApplyResponse>(
        "config/profile/apply",
        "arg" to profile,
        "dry-run" to dryRun
      )

    }

  }

  object Dag {
    data class CID(@SerializedName("/") val cid: String)

    data class PutResponse(@SerializedName("Cid") val cid: CID)

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

    @JvmStatic
    fun put(
      format: String? = null,
      inputEnc: String? = null,
      pin: Boolean? = null,
      hashFunc: String? = null
    ) = apiCall<PutResponse>(
      "dag/put",
      "format" to format,
      "input-enc" to inputEnc,
      "pin" to pin,
      "hash" to hashFunc
    )

    data class ResolveResponse(
      @SerializedName("Cid") val cid: CID,
      @SerializedName("RemPath") val remPath: String
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

    @JvmStatic
    fun resolve(path: String) = apiCall<ResolveResponse>("dag/resolve", "arg" to path)

    data class StatResponse(
      @SerializedName("NumBlocks") val numBlocks: Long,
      @SerializedName("Size") val size: Long
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

    @JvmStatic
    @JvmOverloads
    fun stat(cid: String, progress: Boolean? = null) =
      apiCall<StatResponse>("dag/stat", "arg" to cid, "progress" to progress)

  }


  object Files {

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

    @JvmStatic
    fun cp(source: String, dest: String) =
      ApiCall(
        "files/cp".addUrlArgs(
          "arg" to source,
          "arg" to dest
        ),
        ResponseProcessors.constantResult(true)
      )


    data class LsResponse(
      @SerializedName("Entries")
      val entries: Array<Entry>
    ) {
      data class Entry(
        @SerializedName("Hash") val hash: String,
        @SerializedName("Name") val name: String,
        @SerializedName("Size") val size: Long,
        @SerializedName("Type") val type: Int
      )
    }

    /**
     * api/v0/files/ls
     * List directories in the local mutable namespace.
     * cURL Example
     * curl -X POST "http://127.0.0.1:5001/api/v0/files/ls?arg=<path>&long=<value>&U=<value>"
     *
     *   Arguments
     *   @param path Path to show listing for. Defaults to '/'. Required: no.
     *   @param longListing Use long listing format. Required: no.
     *   @param directoryOrder Do not sort; list entries in directory order. Required: no.
     */
    @JvmStatic
    @JvmOverloads
    fun ls(path: String? = null, longListing: Boolean? = null, directoryOrder: Boolean? = null) =
      apiCall<LsResponse>("files/ls", "arg" to path, "long" to longListing, "U" to directoryOrder)


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

    @JvmStatic
    @JvmOverloads
    fun read(
      path: String? = null,
      offset: Long? = null,
      count: Long? = null
    ): ApiCall<ApiCall.InputSource> =
      ApiCall(
        "files/read".addUrlArgs(
          "arg" to path,
          "offset" to offset,
          "count" to count
        ),
        ResponseProcessors.identity()
      )


    data class StatResponse(
      @SerializedName("Blocks") val blocks: Int,
      @SerializedName("CumulativeSize") val cumulativeSize: Long,
      @SerializedName("Hash") val hash: String,
      @SerializedName("Local") val local: Boolean,
      @SerializedName("Size") val size: Long,
      @SerializedName("SizeLocal") val sizeLocal: Long,
      @SerializedName("Type") val type: String,
      @SerializedName("WithLocaltity") val withLocality: Boolean
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

    @JvmStatic
    @JvmOverloads
    fun stat(
      path: String,
      format: String? = null,
      hash: Boolean? = null,
      size: Boolean? = null,
      withLocal: Boolean? = null
    ): ApiCall<StatResponse> = apiCall(
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
    @JvmStatic
    fun write(
      path: String,
      offset: Long? = null,
      create: Boolean? = null,
      parents: Boolean? = null,
      truncate: Boolean? = null,
      count: Long? = null,
      rawLeaves: Boolean? = null,
      cidVersion: String? = null,
      hash: String? = null
    ) = ApiCall(
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
      ), ResponseProcessors.identity()
    )
  }


  object Key {

    data class GenResponse(
      @SerializedName("Id") val id: String,
      @SerializedName("Name") val name: String
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
    @JvmStatic
    fun gen(name: String, type: String? = null, size: Int? = null, ipnsBase: String? = null) =
      apiCall<GenResponse>(
        "key/gen".addUrlArgs(
          "arg" to name,
          "type" to type,
          "size" to size,
          "ipns-base" to ipnsBase
        )
      )


    data class Key(
      @SerializedName("Name")
      val name: String,
      @SerializedName("Id")
      val id: String
    )

    data class LsResponse(@SerializedName("Keys") val keys: Array<Key>)


    /**
     * List all local keypairs
     *
     * @param ipnsBase Encoding used for keys: Can either be a multibase encoded CID or a base58btc encoded multihash. Takes {b58mh|base36|k|base32|b...}. Default: base36. Required: no.
     */
    @JvmStatic
    @JvmOverloads
    fun ls(ipnsBase: String? = null, extraInfo: Boolean? = null) =
      apiCall<LsResponse>("key/list", "ipns-base" to ipnsBase, "l" to extraInfo)

  }

  object Name {

    data class PublishResponse(
      @SerializedName("Name")
      val name: String,
      @SerializedName("Value")
      val value: String
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
    @JvmStatic
    fun publish(
      path: String,
      resolve: Boolean = true,
      lifetime: String? = null,
      key: String? = null
    ) = apiCall<PublishResponse>(
      "name/publish",
      "arg" to path,
      "resolve" to resolve,
      "lifetime" to lifetime,
      "key" to key
    )

  }


  object Network {

    data class ID(
      @SerializedName("ID")
      val id: String,
      @SerializedName("AgentVersion")
      val agentVersion: String,
      @SerializedName("ProtocolVersion")
      val protocolVersion: String,
      @SerializedName("PublicKey")
      val publicKey: String,
      @SerializedName("Protocols")
      val protocols: Array<String>,
      @SerializedName("Addresses")
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
    @JvmStatic
    @JvmOverloads
    fun id(peerID: String? = null, peerIDBase: String? = null) = apiCall<ID>(
      "id",
      "arg" to peerID, "peerid-base" to peerIDBase
    )

  }


  object PubSub {

    data class Message(
      val from: String,
      val data: String,
      val seqno: String,
      val topicIDs: Array<String>
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
    @JvmStatic
    fun subscribe(topic: String, discover: Boolean? = true) = apiCall<Message>(
      "pubsub/sub", "arg" to topic, "discover" to discover
    )

    @JvmStatic
    fun publish(topic: String, data: String) = ApiCall(
      "pubsub/pub".addUrlArgs("arg" to topic, "arg" to data)
    ) {
      flowOf(true)
    }


  }

  object Repo {

    data class GcResponse(
      @SerializedName("Key")
      val key: GcError
    ) {
      data class GcError(
        @SerializedName("/")
        val cid: String
      )
    }

    /**
     * @param stream-error Stream errors
     * @param quiet Write minimal output

     */
    @JvmOverloads
    fun gc(streamErrors: Boolean? = null, quiet: Boolean? = null) =
      apiCall<GcResponse>("repo/gc", "stream-errors" to streamErrors, "quiet" to quiet)


    data class StatResponse(
      @SerializedName("NumObjects")
      val numObjects: Long,
      @SerializedName("RepoPath")
      val repoPath: String,
      @SerializedName("Version")
      val version: String,
      @SerializedName("RepoSize")
      val repoSize: Long,
      @SerializedName("StorageMax")
      val storageMax: Long
    )

    /**
     * Get stats for the currently used repo.
     * @param sizeOnly Only report RepoSize and StorageMax. Required: no.
     * @param human Print sizes in human readable format (e.g., 1K 234M 2G). Required: no.
     */
    @JvmOverloads
    fun stat(sizeOnly: Boolean? = null, human: Boolean? = null) = apiCall<StatResponse>(
      "repo/stat",
      "size-only" to sizeOnly, "human" to human
    )

    data class VerifyResponse(
      @SerializedName("Msg")
      val msg: String,
      @SerializedName("Progress")
      val progress: Int
    )

    /**
     * Verify all blocks in repo are not corrupted.
     */
    fun verify() = apiCall<VerifyResponse>("repo/verify")

    data class VersionResponse(@SerializedName("Version") val version: String)

    /**
     * Show the repo version.
     * curl -X POST "http://127.0.0.1:5001/api/v0/repo/version?quiet=<value>"
     * @param quiet Write minimal output. Required: no.
     **/

    fun version(quiet: Boolean? = null) = apiCall<VersionResponse>(
      "repo/version", "quiet" to quiet
    )
  }


  object Stats {

    data class BandwidthResponse(
      @SerializedName("TotalIn")
      val totalIn: Long,
      @SerializedName("TotalOut")
      val totalOut: Long,
      @SerializedName("RateIn")
      val rateIn: Double,
      @SerializedName("RateOut")
      val rateOut: Double
    )

    @JvmStatic
    fun bw() = apiCall<BandwidthResponse>("stats/bw")
  }

  object Swarm {

  }

//private val log = org.slf4j.LoggerFactory.getLogger(API::class.java)

}



