package danbroid.ipfs.api.test

import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import org.junit.Test

class ObjectTest {

  @Test
  fun test() {
    log.info("test1()")
    val hash_hello_world = "QmXy2pAWQ3Ef1PqZqi4Z9TJnpDh1trdkCqAvzBgKNNRrSR"
    val hash_empty_string = "QmdfTbBqBPQ7VNxZEYEj14VmRuZBkqFbiwReogJgS1zR1n"
    val hello_world = "Hello World"

    ipfs.blocking {
      var result = obj.patch.setData(TestData.CID_EMPTY_OBJECT, "").invoke().json()
      require(result.Hash == hash_empty_string) {
        "Invalid hash: ${result.Hash} expecting $hash_empty_string"
      }
      
      result = obj.patch.setData(TestData.CID_EMPTY_OBJECT, hello_world).invoke().json()
      require(result.Hash == hash_hello_world) {
        "Invalid hash: ${result.Hash} expecting $hash_hello_world"
      }


/*    `object`.apply {


      obj = get(hash_hello_world).get().valueOrThrow()
      log.debug("got object: $obj")
      Assert.assertEquals("Expecting $hello_world", hello_world, obj.data)


      patch.addLink(hash_hello_world, "empty_message", hash_empty_string).get()
        .valueOrThrow().also {
          log.debug("hash: ${it.hash}")
          Assert.assertEquals(
            "Incorrect hash: ${it.hash}",
            "QmPe2j1iNJ7TowcHWzygeyhs656RjBtcA9xrnbvA9LFPS4",
            it.hash
          )
        }
    }*/

    }


  }
}

private val log = org.slf4j.LoggerFactory.getLogger(ObjectTest::class.java)
