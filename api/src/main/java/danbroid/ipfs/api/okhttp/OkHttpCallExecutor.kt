package danbroid.ipfs.api.okhttp

import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfs.api.utils.uriEncode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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
          call.parts.forEach { part ->
            if (part is ApiCall.StringPart) {
              MultipartBody.Part.createFormData(
                "file", part.name, part.data.toRequestBody(
                  MEDIA_TYPE_APPLICATION
                )
              ).addHeaders("Abspath" to part.absPath).also {
                builder.addPart(it)
              }
            }
            if (part is ApiCall.FilePart) {
              addFile(builder, part.file)
            }
          }
        }.build()


  protected fun addFile(builder: MultipartBody.Builder, file: File, rootLength: Int? = null) {
    log.trace("addFile() $file isDir:${file.isDirectory} isFile:${file.isFile}")

    val fileName: String =
      rootLength?.let { file.absolutePath.substring(rootLength).uriEncode() } ?: file.name

    when {

      file.isFile -> {
        MultipartBody.Part.createFormData(
          "file", fileName, file.asRequestBody(MEDIA_TYPE_APPLICATION)
        ).addHeaders("Abspath" to file.absolutePath).also {
          builder.addPart(it)
        }
      }

      file.isDirectory -> {
        val rootDirLength = rootLength ?: file.parentFile.absolutePath.let {
          if (it == "/") 0 else it.length + 1
        }

        MultipartBody.Part.createFormData(
          "file", fileName, "".toRequestBody(MEDIA_TYPE_DIRECTORY)
        ).also {
          builder.addPart(it)
        }

        file.listFiles()?.forEach {
          addFile(builder, it, rootDirLength)
        }
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

