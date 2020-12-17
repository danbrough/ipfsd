package danbroid.ipfs.api.test

import danbroid.ipfs.api.jsonSequence
import danbroid.ipfs.api.parseJsonList
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.Test

class JsonTest {

  @Test
  fun test1() {
    val data = """ "string1" "string2" 
       "string3"
    """
    log.debug("data: $data")

    data.jsonSequence<String>().forEach {
      log.debug("string: $it")
    }
  }

  @Test
  fun test2() {


  }

}

private val log = org.slf4j.LoggerFactory.getLogger(JsonTest::class.java)
