package danbroid.ipfs.api

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import danbroid.ipfs.api.utils.createGsonBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.Reader


private fun <T> parseDAG(api: IPFS, reader: Reader, type: Class<T>): T {
  val json = reader.readText()
  log.info("json: $json")
  return createDagGson(api).fromJson(json, type)
}


@Suppress("UNCHECKED_CAST")
suspend fun <T> IPFS.dag(cid: String, type: Class<in T>): ApiCall.ApiResponse<T> {
  return dag.get(cid).flow().map {
    if (it.isSuccessful) (it as ApiCall.ApiResponse<T>).value =
      parseDAG(this, it.getReader(), type) as T
    it as ApiCall.ApiResponse<T>
  }.first()
}


@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T> IPFS.dag(cid: String): ApiCall.ApiResponse<T> =
  dag(cid, T::class.java)


interface Dag

object DagSupport

private val log = org.slf4j.LoggerFactory.getLogger(DagSupport::class.java)

class DagTypeAdapterFactory(val api: IPFS, val dag: Any?) : TypeAdapterFactory {
  val dags = mutableMapOf<Any, String>()
  var isRoot = true

  override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
    //log.trace("type: $type dag:${Dag::class.java.isAssignableFrom(type.rawType)}")
    if (type.rawType.interfaces.contains(Dag::class.java)) {
      val delegate = gson.getDelegateAdapter(this, type)
      log.trace("type: $type is dag")

      return object : TypeAdapter<T>() {
        override fun write(out: JsonWriter, value: T) {
          if (value == null) {
            out.nullValue()
          } else if (dags.containsKey(value)) {
            out.value(dags[value])
          } else if (value == dag) {
            delegate.write(out, value)
          } else {
            val json = delegate.toJson(value)
            val cid =
              api.dag.put().apply { addData(json.toByteArray()) }.getBlocking().value.cid.cid
            dags[value] = cid
            //log.info("wrote $value as $cid")
            out.beginObject().name("/").value(cid).endObject()
          }
        }

        override fun read(input: JsonReader): T {
          val tok = input.peek()
          log.debug("read: tok: $tok isRoot: $isRoot")
          if (isRoot) return delegate.read(input).also {
            isRoot = false
          }


          input.beginObject()
          input.nextName().also {
            log.trace("name is $it")
          }

          val cid = input.nextString()
          input.endObject()
          log.trace("reading cid: $cid type: $type")
          return runBlocking {
            api.dag<T>(cid, type.rawType).valueOrThrow()
          }
        }
      }
    }
    return null
  }

}

fun createDagGson(api: IPFS, dag: Any? = null): Gson = createGsonBuilder()
  .registerTypeAdapterFactory(DagTypeAdapterFactory(api, dag)).create()

