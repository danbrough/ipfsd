package danbroid.ipfsd.demo.app.shopping

import danbroid.ipfs.api.Serializable
import danbroid.ipfsd.demo.app.IPFSApp


@Serializable
class ShoppingList : IPFSApp() {

  @Serializable
  data class Thang(var count: Int, val msg: String)

  var thang1 = Thang(12, "a thang")

  override fun toString() = "${super.toString()}:$title:$thang1"


}

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingList::class.java)
