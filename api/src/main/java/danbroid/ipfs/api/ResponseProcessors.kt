package danbroid.ipfs.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonStreamParser
import java.io.StringReader
import java.lang.IllegalArgumentException

object ResponseProcessors {

  @JvmStatic
  fun <T> jsonParser(jsonType: Class<T>): ResponseProcessor<T> = { input, handler ->
    input.getReader().use {
      val text = it.readText()
      org.slf4j.LoggerFactory.getLogger("danbroid.ipfs.api").debug(text)
      val reader = StringReader(text)
      val parser = JsonStreamParser(reader)
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
  fun <T> constantResult(result: T): ResponseProcessor<T> = { _, handler ->
    handler.invoke(Result.success(result))
  }

}

private inline fun Any.toResponseProcessor() =
  when {
    this::class.isData -> ResponseProcessors.jsonParser(this::class.java)
    else -> throw IllegalArgumentException("Cannot produce ResponseProcessor for $this")
  }



