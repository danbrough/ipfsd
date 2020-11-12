package danbroid.ipfsd.demo.app.shopping

import danbroid.ipfsd.demo.app.IPFSApp

class ShoppingList : IPFSApp() {
  var title: String = "Shopping List: $id"


}

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingList::class.java)
