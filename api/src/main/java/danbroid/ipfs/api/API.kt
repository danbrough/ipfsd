package danbroid.ipfs.api

import danbroid.ipfs.api.utils.addUrlArgs
import java.io.File

class API(val executor: CallExecutor) {

  interface CallExecutor {
    suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>)
  }

  @JvmOverloads
  fun ls(path: String, stream: Boolean = false) = ApiCall(
    executor,
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
      executor,
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

  fun id() = ApiCall(executor, "id", Types.ID::class.java)
  fun version() = ApiCall(executor, "version", Types.Version::class.java)


  class PubSub(private val api: API) {
    @JvmOverloads
    fun subscribe(topic: String, discover: Boolean = true) = ApiCall(
      api.executor,
      "pubsub/sub".addUrlArgs("arg" to topic, "discover" to discover),
      Types.PubSub.Message::class.java
    )

    fun publish(topic: String, data: String) = ApiCall<Boolean>(
      api.executor,
      "pubsub/pub".addUrlArgs("arg" to topic, "arg" to data)
    ) { reader, handler ->
      handler.invoke(true)
    }
  }

  @JvmField
  val pubSub = PubSub(this)

  class Stats(private val api: API) {
    fun bw() = ApiCall(api.executor, "stats/bw", Types.Stats.Bandwidth::class.java)
  }

  @JvmField
  val stats = Stats(this)


  class Name(private val api: API) {

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
        api.executor,
        "name/publish".addUrlArgs(
          "arg" to path,
          "resolve" to resolve,
          "lifetime" to lifetime,
          "key" to key
        ),
        Types.NameValue::class.java
      )
  }

  @JvmField
  val name = Name(this)

  class Key(private val api: API) {
    /**
     * List all local keypairs
     *
     * @param ipnsBase Encoding used for keys: Can either be a multibase encoded CID or a base58btc encoded multihash. Takes {b58mh|base36|k|base32|b...}. Default: base36. Required: no.
     */
    @JvmOverloads
    fun ls(
      ipnsBase: String? = null
    ) =
      ApiCall(api.executor, "key/list".addUrlArgs("ipns-base" to ipnsBase), Types.Keys::class.java)
  }

  @JvmField
  val key = Key(this)

  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(API::class.java)
  }
}





