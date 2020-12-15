package danbroid.ipfs.api.test

import danbroid.ipfs.api.parseJsonList
import org.junit.Test

class JsonTest {


  @Test
  fun test1() {
    val data = """ "string1" "string2" 
       "string3"
    """
    log.debug("data: $data")

    data.parseJsonList<String>().forEach {
      log.debug("string: $it")
    }
  }

/*  public final fun <T> decodeFromString(
    deserializer: DeserializationStrategy<T>,
    string: String
  ): T {
    val reader = JsonReader(string)
    val input = StreamingJsonDecoder(this, WriteMode.OBJ, reader)
    val result = input.decodeSerializableValue(deserializer)
    if (!reader.isDone) {
      error("Reader has not consumed the whole input: $reader")
    }
    return result
  }*/
}

private val log = org.slf4j.LoggerFactory.getLogger(JsonTest::class.java)
