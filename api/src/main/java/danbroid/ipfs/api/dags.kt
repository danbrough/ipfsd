package danbroid.ipfs.api

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import danbroid.ipfs.api.utils.createGsonBuilder
import kotlinx.coroutines.Deferred
import kotlinx.serialization.Serializable
import java.io.Reader


private fun <T> parseDAG(api: IPFS, reader: Reader, type: Class<T>): T {
  val json = reader.readText()
  log.info("json: $json")
  return createDagGson(api).fromJson(json, type)
}


@Serializable
class DagObjectRef<T>(
  val hash: String,
  @kotlinx.serialization.Transient
  val ipfs: IPFS = IPFS()
) {

  @Suppress("UNCHECKED_CAST")
  inline fun <reified T> get(): Deferred<T> =
    ipfs {
      dag.get<T>(hash).get().valueOrThrow()
    }

  inline fun <reified T> getBlocking(): T =
    ipfs.blocking { dag.get(hash, T::class.java).get().valueOrThrow() }

  suspend inline fun <reified T> await(): T = get<T>().await()

}

@Serializable
class DagRef2<T>(val hash: String) {

  fun get(ipfs: IPFS):T = ipfs.dag.get<Any>(hash)

}

suspend inline fun <reified T> IPFS.dag(hash: String): T = dag.get<T>(hash).get().valueOrThrow()
//fun <T> IPFS.dag(hash: String): T = dag.get<T>(hash).get().valueOrThrow()


/*
@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T> IPFS.dag(cid: String): T =
  dag(cid, T::class.java).valueOrThrow()
*/


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
              api.blocking { dag.put().apply { addData(json.toByteArray()) }.get().value.cid.cid }
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
          return api.blocking {
            dag.get(cid, type.rawType as Class<T>).get().valueOrThrow()
          }
        }
      }
    }
    return null
  }

}

fun createDagGson(api: IPFS, dag: Any? = null): Gson = createGsonBuilder()
  .registerTypeAdapterFactory(DagTypeAdapterFactory(api, dag)).create()

