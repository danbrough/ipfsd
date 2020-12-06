package danbroid.ipfs.api.test

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import danbroid.ipfs.api.DagObjectRef
import danbroid.ipfs.api.IPFS
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.junit.Test
import java.lang.reflect.ParameterizedType


class DagRefTest2 {

  val ipfs = IPFS()
  val correct_uni_cid = "bafyreidfuhxrvbplh6362xzrzuc7jventfu6prjjstobsf5ljbiwh7hgri"

  @InternalSerializationApi
  @Test
  fun test() {
    log.info("test()")
    val uni =
      UNI("Victoria University", 1860, setOf(UNI.Department("Maths"), UNI.Department("Music")))

    @InternalSerializationApi
    fun module(ipfs: IPFS) = SerializersModule {
      // contextual(Student::class, IPFSObjectSerializer(ipfs, Student::class))
      //contextual(School::class, IPFSObjectSerializer(ipfs, School::class))

    }

    @InternalSerializationApi
    fun format(ipfs: IPFS) = Json {
      serializersModule = module(ipfs)
    }

    format(ipfs).encodeToString(uni)


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
      require(cid == correct_uni_cid) {
        "CID should be $correct_uni_cid not $cid"
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


    }

  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagRefTest::class.java)
