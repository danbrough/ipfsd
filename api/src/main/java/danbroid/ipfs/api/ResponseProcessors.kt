package danbroid.ipfs.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonStreamParser
import java.io.EOFException
import java.lang.IllegalArgumentException

object ResponseProcessors {

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

  @JvmStatic
  fun stringReader(): ResponseProcessor<String> = { input, handler ->
    input.getReader().readText().also {
      handler.invoke(Result.success(it))
    }
  }

  @JvmStatic
  fun <T> constantResult(result: T): ResponseProcessor<T> = { input, handler ->
    handler.invoke(Result.success(result))
  }

}

private inline fun Any.toResponseProcessor(): ResponseProcessor<*> =
  when {
    this::class.isData -> ResponseProcessors.jsonParser(this::class.java)
    else -> throw IllegalArgumentException("Cannot produce ResponseProcessor for $this")
  }



