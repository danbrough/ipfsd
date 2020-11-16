package danbroid.ipfs.api

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*


class Project(
  // val in a primary constructor -- serialized
  val name: String
) {
  var stars: Int = 0 // property with getter & setter -- serialized

  val path: String // getter only -- not serialized
    get() = "kotlin/$name"

  private var locked: Boolean = false // private, not accessible -- not serialized
}

@Serializer(forClass = Project::class)
object ProjectSerializer

object DateAsLongSerializer : KSerializer<Date> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)
  override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)
  override fun deserialize(decoder: Decoder): Date = Date(decoder.decodeLong())
}

@Serializable
data class Thang(
  var name: String = "OK",
  @SerialName("dude")
  val s1: Stuff,
  @Contextual
  var date: Date
) {

  init {
    require(name.isNotEmpty()) { "name cannot be empty" }
  }

  @Serializable
  data class Stuff(val count: Int, val msg: String)


}

@Serializable
class ProgrammingLanguage(
  val name: String,
  @Contextual
  val stableReleaseDate: Date
)

private val module = SerializersModule {
  contextual(DateAsLongSerializer)
  contextual(Project::class, ProjectSerializer)
}

val format = Json {
  serializersModule = module
}

class JsonTest {
  @Test
  fun test1() {
    val data = Project("kotlinx.serialization").apply { stars = 9000 }
    println(Json.encodeToString(ProjectSerializer, data))

    val project =
      ProgrammingLanguage("Kotlin", SimpleDateFormat("yyyy-MM-ddX").parse("2016-02-15+00"))
    println(format.encodeToString(project))

    format.encodeToString(data).also {
      log.info("STARS: $it")
    }
    format.encodeToString(
      Thang(
        "test",
        Thang.Stuff(123, "Hello at ${System.currentTimeMillis()}"),
        Date()
      )
    )
      .also {
        log.info("JSON: $it")
      }


    format.encodeToString(Date()).also {
      log.info("CURRENT DATE: $it")
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(JsonTest::class.java)
