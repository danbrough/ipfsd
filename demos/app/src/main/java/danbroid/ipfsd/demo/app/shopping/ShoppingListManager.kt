package danbroid.ipfsd.demo.app.shopping

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShoppingListManager(val context: Context) {

  companion object : SingletonHolder<ShoppingListManager, Context>(::ShoppingListManager)

  val datastore: DataStore<Preferences> = context.createDataStore("apps")
  suspend fun test() = withContext(Dispatchers.IO) {
    datastore.updateData {
      it
    }
/*    val cid = context.appPrefs.shoppingListApp
    ipfs.network.id().json().also {
      log.info("id is $it")
    }
    log.debug("cid is $cid")
    val list: ShoppingList
    if (cid == null) {
      log.debug("creating new shopping list")
      list = ShoppingList("default")
      context.appPrefs.shoppingListApp = list.toDag().cid()
      log.info("new cid: ${context.appPrefs.shoppingListApp}")
    } else {
      list = cid.cid<ShoppingList>().value()!!
    }

    log.debug("shopping list: $list description: ${list.description.value()}")*/
  }


}

val Context.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListManager::class.java)
