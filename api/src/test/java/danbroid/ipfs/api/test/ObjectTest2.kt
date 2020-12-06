package danbroid.ipfs.api.test

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import danbroid.ipfs.api.IPFS
import org.junit.Test
import java.lang.reflect.ParameterizedType

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class IpfsLink

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IpfsObject

@IpfsObject
class ShoppingList(val name: String, val created: Long = System.currentTimeMillis()) {

  class Item(val name: String)

  @IpfsLink
  val item = Item("Banana")


}

class ObjectTest2 {
  val ipfs = IPFS()

  @Test
  fun test() {
    log.debug("test()")

    data class DagContext(
      val name: String? = null,
      val parent: DagContext? = null,
      var dagField: Boolean = false
    )

    var dagContext = DagContext()

    var dagField = false

    val gson = GsonBuilder()
      .addSerializationExclusionStrategy(object : ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes): Boolean {
          f.getAnnotation(IpfsLink::class.java) ?: return false
          log.warn("FIELD: skipField: ${f.name} type:${f.declaredType} class:${f.declaredClass}")

          dagContext = DagContext(f.name, dagContext, dagField = true)
          val type = f.declaredType
          if (type is ParameterizedType) {
            log.info("rawType: rawType:${type.rawType} ownerType:${type.ownerType} typeArgs:${type.actualTypeArguments}")
          }

          return false
        }

        override fun shouldSkipClass(clazz: Class<*>): Boolean {
/*          log.debug("skipClass: $clazz genericSuper:${clazz.genericSuperclass}")
          val isIterable = Iterable::class.java.isAssignableFrom(clazz)
          if (isIterable) {

          }*/
          return false
        }

      })
      .registerTypeAdapterFactory(object : TypeAdapterFactory {
        override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {

          val isDag = type.rawType.isAnnotationPresent(IpfsObject::class.java)
          if (!isDag && !dagContext.dagField) {
            return null
          }
          dagContext.dagField = false

          val isIterable = Iterable::class.java.isAssignableFrom(type.rawType)
          log.trace("type: $type dag:$isDag list:$isIterable")

          val delegate = gson.getDelegateAdapter(this, type)

          return object : TypeAdapter<T>() {

            override fun write(out: JsonWriter, value: T) {
              log.debug("write value: $value")
              val json = delegate.toJson(value)
              log.debug("finished writing $value as $json")
              out.nullValue()
            }

            override fun read(input: JsonReader): T {
              TODO("Not yet implemented")
            }

          }
        }
      }).create()

    val list = ShoppingList("Stuff")
    gson.toJson(list).also {
      log.debug("json: $it")
    }


  }

  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(ObjectTest2::class.java)
  }
}


