package danbroid.ipfsd.demo.app.shopping

import danbroid.ipfsd.demo.app.Dag
import danbroid.ipfsd.demo.app.IPFSApp

class ShoppingList : IPFSApp(), Dag {
  var title: String = "Shopping List: ${description.id}"

  data class Thang(val count: Int, val msg: String) : Dag


  val thang1 = Thang(12, "a thang")


  val thang2 = Thang(2, "Thang 2")


}

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingList::class.java)
