package danbroid.ipfs.api.test

import danbroid.ipfs.api.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.junit.Test


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
data class Animal(val name: String, val diet: DagLink<Diet>? = null)


class SerialTest {
  @Test
  fun test1() {
    val data: Project = OwnedProject("kotlinx.coroutines", "kotlin")
    log.info(format.encodeToString(data))
    val json = format

    api.blocking {
      val satchmo = Animal("Satchmo", Diet("Biikkies").dagLink(this))
      val data = satchmo.toJson(this.json)
      log.info("data: $data")
      val satchmo2: Animal = data.parseJson(this.json)
      log.info("satchmo2: $satchmo2")

      val satchLink: DagLink<Animal> = satchmo.dagLink(this)
      log.info("satchlink $satchLink")


      val animals: List<DagLink<Animal>> =
        listOf(
          Animal("Oscar", Diet("Bikkies").dagLink(this)).dagLink<Animal>(this),
          satchLink
        )

      log.debug("created $animals")
      json.encodeToString(animals).also {
        log.info("box json: $it")
        val animals2: List<DagLink<Animal>> = json.decodeFromString(it)
        log.trace("animals: $animals")
        log.trace("animals2: $animals2")
        require(animals2 == animals) {
          "animals2 != animals"
        }

      }
    }
  }


}

