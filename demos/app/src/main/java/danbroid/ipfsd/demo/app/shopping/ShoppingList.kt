package danbroid.ipfsd.demo.app.shopping

import danbroid.ipfs.api.Dag
import danbroid.ipfs.api.Serializable
import danbroid.ipfsd.demo.app.IPFSApp


@Serializable
class ShoppingList : IPFSApp() {
  var title: String = "Shopping List: ${description.id}"

  @Serializable
  data class Thang(var count: Int, val msg: String) : Dag


  val thang1 = Thang(12, "a thang")


  val thang2 = Thang(2, "Thang 2")

  override fun toString() = "${super.toString()}:$title:$thang1:$thang2"


}

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingList::class.java)
