package danbroid.ipfs.api.test

import danbroid.ipfs.api.API
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RepoTest : CallTest() {

  @Test
  fun stat() = callTest(ipfs.repo.stat(human = true)) {
    log.debug("RESULT: $it")
  }

}

private val log = org.slf4j.LoggerFactory.getLogger(KeyTests::class.java)
