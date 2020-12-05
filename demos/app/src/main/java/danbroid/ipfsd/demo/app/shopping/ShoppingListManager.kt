package danbroid.ipfsd.demo.app.shopping

import android.content.Context
import danbroid.ipfsd.demo.app.appPrefs
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ShoppingListManager(val context: Context) {

  companion object : SingletonHolder<ShoppingListManager, Context>(::ShoppingListManager)

  val coroutineScope = CoroutineScope(Dispatchers.IO)


  init {
    coroutineScope.launch {
    }
  }


  suspend fun test() {
    val cid = context.appPrefs.shoppingListApp
    log.debug("cid is $cid")
    val shoppingList = load(cid, ShoppingList::class.java)
    log.debug("shopping list: $shoppingList")
  }

  fun <T> load(cid: String, type: Class<T>) {

  }

  fun close() {
    coroutineScope.cancel("ShoppingListManager closed")
  }

}

val Context.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListManager::class.java)
