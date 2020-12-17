package danbroid.ipfs.api.okhttp

import danbroid.ipfs.api.*
import danbroid.ipfs.api.Request
import danbroid.ipfs.api.utils.uriEncode
import okhttp3.*
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import okio.BufferedSink
import okio.source
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class OkHttpExecutor(
  val urlBase: String = "http://localhost:5001/api/v0",
  val builder: OkHttpClient.Builder = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS)
) : IPFS.Executor {

  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(OkHttpExecutor::class.java)
    val MEDIA_TYPE_APPLICATION = "application/octet-stream".toMediaType()
    val MEDIA_TYPE_DIRECTORY = "application/x-directory".toMediaType()
    var DEBUG_HTTP = false
  }

  private val httpClient by lazy {
    builder
      .apply {
        if (DEBUG_HTTP)
          addNetworkInterceptor {
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
      .build()
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

  override fun <T> invoke(request: Request<T>): IPFS.ApiResponse<T> =
    createCall(request).execute().let {
      HttpResponse(it)
    }

  override fun <T> invoke(request: Request<T>, callback: IPFS.Executor.Callback<T>) {
    createCall(request).enqueue(object : Callback {
      override fun onFailure(call: Call, e: IOException) = callback.onError(request, e)

      override fun onResponse(call: Call, response: Response) =
        callback.onResponse(request, HttpResponse(response))
    })
  }

  fun createCall(request: Request<*>): Call =
    httpClient.newCall(
      okhttp3.Request.Builder().url("$urlBase/${request.path}")
        .post(requestBody(request))
        .build()
    )

  protected fun <T> requestBody(request: Request<T>): RequestBody {
    log.debug("requestBody() $request")
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




