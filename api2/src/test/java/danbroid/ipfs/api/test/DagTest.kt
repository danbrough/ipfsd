package danbroid.ipfs.api.test

import danbroid.ipfs.api.Types
import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.parseJsonList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.Test

class DagTest {
  @Test
  fun test1() {
    log.info("test1()")
    val json1 = Json.encodeToString(buildJsonObject {
      put("message", "Hello World")
      put("count", 123)
    })

    ipfs.blocking {
      Json.encodeToString(TestData.HelloWorld.data).also {
        log.debug("json: $it")
        dag.put().apply {
          add(it)
          add(json1)

        }.invoke {
          it.reader.readText().parseJsonList<Types.CID>().forEach {
            log.info("cid: $it")
          }
        }

      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagTest::class.java)
