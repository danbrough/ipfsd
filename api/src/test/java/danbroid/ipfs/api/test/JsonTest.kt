package danbroid.ipfs.api.test

import kotlinx.coroutines.runBlocking
import org.junit.Test

class JsonTest {

  @Test
  fun test1() {

  }

  @Test
  fun test2() {

    runBlocking {
      api.dag.putObject()
    }
  }

}

