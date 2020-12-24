package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import org.junit.Test

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
    ipfs.blocking {

      dag.put(json).json().also {
        log.debug("cid: $it")
        require(it.Cid.path == people_cid) {
          "Invalid cid: ${it.Cid.path}"
        }
      }

      dag.get<List<Person>>(people_cid).json().also {
        log.warn("Persons: $it")
      }
    }

  }

  @Test
  fun test2() {
    log.info("test2()")
    ipfs.blocking {
      dag.put().apply {

      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagTest::class.java)
