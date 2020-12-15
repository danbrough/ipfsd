package danbroid.ipfs.api.okhttp


import danbroid.ipfs.api.*
import danbroid.ipfs.api.utils.uriEncode
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

open class OkHttpCallExecutor(val urlBase: String = "http://localhost:5001/api/v0") :
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

  protected fun createRequestBody(call: ApiCall<*>): RequestBody {
    val parts = call.iterator()
    return if (!parts.hasNext()) "".toRequestBody()
    else
      MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .also { builder ->
          parts.forEach {
            builder.addPart(it)
          }
        }.build()
  }

  protected fun createRequestBody(call: ApiCall2): RequestBody {
    val parts = call.iterator()
    return if (!parts.hasNext()) "".toRequestBody()
    else
      MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .also { builder ->
          parts.forEach {
            builder.addPart(it)
          }
        }.build()
  }

  private fun MultipartBody.Builder.addPart(part: Part): MultipartBody.Builder = apply {
    addPart(part.toOkHttpPart())
    if (part is DirectoryPart)
      part.forEach {
        addPart(it)
      }
  }

  private fun Part.toOkHttpPart(): MultipartBody.Part =
    MultipartBody.Part.createFormData("file", name.uriEncode(), toRequestBody())

  private fun Part.toRequestBody(): RequestBody =
    if (this is DirectoryPart) "".toRequestBody(MEDIA_TYPE_DIRECTORY) else let {
      this as DataPart
      object : RequestBody() {
        override fun contentLength() = length()
        override fun contentType() = MEDIA_TYPE_APPLICATION
        override fun writeTo(sink: BufferedSink) {
          read().source().use { source -> sink.writeAll(source) }
        }
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

  protected fun createRequest2(call: ApiCall2): Call =
    Request.Builder()
      .url("${urlBase}/${call.path}")
      .post(createRequestBody(call))
      .build().let {
        httpClient.newCall(it)
      }


  class HttpResponse<T>(val response: Response) : ApiCall.ApiResponse<T> {
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

  class HttpResponse2(val response: Response) : ApiCall2.ApiResponse2 {

    override val isSuccessful = response.isSuccessful

    override val errorMessage: String
      get() = "Code:${response.code}:${response.message}"

    override val stream: InputStream
      get() = response.body?.byteStream() ?: throw Exception(errorMessage)

    override val reader: Reader
      get() = response.body?.charStream() ?: throw Exception(errorMessage)

    override fun toString() =
      "HttpResponse2<${response.code}:${response.message}>]"

    override fun close() = response.close()

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

  override fun exec2(call: ApiCall2): ApiCall2.ApiResponse2 = createRequest2(call).execute().let {
    HttpResponse2(it)
  }


}

class JavaOkHttpCallExecutor : OkHttpCallExecutor()