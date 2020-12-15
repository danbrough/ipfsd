package danbroid.ipfs.api.test

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import danbroid.ipfs.api.DagObjectRef
import danbroid.ipfs.api.IPFS
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModule
import org.junit.Test
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass


@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IpfsLink

@Serializable
@IpfsLink
class UNI(val name: String, val year: Int) {

  @Serializable
  data class Address(var street: String, var suburb: String, var postcode: Int, var city: String)

  @Serializable
  @IpfsLink
  data class Department(var name: String, var size: Int = 0)

  @Contextual
  @IpfsLink
  lateinit var address: Address

  @IpfsLink
  @Contextual
  @Serializable
  var departments: Set<Department> = emptySet()

  @Contextual
  lateinit var dept: Department

}

@IpfsLink
class SomethingWithAUni(ipfs: IPFS, val name: String, cid: String) {
  val uni = DagObjectRef<UNI>(cid, ipfs)
}

suspend inline fun <reified T> CharSequence.dag(ipfs: IPFS): T =
  DagObjectRef<T>(toString(), ipfs).await()

class DagRefTest {

  val uni =
    UNI("Victoria University", 1860).apply {
      departments = setOf(UNI.Department("Maths"), UNI.Department("Music"))
      address = UNI.Address("14 Somewhere Street", "Kelburn", 6231, "Wellington")
      dept = UNI.Department("Stuff", 1234)
    }

  class IPFSObjectSerializer<T>(
    val ipfs: IPFS,
    val type: KClass<*>,
    val serializer: KSerializer<T>
  ) :
    KSerializer<T> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("DagLink") {
      element<String>("/")
    }


    override fun deserialize(decoder: Decoder): T =
      decoder.decodeStructure(descriptor) {
        val hash = decodeStringElement(descriptor, 0)
        ipfs.blocking {
          val json = dag.get<Any>(hash).get().bodyText!!
          log.trace("json: $json")
          Json.decodeFromString(serializer, json)
        }
      }


    override fun serialize(encoder: Encoder, value: T) {
      log.debug("serialize() $value")
      val json = Json.encodeToString(serializer, value)
      log.trace("json: $json")
      ipfs.blocking {
        ipfs.dag.put().apply {
          addData(json.toByteArray())
        }.get().valueOrThrow().cid.cid
      }.also { hash ->
        log.trace("hash: $hash")
        encoder.encodeStructure(descriptor) {
          encodeStringElement(descriptor, 0, hash)
        }
      }
    }

  }

  @Test
  fun test() {
    log.info("test()")

    fun module(ipfs: IPFS) = SerializersModule {
      contextual(
        UNI.Department::class,
        IPFSObjectSerializer(ipfs, UNI.Department::class, UNI.Department.serializer())
      )
      contextual(
        UNI.Address::class,
        IPFSObjectSerializer(ipfs, UNI.Address::class, UNI.Address.serializer())
      )
      //contextual(School::class, IPFSObjectSerializer(ipfs, School::class))
    }

    fun format(ipfs: IPFS) = Json {
      serializersModule = module(ipfs)
    }

    val json = format(ipfs).encodeToString(uni)
    log.info("uni: $json")
  }

  @Test
  fun gsonTest() {
    log.info("gsonTest()")

    var isLink = true

    val dagRefAdapter = object : TypeAdapter<DagObjectRef<*>>() {
      override fun write(out: JsonWriter, value: DagObjectRef<*>) {
        out.beginObject().name("/").value(value.hash).endObject()
      }

      override fun read(input: JsonReader) = input.let {
        input.beginObject()
        input.nextName()
        val hash = input.nextString()
        input.endObject()
        DagObjectRef<Any>(hash, ipfs)
      }

    }


    val gson = GsonBuilder()
      .setExclusionStrategies(object : ExclusionStrategy {
        override fun shouldSkipField(f: FieldAttributes): Boolean {
          //log.trace("shouldSkipField: ${f.name}")

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
          // log.trace("shouldSkipClass: $clazz")
          if (clazz.isAnnotationPresent(IpfsLink::class.java)) {
            log.warn("IPFS OBJECT: $clazz ")
            isLink = true
          }

          return false
        }

      })
      .registerTypeAdapterFactory(object : TypeAdapterFactory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {

          //val isObject = type.rawType.isAnnotationPresent(IpfsObject::class.java)
          log.trace("create(): $type isDag: $isLink")
          if (type.rawType == DagObjectRef::class.java) return dagRefAdapter as TypeAdapter<T>
          if (!isLink) return null
          isLink = false
          val delegate = gson.getDelegateAdapter(this, type)
          return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T) {
              log.warn("TYPE: $value")
              val json = delegate.toJson(value)
              isLink = false

              log.debug("json is ${json}")

              val hash =
                ipfs.blocking { dag.put(pin = true).apply { addData(json.toByteArray()) }.get() }
                  .valueOrThrow().cid.cid
              log.debug("hash: $hash")
              out.beginObject().name("/").value(hash).endObject()
            }

            override fun read(input: JsonReader): T {
              log.error("READING A $type")
              input.beginObject()
              input.nextName()
              val hash = input.nextString()
              log.debug("hash: $hash")
              input.endObject()
              val json = ipfs.blocking {
                dag.get(hash, type.rawType).get().bodyText!!
              }
              log.debug("json $json")
              return delegate.fromJson(json)
            }
          }
        }
      })

    log.debug("here")

    val json = gson.setPrettyPrinting().create().toJson(uni)
    log.info("JSON: ${json}")
    val cid = Json.parseToJsonElement(json).jsonObject["/"]!!.jsonPrimitive.content
    log.debug("CID IS $cid")
    ipfs.blocking {
      val correct_cid = "bafyreidmxvzkrjfeapyl6fr5u4l3prwj5rctnmmwketysoe6la5kbmvc2q"
      require(cid == correct_cid) {
        "CID should be $correct_cid not $cid"
      }

      val addressRef = DagObjectRef<UNI.Address>(
        "bafyreiep447k4dbt65zq54lptsurxuxqusszkaptvc5dynr5hv7c3v2jyy",
        ipfs
      )

      addressRef.await<UNI.Address>().also { address ->
        log.debug("address: $address")
        require(address == uni.address) {
          "Invalid address: $address"
        }
      }


      log.warn("something test ..")
      val something = SomethingWithAUni(this, "Something", correct_cid)
      val json = gson.setPrettyPrinting().create().toJson(something)
      log.debug("something json: $json")

      val something2 = gson.create().fromJson(json, SomethingWithAUni::class.java)
      log.debug("something2: $something2")
    }


  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagRefTest::class.java)
