package danbroid.ipfs.api

import danbroid.ipfs.api.utils.addUrlArgs
import java.io.File

/**
 * API for calls to an IPFS node
 */
object API {
  private inline fun <reified T> apiCall(path: String, vararg args: Pair<String, Any?>) =
    ApiCall(path.addUrlArgs(*args), T::class.java)

  @JvmOverloads
  fun ls(path: String, stream: Boolean = false) =
    apiCall<Types.Objects>("ls", "arg" to path, "stream" to stream)

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
    apiCall<Types.File>(
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

  fun id() = apiCall<Types.ID>("id")

  fun version() = apiCall<Types.Version>("version")


  object PubSub {
    
    @JvmOverloads
    fun subscribe(topic: String, discover: Boolean? = true) = apiCall<Types.PubSub.Message>(
      "pubsub/sub", "arg" to topic, "discover" to discover
    )


    fun publish(topic: String, data: String) = ApiCall<Boolean>(
      "pubsub/pub".addUrlArgs("arg" to topic, "arg" to data)
    ) { _, handler ->
      handler.invoke(true)
    }
  }

  object Repo {
    /**
     * @param stream-error Stream errors
     * @param quiet Write minimal output

     */
    @JvmOverloads
    fun gc(streamErrors: Boolean? = null, quiet: Boolean? = null) =
      apiCall<Types.KeyError>("repo/gc", "stream-errors" to streamErrors, "quiet" to quiet)

  }


  object Stats {
    fun bw() = apiCall<Types.Stats.Bandwidth>("stats/bw")
  }


  object Name {

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
    ) = apiCall<Types.NameValue>(
      "name/publish",
      "arg" to path,
      "resolve" to resolve,
      "lifetime" to lifetime,
      "key" to key
    )
  }


  object Key {
    /**
     * List all local keypairs
     *
     * @param ipnsBase Encoding used for keys: Can either be a multibase encoded CID or a base58btc encoded multihash. Takes {b58mh|base36|k|base32|b...}. Default: base36. Required: no.
     */
    @JvmOverloads
    fun ls(ipnsBase: String? = null) = apiCall<Types.Keys>("key/list", "ipns-base" to ipnsBase)
  }


  object Config {
    object Profile {
      /**
       * @param profile The profile to apply to the config.
       * @param dryRun Print difference between the current config and the config that would be generated.
       */
      @JvmOverloads
      fun apply(profile: String, dryRun: Boolean? = null) = apiCall<Types.Config.ConfigChange>(
        "config/profile/apply",
        "arg" to profile,
        "dry-run" to dryRun
      )
    }
  }

  //private val log = org.slf4j.LoggerFactory.getLogger(API::class.java)

}





