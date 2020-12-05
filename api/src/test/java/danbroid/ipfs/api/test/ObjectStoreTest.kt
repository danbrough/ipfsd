package danbroid.ipfs.api.test

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import danbroid.ipfs.api.CID_EMPTY_OBJECT
import danbroid.ipfs.api.IPFS
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.Json
import org.junit.Test
import java.awt.Color
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass

interface Person {
  var name: String
  var age: Int
}

@Suppress("UNCHECKED_CAST")
@InternalSerializationApi

class IPFSObjectSerializer<T : Any>(val ipfs: IPFS, val type: KClass<T>) : KSerializer<T> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("IPFSObject", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): T {
    log.debug("deserialize()")
    val hash = decoder.decodeString()
    log.debug("hash: $hash")
    return runBlocking {
      ipfs.`object`.get(hash).get().valueOrThrow().let {
        log.debug("object is: $it")
        Json.decodeFromString(type.serializer(), it.data)
      }
    }
  }

  override fun serialize(encoder: Encoder, value: T) {
    ipfs {
      val json = Json.encodeToString(type.serializer(), value)
      `object`.patch.setData(CID_EMPTY_OBJECT, json).get().also {
        log.warn("object: $it")
        encoder.encodeString(it.valueOrThrow().hash)
      }
    }
  }

}


@Serializable
data class Student(override var name: String, override var age: Int) : Person


@Serializable
data class School(val name: String, val year: Int) {
  val students = mutableSetOf<Student>()
}

fun school() = School("Newtown Primary", 1880).also {
  it.students.add(Student("Dan Brough", 10))
  it.students.add(Student("Steve", 11))
}

class ColorAsObjectSerializer : KSerializer<Color> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Color") {
    element<Int>("r")
    element<Int>("g")
    element<Int>("b")
  }

  override fun deserialize(decoder: Decoder): Color =
    decoder.decodeStructure(descriptor) {
      var r = -1
      var g = -1
      var b = -1
      while (true) {
        when (val index = decodeElementIndex(descriptor)) {
          0 -> r = decodeIntElement(descriptor, 0)
          1 -> g = decodeIntElement(descriptor, 1)
          2 -> b = decodeIntElement(descriptor, 2)
          CompositeDecoder.DECODE_DONE -> break
          else -> error("Unexpected index: $index")
        }
      }
      require(r in 0..255 && g in 0..255 && b in 0..255)
      Color((r shl 16) or (g shl 8) or b)
    }

  override fun serialize(encoder: Encoder, value: Color) =
    encoder.encodeStructure(descriptor) {
      encodeIntElement(descriptor, 0, (value.rgb shr 16) and 0xff)
      encodeIntElement(descriptor, 1, (value.rgb shr 8) and 0xff)
      encodeIntElement(descriptor, 2, value.rgb and 0xff)
    }
}

class ObjectStoreTest : CallTest() {

  companion object {
    val school = school()
  }

  fun save(school: School) {
    log.info("DESCRIPTOR: ${School.serializer().descriptor}")
    Json.encodeToString(school).also {
      log.info("JSON: $it")

    }
  }

  @InternalSerializationApi
  @Test
  fun test() {
    log.info("test()")

    save(school)
    val serializer = IPFSObjectSerializer(ipfs, Student::class)
    school.students.forEach { student ->
      Json.encodeToString(serializer, student).also {
        log.info("student: $student => $it")
        val student2 = Json.decodeFromString(serializer, it)
        log.debug("student2: $student2")
        require(student == student2) {
          log.warn("Invalid student match")
        }
      }
    }


  }

  @Test
  fun test2() {
    log.info("test2()")
    val school = school()
    Json.encodeToString(school).also {
      log.info("JSON: $it")
    }
  }
}

class DataTypeAdapterFactory(val ipfs: IPFS) : TypeAdapterFactory {
  val cache = mutableMapOf<Any, IPFS.Object.Object>()

  override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
    val dataFactory = this
    val isData = Reflection.createKotlinClass(type.rawType).isData
    log.debug("type: $type isData: ${isData}")
    if (!isData) return null
    return object : TypeAdapter<T>() {
      override fun write(out: JsonWriter, value: T) {
        log.trace("write: $value")
        val json = gson.getDelegateAdapter(dataFactory, type).toJson(value)
        log.trace("json: $json")
        cache.get(value)?.also {

        }
        ipfs {
          val o = `object`.patch.setData(CID_EMPTY_OBJECT, json).get().valueOrThrow()
          log.debug("object: $o")
          out.value(o.hash)
        }
      }

      override fun read(input: JsonReader): T {
        TODO("Not yet implemented")
      }
    }
  }
}

suspend fun IPFS.store(o: Any?, type: Class<*>): String {
  val gson = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
    .registerTypeAdapterFactory(DataTypeAdapterFactory(this))
    .create()

  val data = gson.toJson(o, type)
  log.debug("data: $data")
  return ""
}

inline suspend fun <reified T> IPFS.store(o: T) = store(o, T::class.java)

private val log = org.slf4j.LoggerFactory.getLogger(ObjectStoreTest::class.java)
