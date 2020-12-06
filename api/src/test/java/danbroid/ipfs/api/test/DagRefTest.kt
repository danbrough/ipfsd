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
import org.junit.Test
import java.lang.reflect.ParameterizedType

@Serializable
@IpfsObject
data class UNI(var name: String, var year: Int, @IpfsLink var departments: Set<Department>) {


  @Serializable
  data class Address(var street: String, var suburb: String, var postcode: Int, var city: String)

  @Serializable
  @IpfsObject
  data class Department(var name: String)

  @Contextual
  @IpfsLink
  val address = Address("14 Somewhere Street", "Kelburn", 6231, "Wellington")


}

@IpfsObject
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
          if (clazz.isAnnotationPresent(IpfsObject::class.java)) {
            log.warn("IPFS OBJECT: $clazz ")
            isLink = true
          }

          return false
        }

      })
      .registerTypeAdapter(DagObjectRef::class.java, object : TypeAdapter<DagObjectRef<*>>() {
        override fun write(out: JsonWriter, value: DagObjectRef<*>) {
          out.beginArray()
          out.value(value.hash)
          out.value(value.type.name)
          out.endArray()
        }

        override fun read(input: JsonReader): DagObjectRef<*> {
          input.beginArray()
          val cid = input.nextString()
          val clazz = input.nextString()
          input.endArray()

          return DagObjectRef(ipfs, cid, Class.forName(clazz))
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
                out.value(hash)
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
    val cid = Json.decodeFromString<String>(json)
    ipfs {
      log.info("DAG ${dag.get(cid).get().bodyText}")
      val correct_cid = "bafyreidfuhxrvbplh6362xzrzuc7jventfu6prjjstobsf5ljbiwh7hgri"
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
      log.debug("something uni: ${something.uni(this)}")
    }

  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagRefTest::class.java)
