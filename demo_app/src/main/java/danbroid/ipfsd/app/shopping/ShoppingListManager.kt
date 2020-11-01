package danbroid.ipfsd.app.shopping

import android.app.Activity
import android.content.Context
import danbroid.ipfs.api.API
import danbroid.ipfsd.app.AppRegistry
import danbroid.ipfsd.client.model.ipfsModel
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import java.util.*

class ShoppingListManager(context: Context) {

  companion object : SingletonHolder<ShoppingListManager, Context>(::ShoppingListManager) {
    const val SHOPPING_FILES_PREFIX = "${AppRegistry.IPFSD_FILES_PREFIX}/shopping_lists"
  }

  val executor = (context as Activity).ipfsModel.callExecutor
  val coroutineScope = CoroutineScope(Dispatchers.IO)

  val shoppingLists = coroutineScope.produce {
    log.warn("producing shoppingLists")
    API.Files.ls("$SHOPPING_FILES_PREFIX/").get(executor).value.entries.map {
      ShoppingList(it.name, it.hash)
    }.also {
      log.warn("sending shopping lists")
      send(it)
    }
  }

  init {
    coroutineScope.launch {
      log.debug("shopping lists: ${shoppingLists.receive()}")
    }
  }


  suspend fun createList() {
    val path = "$SHOPPING_FILES_PREFIX/${UUID.randomUUID()}.json"
    API.Files.write(path, create = true, parents = true).addData("{}").get(executor).also {
      log.warn("RESULT: $it")
    }
  }

  fun close() {
    coroutineScope.cancel("ShoppingListManager closed")
  }

}

val Context.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListManager::class.java)
