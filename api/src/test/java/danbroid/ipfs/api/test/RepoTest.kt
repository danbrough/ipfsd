package danbroid.ipfs.api.test

import org.junit.Test

class RepoTest : CallTest() {

  @Test
  fun stat() = ipfs.blocking {
    repo.stat(human = true).get().valueOrThrow().also {
      log.debug("RESULT: $it")
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(KeyTests::class.java)
