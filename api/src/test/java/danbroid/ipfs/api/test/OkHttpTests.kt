package danbroid.ipfs.api.test

import com.google.gson.GsonBuilder
import com.google.gson.JsonStreamParser
import danbroid.ipfs.api.API
import danbroid.ipfs.api.utils.addUrlArgs
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Test
import java.util.*

private const val api_port = 5001
private const val url_base = "http://localhost:$api_port/api/v0"
private val MEDIA_TYPE_APPLICATION = "application/octet-stream".toMediaType()

class OkHttpTests {
  @Test
  fun addString() {
    log.info("addString()")
    val client = OkHttpClient.Builder().build()
    val pin = true
    val wrap_with_directory = true
    val url = "$url_base/add".addUrlArgs(
      "hash" to "sha2-256",
      "pin" to pin,
      "wrap-with-directory" to wrap_with_directory
    )
    val message = "Hello from ${OkHttpTests::class.java} at ${Date()}\n"

    val requestBody = MultipartBody.Builder()
      .setType(MultipartBody.FORM)

//      .addFormDataPart("file", "message.txt", message.toRequestBody(MEDIA_TYPE_APPLICATION))
      .addPart(
        MultipartBody.Part.createFormData(
          "file",
          "message.txt",
          message.toRequestBody(MEDIA_TYPE_APPLICATION)
        ).let {
          val headers = it.headers!!.newBuilder()
            .add("Abspath", "/stuff/message.txt")
            .build()
          MultipartBody.Part.Companion.create(headers, it.body)
        }
      )

      /*.addFormDataPart("title", "Square Logo")
      .addFormDataPart("image", "logo-square.png",
        File("docs/images/logo-square.png").asRequestBody(MEDIA_TYPE_PNG))*/
      .build()

    val request = Request.Builder().url(url).post(requestBody).build()
    log.trace("loading url: $url")
    val response = client.newCall(request).execute()
    log.debug("response: $response contentType:${response.header("Content-type", null)}")
    val json = response.body?.string()
    log.trace("body: ${json}")
    val parser = JsonStreamParser(json)
    val gson = GsonBuilder().setPrettyPrinting().create()
    while (parser.hasNext()) {
      val e = parser.next()
      println("e: ${gson.toJson(e)}")
      val data = gson.fromJson(e, API.Files::class.java)
      log.debug("data: $data")
    }

  }
}

private val log = org.slf4j.LoggerFactory.getLogger(OkHttpTests::class.java)
