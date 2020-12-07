package danbroid.ipfs.api.test

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import danbroid.ipfs.api.DagObjectRef
import danbroid.ipfs.api.IPFS
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Test
import java.lang.reflect.ParameterizedType

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IpfsLink

@Serializable
@IpfsLink
data class UNI(var name: String, var year: Int, @IpfsLink var departments: Set<Department>) {


  @Serializable
  data class Address(var street: String, var suburb: String, var postcode: Int, var city: String)

  @Serializable
  @IpfsLink
  data class Department(var name: String)

  @Contextual
  @IpfsLink
  val address = Address("14 Somewhere Street", "Kelburn", 6231, "Wellington")


}

@IpfsLink
class SomethingWithAUni(val name: String, val cid: String) {
  suspend fun uni(ipfs: IPFS) = cid.dag<UNI>(ipfs)
}

suspend inline fun <reified T> CharSequence.dag(ipfs: IPFS): T =
  DagObjectRef(ipfs, toString(), T::class.java).get()

class DagRefTest {

  val ipfs = IPFS()

  @Test
  fun test() {
    log.info("test()")
    val uni =
      UNI("Victoria University", 1860, setOf(UNI.Department("Maths"), UNI.Department("Music")))


    var isLink = true

    val gson = GsonBuilder()
      .setExclusionStrategies(object : ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes): Boolean {
          log.trace("shouldSkipField: ${f.name}")

          if (f.getAnnotation(IpfsLink::class.java) != null) {
            isLink = true
            log.warn("FIELD: name: ${f.name} type:${f.declaredType} class:${f.declaredClass}")
            val type = f.declaredType
            if (type is ParameterizedType) {
              log.info("rawType: rawType:${type.rawType} ownerType:${type.ownerType} typeArgs:${type.actualTypeArguments}")
            }
          }
          return false
        }

        override fun shouldSkipClass(clazz: Class<*>): Boolean {
          log.trace("shouldSkipClass: $clazz")
          if (clazz.isAnnotationPresent(IpfsLink::class.java)) {
            log.warn("IPFS OBJECT: $clazz ")
            isLink = true
          }

          return false
        }

      })
      .registerTypeAdapter(DagObjectRef::class.java, object : TypeAdapter<DagObjectRef<*>>() {
        override fun write(out: JsonWriter, value: DagObjectRef<*>) {
          out.beginObject()
          out.name("/")
          out.value(value.hash)
          out.endObject()
        }

        override fun read(input: JsonReader): DagObjectRef<*> {
          input.beginObject()
          input.nextName()
          val cid = input.nextString()
          input.endObject()

          return DagObjectRef(ipfs, cid, Any::class.java)
        }

      })
      .registerTypeAdapterFactory(object : TypeAdapterFactory {
        override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {

          //val isObject = type.rawType.isAnnotationPresent(IpfsObject::class.java)
          log.trace("create(): $type isDag: $isLink")
          if (!isLink) return null
          isLink = false
          val delegate = gson.getDelegateAdapter(this, type)
          return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T) {

              val json = delegate.toJson(value)
              isLink = false

              log.debug("json is ${json}")
              ipfs {
                val hash =
                  dag.put(pin = true).apply { addData(json.toByteArray()) }.get()
                    .valueOrThrow().cid.cid
                log.debug("hash: $hash ")
                out.beginObject().name("/").value(hash).endObject()
              }
            }

            override fun read(input: JsonReader): T {
              TODO("Not implemented")
            }
          }
        }
      })

    val json = gson.setPrettyPrinting().create().toJson(uni)
    log.info("JSON: ${json}")
    val cid = Json.parseToJsonElement(json).jsonObject["/"]!!.jsonPrimitive.content
    ipfs {
      log.info("DAG ${dag.get(cid).get().bodyText}")
      val correct_cid = "bafyreidmxvzkrjfeapyl6fr5u4l3prwj5rctnmmwketysoe6la5kbmvc2q"
      require(cid == correct_cid) {
        "CID should be $correct_cid not $cid"
      }

      val addressRef = DagObjectRef(
        ipfs,
        "bafyreiep447k4dbt65zq54lptsurxuxqusszkaptvc5dynr5hv7c3v2jyy",
        UNI.Address::class.java
      )

      addressRef.get().also { address ->
        require(address == uni.address) {
          "Invalid address: $address"
        }
      }


      val something = SomethingWithAUni("Something", correct_cid)
      val json = gson.setPrettyPrinting().create().toJson(something)
      log.debug("something json: $json")
    }

  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagRefTest::class.java)
