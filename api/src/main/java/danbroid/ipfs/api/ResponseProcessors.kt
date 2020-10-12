package danbroid.ipfs.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonStreamParser
import java.io.EOFException

class ResponseProcessors {
  companion object {
    @JvmStatic
    fun <T> jsonParser(jsonType: Class<T>): ResponseProcessor<T> = { input, handler ->
      input.getReader().use {
        val parser = JsonStreamParser(it)
        while (parser.hasNext()) {
          val result = GsonBuilder().create().fromJson(parser.next(), jsonType)
          handler.invoke(Result.success(result))
        }
      }
    }
  }
}