package danbroid.ipfs.api.test

import danbroid.ipfs.api.Types
import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.junit.Test
import org.slf4j.LoggerFactory


interface Project

@Serializable
//@SerialName("owned")
class OwnedProject(val name: String, val owner: String) : Project

val module = SerializersModule {
  polymorphic(Project::class) {
    subclass(OwnedProject::class)
  }

}


open class TestSerializer<T>(val serializer: KSerializer<T>) : KSerializer<T> {
  override val descriptor: SerialDescriptor = Types.Link.serializer().descriptor

  override fun deserialize(decoder: Decoder): T {
    val link = Types.Link.serializer().deserialize(decoder)
    return ipfs.blocking {
      Json.decodeFromString(serializer, dag.get<Any>(link.path).invoke().text)
    }
  }

  override fun serialize(encoder: Encoder, value: T) {
    log.debug("serializer() $value")
    val data = Json.encodeToString(serializer, value)
    log.debug("data: $data")
    ipfs.blocking {
      ipfs.dag.put(data).json().also {
        log.debug("path: $it")
        Types.CID.serializer().serialize(encoder, it)
      }
    }
  }
}


val format = Json {
  serializersModule = module
}


@Serializable(with = DagLinkSerializer::class)
data class DagLink<T>(val value: T)

@Serializable
data class Diet(val food: String)

@Serializable
data class Animal(val name: String, val diet: DagLink<Diet>? = null)

inline fun <reified T> T.dagLink() = DagLink(this)

class DagLinkSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<DagLink<T>> {
  val linkSerializer = Types.Link.serializer()
  override val descriptor: SerialDescriptor = dataSerializer.descriptor


  override fun serialize(encoder: Encoder, value: DagLink<T>) {
    //encoder.encodeSerializableValue(dataSerializer, value.contents)
    log.debug("serialize() $value")
    val format = Json {
      serializersModule = encoder.serializersModule
    }
    val json = format.encodeToString(dataSerializer, value.value)
    log.debug("json: $json")
    ipfs.blocking {
      val cid = dag.put(json).json()
      log.debug("cid: $cid")
      linkSerializer.serialize(encoder, Types.Link(cid.Cid.path))
    }

  }

  override fun deserialize(decoder: Decoder): DagLink<T> {
    val format = Json {
      serializersModule = decoder.serializersModule
    }
    val link = linkSerializer.deserialize(decoder)
    val json = ipfs.blocking {
      dag.get<Any>(link.path).invoke().text
    }
    return DagLink(format.decodeFromString(dataSerializer, json))
    //DagLink(dataSerializer.deserialize(decoder))
  }
}

class SerialTest {
  @Test
  fun test1() {
    val data: Project = OwnedProject("kotlinx.coroutines", "kotlin")
    log.info(format.encodeToString(data))

    val cat =
      listOf(Animal("Oscar", Diet("Bikkies").dagLink()).dagLink(), Animal("Satchmo").dagLink())

    format.encodeToString(cat).also {
      log.info("box json: $it")
      val animals:List<DagLink<Animal>> = format.decodeFromString(it)
      log.debug("animals: $animals")
      require(animals == cat){
        "not good"
      }
    }

  }
}

private val log = LoggerFactory.getLogger(SerialTest::class.java)