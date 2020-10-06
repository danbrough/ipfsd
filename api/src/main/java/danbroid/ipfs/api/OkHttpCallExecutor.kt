package danbroid.ipfs.api

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

open class OkHttpCallExecutor(
  port: Int = 5001,
  val urlBase: String = "http://localhost:$port/api/v0"
) :
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

  override suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>) {
    runCatching {
      log.trace("exec() url: ${call.path}")
      withContext(Dispatchers.IO) {
        val httpCall = createRequest(call)
        httpCall.execute().use { response ->
          if (!response.isSuccessful) {
            call.errorHandler.invoke(Exception("Request failed: ${response.message} code:${response.code}"))
            return@use
          }

          response.body!!.charStream().use { reader ->
            call.responseProcessor.invoke(reader, handler)
          }
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
        else -> call.errorHandler.invoke(it)
      }

    }
  }

}

