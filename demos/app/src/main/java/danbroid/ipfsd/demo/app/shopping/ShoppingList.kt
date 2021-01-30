package danbroid.ipfsd.demo.app.shopping

import danbroid.ipfs.api.DagNode
import danbroid.ipfs.api.Serializable
import danbroid.ipfsd.demo.app.IPFSApp


@Serializable
data class ShoppingList(val title: String) : IPFSApp() {


  @Serializable
  data class Thang(var count: Int, val msg: String)


  var thang: DagNode<Thang>? = null


  override fun toString() = "${super.toString()}:$title:$thang"


}

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingList::class.java)
