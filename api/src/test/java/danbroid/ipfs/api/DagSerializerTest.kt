package danbroid.ipfs.api

import org.junit.Test


class Person(val name: String, val count: Int)


class DagSerializerTest {
  @Test
  fun test() {
    log.info("test()")
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagSerializerTest::class.java)
