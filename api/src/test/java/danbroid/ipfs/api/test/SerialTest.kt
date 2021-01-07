package danbroid.ipfs.api.test

import danbroid.ipfs.api.DagLink
import danbroid.ipfs.api.Serializable
import danbroid.ipfs.api.dagLink
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
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


val format = Json {
  serializersModule = module
}


@Serializable
data class Diet(val food: String)


@Serializable
data class Animal(val name: String, val diet: DagLink<Diet>? = null)


class SerialTest {
  @Test
  fun test1() {
    val data: Project = OwnedProject("kotlinx.coroutines", "kotlin")
    log.info(format.encodeToString(data))
    val json = Json
    val animals =
      listOf(Animal("Oscar", Diet("Bikkies").dagLink()).dagLink(), Animal("Satchmo").dagLink())

    json.encodeToString(animals).also {
      log.info("box json: $it")
      val animals2: List<DagLink<Animal>> = json.decodeFromString(it)
      log.debug("animals: $animals2")
      require(animals2 == animals) {
        "animals2 != animals"
      }
    }

  }
}

private val log = LoggerFactory.getLogger(SerialTest::class.java)