package danbroid.ipfs.api.okhttp

import danbroid.ipfs.api.*
import danbroid.ipfs.api.Request
import danbroid.ipfs.api.utils.uriEncode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import okio.BufferedSink
import okio.source
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

open class OkHttpExecutor(
  var urlBase: String = IPFS_DEFAULT_API_URL,
  var builder: OkHttpClient.Builder =
    OkHttpClient.Builder().readTimeout(0, TimeUnit.SECONDS),
  override val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) : IPFS.Executor {


  init {
    log.info("HERE2")
    val env = System.getenv()

    env.get(ENV_IPFS_API)?.also {
      urlBase = it
    }

    env.get(ENV_IPFS_USERNAME)?.also {
      log.error("configuring basic authentication")
      val password = env.get(ENV_IPFS_PASSWORD)
      if (password == null) throw Exception("$ENV_IPFS_PASSWORD is not set in the environment")
      setCredentials(it, password)
    }

    if (!this::httpClient.isInitialized)
      buildClient()
  }

  private var authenticator: Authenticator? = null

  override fun setCredentials(username: String, password: String) {
    val creds = Credentials.basic(username, password)
    authenticator = Authenticator { route, response ->
      //log.warn("route: $route response: $response")
      response.request.newBuilder().addHeader("Authorization", creds).build()
    }
    buildClient()
  }

  private lateinit var httpClient: OkHttpClient

  private fun buildClient(): OkHttpClient = builder.let { builder ->
    authenticator?.also {
      log.debug("setting authenticator")
      builder.authenticator(it)
    }

    if (DEBUG_HTTP) {
      log.warn("adding network interceptor")
      builder.addNetworkInterceptor {
        val request = it.request()
        log.trace("request: ${request}")
        val body = request.body
        if (body is MultipartBody) {
          val sink = Buffer()
          body.writeTo(sink)
          val output = ByteArrayOutputStream()
          sink.writeTo(output)
          log.trace("body: ${output}")
        }
        it.proceed(request)
      }
    }

    httpClient = builder.build()
    httpClient
  }


  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(OkHttpExecutor::class.java)
    val MEDIA_TYPE_APPLICATION = "application/octet-stream".toMediaType()
    val MEDIA_TYPE_DIRECTORY = "application/x-directory".toMediaType()

    //true if DEBUG_OKHTTP=1 environmental variable is set
    var DEBUG_HTTP = System.getenv().get("DEBUG_OKHTTP") == "1"
  }


  class HttpResponse<T>(val response: Response) : IPFS.ApiResponse<T> {

    override val isSuccessful = response.isSuccessful
    override val errorMessage = "Code:${response.code}: ${response.message}"
    override val stream = body().byteStream()
    override val reader = body().charStream()

    private fun body(): ResponseBody =
      if (response.isSuccessful) response.body!! else throw Exception(errorMessage)

    override fun close() = response.close()
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  override suspend fun <T> invoke(request: Request<T>): IPFS.ApiResponse<T> =
    withContext(Dispatchers.IO) {
      createCall(request).execute().let {
        HttpResponse(it)
      }
    }

  override fun <T> invoke(request: Request<T>, callback: IPFS.Executor.Callback<T>) {
    createCall(request).enqueue(object : okhttp3.Callback {
      override fun onFailure(call: okhttp3.Call, e: IOException) {
        callback.onResponse(request, null, e)
      }

      override fun onResponse(call: okhttp3.Call, response: Response) {
        callback.onResponse(request, HttpResponse(response))
      }
    })
  }

  fun createCall(request: Request<*>): okhttp3.Call =
    httpClient.newCall(
      okhttp3.Request.Builder().url("$urlBase/${request.path}")
        .post(requestBody(request))
        .build()
    )

  protected fun <T> requestBody(request: Request<T>): RequestBody {
    // log.debug("requestBody() $request")
    if (request !is DirectoryRequest) return "".toRequestBody()
    return MultipartBody.Builder()
      .setType(MultipartBody.FORM).apply {
        //.addPart(toOkHttpPart(request.part))
        request.forEach {
          addParts(this, it)
        }
      }
      .build()
  }


  protected fun addParts(builder: MultipartBody.Builder, part: Part) {
    val okHttpPart =
      MultipartBody.Part.createFormData("file", part.name.uriEncode(), requestBody(part))
    builder.addPart(okHttpPart)

    part.forEach {
      addParts(builder, it)
    }

  }

  protected fun requestBody(part: Part): RequestBody {
    if (part.isDirectory) return "".toRequestBody(MEDIA_TYPE_DIRECTORY)
    if (part is DataPart) return part.data.toRequestBody(MEDIA_TYPE_APPLICATION)
    return object : RequestBody() {
      override fun contentLength() = part.length()
      override fun contentType() = MEDIA_TYPE_APPLICATION
      override fun writeTo(sink: BufferedSink) {
        part.getInput().source().use { source -> sink.writeAll(source) }
      }
    }
  }

}




