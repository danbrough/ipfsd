package danbroid.ipfs.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonStreamParser
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

object ResponseProcessors {

  @JvmStatic
  fun <T> jsonParser(jsonType: Class<T>): ResponseProcessor<T> = { response ->
    flow {
      response.getReader().use {
        val reader = it
        val parser = JsonStreamParser(reader)
        while (parser.hasNext()) {
          response.value = GsonBuilder().create().fromJson(parser.next(), jsonType)
          emit(response)
        }
      }
    }
  }

  @JvmStatic
  fun <T> constant(result: T): ResponseProcessor<T> = { response ->
    flowOf(response.also {
      it.value = result
    })
  }

  @JvmStatic
  fun isSuccessful(): ResponseProcessor<Boolean> = { response ->
    flowOf(response.also {
      it.value = response.isSuccessful
    })
  }

  @JvmStatic
  fun raw(): ResponseProcessor<Void> = {
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
