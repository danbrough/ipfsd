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

    api.blocking {
      var result = obj.patch.setData(TestData.CID_EMPTY_OBJECT, "").json()
      require(result.Hash == hash_empty_string) {
        "Invalid hash: ${result.Hash} expecting $hash_empty_string"
      }

      result = obj.patch.setData(TestData.CID_EMPTY_OBJECT, hello_world).json()
      require(result.Hash == hash_hello_world) {
        "Invalid hash: ${result.Hash} expecting $hash_hello_world"
      }

      val o = obj.get(hash_hello_world).json()
      require(o.Data == hello_world) {
        "${o.Data} != $hello_world"
      }

      obj.patch.addLink(hash_hello_world, "empty_message", hash_empty_string).json().also {
        val expected_hash = "QmPe2j1iNJ7TowcHWzygeyhs656RjBtcA9xrnbvA9LFPS4"
        require(it.Hash == expected_hash) {
          "Hash: ${it.Hash} != $expected_hash"
        }
      }
    }


  }
}

