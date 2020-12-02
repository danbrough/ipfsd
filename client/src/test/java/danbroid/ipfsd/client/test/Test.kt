package danbroid.ipfsd.client.test

class Test {
  @org.junit.Test
  fun test() {
    log.warn("test1()")

  }
}

private val log = org.slf4j.LoggerFactory.getLogger(Test::class.java)
