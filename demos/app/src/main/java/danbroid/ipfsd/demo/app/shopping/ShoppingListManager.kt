package danbroid.ipfsd.demo.app.shopping

import android.app.Activity
import android.content.Context
import danbroid.ipfs.api.API
import danbroid.ipfsd.client.model.ipfsModel
import danbroid.ipfsd.demo.app.AppRegistry
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*

class ShoppingListManager(context: Context) {

  companion object : SingletonHolder<ShoppingListManager, Context>(::ShoppingListManager) {
  }

  val executor = (context as Activity).ipfsModel.callExecutor
  val coroutineScope = CoroutineScope(Dispatchers.IO)


  init {
    coroutineScope.launch {
    }
  }


  suspend fun createList() {

  }

  fun close() {
    coroutineScope.cancel("ShoppingListManager closed")
  }

}

val Context.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListManager::class.java)
