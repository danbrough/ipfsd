package danbroid.ipfs.api

import danbroid.ipfs.api.utils.addUrlArgs
import java.io.File

object API {


  @JvmOverloads
  fun ls(path: String, stream: Boolean = false) = ApiCall(
    "ls".addUrlArgs("arg" to path, "stream" to stream),
    Types.Objects::class.java
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
    ApiCall(
      "add".addUrlArgs(
        "wrap-with-directory" to wrapWithDirectory,
        "pin" to pin,
        "only-hash" to onlyHash,
        "chunker" to chunker
      ),
      Types.File::class.java
    ).also { call ->
      if (data != null)
        call.addData(data, fileName)
      else if (file != null) {
        if (file.isDirectory && recurseDirectory != true)
          throw IllegalArgumentException("${file.path} is a directory. You must set recurseDirectory = true")
        call.addFile(file)
      }
    }

  fun id() = ApiCall("id", Types.ID::class.java)
  fun version() = ApiCall("version", Types.Version::class.java)


  object PubSub {
    @JvmOverloads
    fun subscribe(topic: String, discover: Boolean = true) = ApiCall(
      "pubsub/sub".addUrlArgs("arg" to topic, "discover" to discover),
      Types.PubSub.Message::class.java
    )

    @JvmOverloads

    fun publish(topic: String, data: String) = ApiCall<Boolean>(
      "pubsub/pub".addUrlArgs("arg" to topic, "arg" to data)
    ) { _, handler ->
      handler.invoke(true)
    }
  }


  object Stats {
    fun bw() = ApiCall("stats/bw", Types.Stats.Bandwidth::class.java)
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
    ) =
      ApiCall(
        "name/publish".addUrlArgs(
          "arg" to path,
          "resolve" to resolve,
          "lifetime" to lifetime,
          "key" to key
        ),
        Types.NameValue::class.java
      )
  }


  object Key {
    /**
     * List all local keypairs
     *
     * @param ipnsBase Encoding used for keys: Can either be a multibase encoded CID or a base58btc encoded multihash. Takes {b58mh|base36|k|base32|b...}. Default: base36. Required: no.
     */
    @JvmOverloads
    fun ls(
      ipnsBase: String? = null
    ) =
      ApiCall("key/list".addUrlArgs("ipns-base" to ipnsBase), Types.Keys::class.java)
  }


  object Config {
    object Profile {
      /**
       * @param profile The profile to apply to the config.
       * @param dryRun Print difference between the current config and the config that would be generated.
       */
      @JvmOverloads
      fun apply(profile: String, dryRun: Boolean? = null) =
        ApiCall(
          "config/profile/apply".addUrlArgs("arg" to profile, "dry-run" to dryRun),
          Types.Config.ConfigChange::class.java
        )
    }
  }

  //private val log = org.slf4j.LoggerFactory.getLogger(API::class.java)

}





