package danbroid.ipfs.api.test

import danbroid.ipfs.api.DagNode
import danbroid.ipfs.api.Serializable
import danbroid.ipfs.api.toDag
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

private val module = SerializersModule {
  polymorphic(Project::class) {
    subclass(OwnedProject::class)
  }

}


private val format = Json {
  serializersModule = module
}


@Serializable
data class Diet(val food: String)


@Serializable
data class Animal(val name: String, val diet: DagNode<Diet>? = null)


class SerialTest {
  @Test
  fun test1() {
    val data: Project = OwnedProject("kotlinx.coroutines", "kotlin")
    log.info(format.encodeToString(data))
    val json = Json
    val animals =
      listOf(Animal("Oscar", Diet("Bikkies").toDag()).toDag(), Animal("Satchmo").toDag())

    json.encodeToString(animals).also {
      log.info("box json: $it")
      val animals2: List<DagNode<Animal>> = json.decodeFromString(it)
      log.trace("animals: $animals")
      log.trace("animals2: $animals2")
      require(animals2 == animals) {
        "animals2 != animals"
      }
    }

  }
}

private val log = LoggerFactory.getLogger(SerialTest::class.java)