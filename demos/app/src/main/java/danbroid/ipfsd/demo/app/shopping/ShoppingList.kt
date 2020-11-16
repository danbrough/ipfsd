package danbroid.ipfsd.demo.app.shopping

import danbroid.ipfsd.demo.app.Dag
import danbroid.ipfsd.demo.app.IPFSApp

class ShoppingList : IPFSApp() {
  var title: String = "Shopping List: ${description.id}"

  @Dag
  data class Thang(val count: Int, val msg: String)


  val thang1 = Thang(12, "a thang")


  val thang2 = Thang(2, "Thang 2")


}

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingList::class.java)
