package danbroid.ipfs.api.test

import danbroid.ipfs.api.FilePart
import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import org.junit.Test
import java.io.File

class DagTest {

  @Serializable
  data class Person(val name: String, val age: Int)

  val jsonData = buildJsonArray {
    addJsonObject {
      put("name", "Dan")
      put("age", 49)
    }
    addJsonObject {
      put("name", "Fred")
      put("age", 12)
    }
  }

  val people = listOf(Person("Dan", 49), Person("Fred", 12))

  val people_cid = "bafyreievey725uimxvlfajdov3fq5fpja6dtqjdinhqfccqjgbjwb763fi"


  @Test
  fun test1() {
    log.info("test1()")

    val json = Json.encodeToString(jsonData)
    api.blocking {

      dag.put(json).json().also {
        log.info("cid: $it")
        require(it.Cid.path == people_cid) {
          "Invalid cid: ${it.Cid.path}"
        }
      }

      dag.get<List<Person>>(people_cid).json().also {
        log.info("Persons: $it")
        require(it == people) {
          "List doesnt match"
        }
      }
    }
  }

  @Test
  fun fileTest() {
    log.debug("fileTest()")
    api.blocking {
      dag.put(pin = true).apply {
        add(FilePart(File("/tmp/test")))
      }.json().also {
        log.debug("CID: $it")
      }
    }
  }
}

