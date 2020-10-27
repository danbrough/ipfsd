package danbroid.ipfs.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonStreamParser
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

object ResponseProcessors {

  @JvmStatic
  fun <T> jsonParser(jsonType: Class<T>): ResponseProcessor<T> = { input ->
    flow {
      input.getReader().use {
        val reader = it
        val parser = JsonStreamParser(reader)
        while (parser.hasNext()) {
          val result = GsonBuilder().create().fromJson(parser.next(), jsonType)
          emit(result)
        }
      }
    }
  }

  @JvmStatic
  fun stringReader(): ResponseProcessor<String> = { input ->
    flowOf(input.getReader().readText())
  }

  @JvmStatic
  fun <T> constantResult(result: T): ResponseProcessor<T> = { input ->
    flowOf(result)
  }

  @JvmStatic
  fun identity(): ResponseProcessor<ApiCall.InputSource> = {
    flowOf(it)
  }

}

/*
private inline fun Any.toResponseProcessor() =
  when {
    this::class.isData -> ResponseProcessors.jsonParser(this::class.java)
    else -> throw IllegalArgumentException("Cannot produce ResponseProcessor for $this")
  }



*/
