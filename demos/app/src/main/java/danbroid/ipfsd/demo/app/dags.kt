package danbroid.ipfsd.demo.app

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.javaField

@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@MustBeDocumented
annotation class Dag


object DagSupport
private val log = org.slf4j.LoggerFactory.getLogger(DagSupport::class.java)

var cid = 1

class DagTypeAdapter<Any> : TypeAdapter<Any>() {
  override fun write(out: JsonWriter, value: Any) {
    out.value("CID:${cid++}")
  }

  override fun read(`in`: JsonReader?): Any {
    TODO("Not yet implemented")
  }
}

fun createGson(): GsonBuilder {
  return GsonBuilder()
    .registerTypeAdapterFactory(object : TypeAdapterFactory {
      override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        if (type.rawType.isAnnotationPresent(Dag::class.java)) {
          return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T) {
              out.value("CID:${cid++}")
            }

            override fun read(`in`: JsonReader?): T {
              TODO("Not yet implemented")
            }
          }
        }
        return null
      }
    })
}


fun walkDag2(o: Any, cids: MutableMap<Any, String> = mutableMapOf(), dag: Boolean = false) {


  val isDag = dag or o::class.hasAnnotation<Dag>()
  log.info("walkDag() $o dag:$isDag")

  o::class.declaredMemberProperties.forEach { prop ->
    log.info("property: ${prop.name} DAG:${prop.hasAnnotation<Dag>()}")
    prop.getter.call(o).also {
      log.debug("${prop.name} = $it")
      val propIsDag = prop.javaField?.isAnnotationPresent(Dag::class.java) == true
      if (propIsDag) {
        log.warn("HAS DAG!: ${propIsDag}")
      }
    }
  }


}

fun walkDag(o: Any) {
  walkDag2(o)
  val gson = createGson().create()
  log.info("created gson")
  gson.toJson(o).also {
    log.info("JSON:$it")
  }

}