import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.CallExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import okio.source
import java.io.InputStream
import java.io.Reader
import java.util.concurrent.TimeUnit

open class OkHttpCallExecutor @JvmOverloads constructor(val urlBase: String = "http://localhost:5001/api/v0") :
  CallExecutor {

  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(OkHttpCallExecutor::class.java)
    val MEDIA_TYPE_APPLICATION = "application/octet-stream".toMediaType()
    val MEDIA_TYPE_DIRECTORY = "application/x-directory".toMediaType()
  }

  private val okHttpClientNoTimeout by lazy {
    OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()
  }

  private val httpClient = okHttpClientNoTimeout

  protected fun createRequestBody(call: ApiCall<*>): RequestBody =
    if (call.parts.isEmpty())
      "".toRequestBody()
    else
      MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .also { builder ->
          call.parts.forEach {
            builder.addPart(it)
          }
        }.build()


  private fun MultipartBody.Builder.addPart(part: ApiCall.Part): MultipartBody.Builder {
    addPart(part.toOkHttpPart())
    if (part.isDirectory) {
      part.getChildren().forEach {
        addPart(it)
      }
    }
    return this
  }

  private fun ApiCall.Part.toOkHttpPart(): MultipartBody.Part =
    MultipartBody.Part.createFormData("file", name, toRequestBody()).let {
      if (!isDirectory) {
        it.addHeaders("Abspath" to absPath!!)
      } else it
    }


  private fun ApiCall.Part.toRequestBody(): RequestBody =
    if (isDirectory) "".toRequestBody(MEDIA_TYPE_DIRECTORY) else
      object : RequestBody() {
        override fun contentLength() = length()
        override fun contentType() = MEDIA_TYPE_APPLICATION
        override fun writeTo(sink: BufferedSink) {
          read().source().use { source -> sink.writeAll(source) }
        }
      }


  protected fun MultipartBody.Part.addHeaders(vararg headers: Pair<String, String>): MultipartBody.Part =
    this.headers!!.newBuilder().apply {
      headers.forEach {
        add(it.first, it.second)
      }
    }.build().let {
      MultipartBody.Part.Companion.create(it, body)
    }


  protected fun createRequest(call: ApiCall<*>): Call =
    Request.Builder()
      .url("${urlBase}/${call.path}")
      .post(createRequestBody(call))
      .build().let {
        httpClient.newCall(it)
      }


  internal class HttpResponse<T>(val response: Response) : ApiCall.ApiResponse<T> {
    override val isSuccessful = response.isSuccessful
    override val responseCode = response.code
    override val responseMessage = response.message

    private constructor(response: Response, t: T?) : this(response) {
      this.t = t
    }

    override val bodyText: String?
      get() = response.body?.string()

    override fun getStream(): InputStream = response.body!!.byteStream()

    override fun getReader(): Reader = response.body!!.charStream()

    private var t: T? = null

    override fun copy(t: T?) = HttpResponse(response, t)

    override var value: T
      get() = if (isSuccessful) t!! else throw IllegalArgumentException("Response not successful: $responseCode:$responseMessage")
      set(value) {
        t = value
      }

    override fun toString() = "HttpResponse<${response.code}:${response.message}:${t ?: bodyText}>]"

    override fun close() {
      response.close()
    }
  }

  override fun <T> exec(call: ApiCall<T>): Flow<ApiCall.ApiResponse<T>> = flow {
    @Suppress("BlockingMethodInNonBlockingContext")
    createRequest(call).execute().also {
      if (!it.isSuccessful) {
        log.warn("REQUEST FAILED: ${it.code}:${it.message}")
        emit(HttpResponse<T>(it))
      } else emitAll(call.responseProcessor.invoke(HttpResponse(it)))
    }
  }.flowOn(Dispatchers.IO)


}

