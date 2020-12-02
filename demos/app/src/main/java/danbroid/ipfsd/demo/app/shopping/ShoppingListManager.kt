package danbroid.ipfsd.demo.app.shopping

import android.content.Context
import danbroid.ipfsd.demo.app.appRegistry
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ShoppingListManager(val context: Context) {

  companion object : SingletonHolder<ShoppingListManager, Context>(::ShoppingListManager) {
  }

  val appRegistry = context.appRegistry
  val coroutineScope = CoroutineScope(Dispatchers.IO)


  init {
    coroutineScope.launch {
    }
  }


  suspend fun test() {
    val app = appRegistry.get(ShoppingList::class.java)
    log.debug("test() $app")
    app.thang1.count++

    appRegistry.save(app)

  }

  fun close() {
    coroutineScope.cancel("ShoppingListManager closed")
  }

}

val Context.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListManager::class.java)
