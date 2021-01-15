package danbroid.ipfsd.demo.app.shopping

import android.content.Context
import danbroid.ipfs.api.cid
import danbroid.ipfs.api.ipfs
import danbroid.ipfs.api.json
import danbroid.ipfs.api.toDag
import danbroid.ipfsd.demo.app.appPrefs
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShoppingListManager(val context: Context) {

  companion object : SingletonHolder<ShoppingListManager, Context>(::ShoppingListManager)


  suspend fun test() = withContext(Dispatchers.IO) {
    val cid = context.appPrefs.shoppingListApp
    ipfs.network.id().json().also {
      log.info("id is $it")
    }
    log.debug("cid is $cid")
    val list: ShoppingList
    if (cid == null) {
      log.debug("creating new shopping list")
      list = ShoppingList("default")
      context.appPrefs.shoppingListApp = list.toDag().cid
    } else {
      list = cid.cid<ShoppingList>().value()!!
    }

    log.debug("shopping list: $list description: ${list.description.value()}")
  }


}

val Context.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListManager::class.java)
