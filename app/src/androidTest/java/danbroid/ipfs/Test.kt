package danbroid.ipfs

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Test : IPFSTest() {

  @org.junit.Test
  fun test1() {
    log.info("test1()")
    runBlocking {
      api.id().asFlow().collect {
        log.debug("ID: $it")
      }
    }
  }

}

private val log = org.slf4j.LoggerFactory.getLogger(Test::class.java)
