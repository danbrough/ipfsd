package danbroid.ipfs.api.test

import danbroid.ipfs.api.CID_EMPTY_OBJECT
import org.junit.Assert
import org.junit.Test

class ObjectTest : CallTest() {


  @Test
  fun test() {
    val hash_hello_world = "QmXy2pAWQ3Ef1PqZqi4Z9TJnpDh1trdkCqAvzBgKNNRrSR"
    val hash_empty_string = "QmdfTbBqBPQ7VNxZEYEj14VmRuZBkqFbiwReogJgS1zR1n"
    val hello_world = "Hello World"
    ipfs {
      `object`.apply {
        var obj = patch.setData(CID_EMPTY_OBJECT, hello_world).get().valueOrThrow()
        log.debug("hash: ${obj.hash}")
        Assert.assertEquals("Object has the wrong hash", hash_hello_world, obj.hash)
        val obj2 = patch.setData(CID_EMPTY_OBJECT, "").get().valueOrThrow()
        Assert.assertEquals("Object has the wrong hash", hash_empty_string, obj2.hash)

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
      }

    }
  }


}

private val log = org.slf4j.LoggerFactory.getLogger(ObjectTest::class.java)
