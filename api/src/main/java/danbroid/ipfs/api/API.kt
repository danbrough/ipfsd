package danbroid.ipfs.api

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import danbroid.ipfs.api.utils.Base58
import danbroid.ipfs.api.utils.addUrlArgs
import okio.ByteString.Companion.decodeBase64
import java.io.File

/**
 * API for calls to an IPFS node
 */
object API {
  private inline fun <reified T> apiCall(path: String, vararg args: Pair<String, Any?>) =
    ApiCall(path.addUrlArgs(*args), T::class.java)

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
    onlyHash: Boolean? = null
  ) =
    apiCall<FileResponse>(
      "add",
      "wrap-with-directory" to wrapWithDirectory,
      "pin" to pin,
      "only-hash" to onlyHash,
      "chunker" to chunker
    ).also { call ->
      if (data != null)
        call.addData(data, fileName)
      else if (file != null) {
        if (file.isDirectory && recurseDirectory != true)
          throw IllegalArgumentException("${file.path} is a directory. You must set recurseDirectory = true")
        call.addFile(file)
      }
    }


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

  fun id() = apiCall<ID>("id")

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

  fun version() = apiCall<VersionResponse>("version")

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
      fun apply(profile: String, dryRun: Boolean? = null) = apiCall<ApplyResponse>(
        "config/profile/apply",
        "arg" to profile,
        "dry-run" to dryRun
      )
    }

  }

  object Files {


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
    @JvmOverloads
    fun ls(path: String? = null, longListing: Boolean? = null, directoryOrder: Boolean? = null) =
      apiCall<LsResponse>("files/ls", "arg" to path, "long" to longListing, "U" to directoryOrder)
  }


  object Key {

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
    @JvmOverloads
    fun ls(ipnsBase: String? = null) = apiCall<LsResponse>("key/list", "ipns-base" to ipnsBase)
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
    fun subscribe(topic: String, discover: Boolean? = true) = apiCall<Message>(
      "pubsub/sub", "arg" to topic, "discover" to discover
    )


    fun publish(topic: String, data: String) = ApiCall<Boolean>(
      "pubsub/pub".addUrlArgs("arg" to topic, "arg" to data)
    ) { _, handler ->
      handler.invoke(true)
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

    /**
     * /api/v0/repo/fsck
     * Remove repo lockfiles.
     * cURL Example
     * curl -X POST "http://127.0.0.1:5001/api/v0/repo/fsck"
     */
    data class FsckResponse(@SerializedName("Message") val message: String)

    fun fsck() = apiCall<FsckResponse>("repo/fsck")
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

    fun bw() = apiCall<BandwidthResponse>("stats/bw")
  }


//private val log = org.slf4j.LoggerFactory.getLogger(API::class.java)

}





