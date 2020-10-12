package danbroid.ipfs.api.okhttp

import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfs.api.utils.uriEncode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.concurrent.TimeUnit

open class OkHttpCallExecutor @JvmOverloads constructor(
  port: Int = 5001,
  val urlBase: String = "http://localhost:$port/api/v0"
) : CallExecutor {

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

  private fun ResponseBody.toInputSource() = object : ApiCall.InputSource {
    override fun getStream() = this@toInputSource.byteStream()
    override fun getReader() = this@toInputSource.charStream()
  }

  override suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>?) {
    if (handler != null) call.resultHandler = handler
    runCatching {
      log.trace("exec() url: ${call.path}")
      withContext(Dispatchers.IO) {
        val httpCall = createRequest(call)
        httpCall.execute().use { response ->

          if (!response.isSuccessful) {
            call.resultHandler?.invoke(Result.failure(Exception("Request failed: ${response.message} code:${response.code}")))
            return@use
          }

          call.responseProcessor.invoke(response.body!!.toInputSource(), call.resultHandler!!)
        }
      }
    }.exceptionOrNull().also {
      when (it) {
        null -> {
          //no exception was thrown
        }
        is CancellationException -> {
          //log.trace("CancellationException: ${it.message}")
        }
        else -> {
          call.resultHandler?.invoke(Result.failure(it))
        }
      }

    }
  }


}

