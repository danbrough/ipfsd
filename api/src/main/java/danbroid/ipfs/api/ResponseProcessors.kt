package danbroid.ipfs.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonStreamParser

class ResponseProcessors {
  companion object {
    @JvmStatic
    fun <T> jsonParser(jsonType: Class<T>): ResponseProcessor<T> = { input, handler ->
      input.getReader().use {
        val parser = JsonStreamParser(it)
        while (parser.hasNext()) {
          val json = GsonBuilder().create().fromJson(parser.next(), jsonType)
          handler.invoke(json)
        }
      }

    }
  }
}