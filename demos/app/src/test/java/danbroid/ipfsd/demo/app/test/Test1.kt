package danbroid.ipfsd.demo.app.test

import danbroid.ipfs.api.*
import danbroid.ipfsd.demo.app.shopping.ShoppingList
import org.junit.Test


class Test1 {
  @Test
  fun test1() {
    log.info("test1()")

    ipfs.blocking {
      log.debug("running test")
      val list = ShoppingList("Test List").also {
        it.thang = ShoppingList.Thang(111, "Its a thang").dagNode()
      }
      log.debug("json: ${list.toJson()}")
      val cid = dag.put(list).json().Cid.path
      log.debug("cid: $cid")
      val list2 = dag.get<ShoppingList>(cid).json()
      log.debug("list2: $list2")
      require(list == list2) {
        "Lists do not match"
      }
      log.debug("description: ${list.description}")
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(Test1::class.java)
