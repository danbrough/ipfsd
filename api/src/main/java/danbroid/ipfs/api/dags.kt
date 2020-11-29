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
import java.io.Reader

fun <T> parseDAG(reader: Reader, type: Class<T>): T {
  return createGsonBuilder().create().fromJson(reader, type)
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T> API.dag(cid: String): ApiCall.ApiResponse<T> {
  return dag.get(cid).flow().map {
    if (it.isSuccessful) (it as ApiCall.ApiResponse<T>).value =
      parseDAG(it.getReader(), T::class.java)
    it as ApiCall.ApiResponse<T>
  }.first()
}


interface Dag

object DagSupport

private val log = org.slf4j.LoggerFactory.getLogger(DagSupport::class.java)

class DagTypeAdapterFactory(val api: API, val dag: Any) : TypeAdapterFactory {
  val dags = mutableMapOf<Any, String>()

  override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
    log.debug("type: $type dag:${Dag::class.java.isAssignableFrom(type.rawType)}")
    if (type.rawType.interfaces.contains(Dag::class.java)) {
      val delegate = gson.getDelegateAdapter(this, type)
      log.warn("type: $type is dag")
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
            log.info("wrote $value as $cid")
            out.value(cid)
          }
        }

        override fun read(`in`: JsonReader?): T {
          TODO("Not yet implemented")
        }
      }
    }
    return null
  }

}

fun createDagGson(api: API, dag: Any): Gson = createGsonBuilder()
  .registerTypeAdapterFactory(DagTypeAdapterFactory(api, dag)).create()

