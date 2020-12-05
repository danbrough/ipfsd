package danbroid.ipfs.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonStreamParser
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.slf4j.LoggerFactory
import java.io.Reader

object ResponseProcessors {

  private val log = LoggerFactory.getLogger(ResponseProcessors::class.java)

  @JvmStatic
  fun <T> jsonParser(jsonType: Class<T>): ResponseProcessor<T> = { response ->
    flow {
      //  log.warn("parsing json: successful: ${response.isSuccessful}")
      response.getReader().use {
        val reader: Reader
        if (false) {
          reader = it
          val text = reader.readText()
          log.trace("PARSING JSON: $text")
        } else {
          reader = it
        }
        val parser = JsonStreamParser(reader)

        while (parser.hasNext()) {
          val t = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create()
            .fromJson(parser.next(), jsonType)
          // log.warn("PARSED: ${t}")
          emit(response.copy(t))
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
