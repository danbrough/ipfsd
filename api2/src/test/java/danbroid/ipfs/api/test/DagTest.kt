package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import org.junit.Test

class DagTest {
  @Test
  fun test1() {
    log.info("test1()")
    val people = buildJsonArray {
      addJsonObject {
        put("name", "Dan")
        put("age", 49)
      }
      addJsonObject {
        put("name", "Fred")
        put("age", 12)
      }
    }
    val json = Json.encodeToString(people)
    ipfs.blocking {
      dag.put(json).json {
        log.debug("cid: $it")
      }
    }

  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagTest::class.java)
