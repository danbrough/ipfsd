package danbroid.ipfsd.demo.app.shopping

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import danbroid.ipfs.api.dagLink
import danbroid.ipfsd.client.ipfsApi
import danbroid.ipfsd.demo.app.IPFSApp
import danbroid.ipfsd.demo.app.appID
import danbroid.ipfsd.demo.app.appsDataStore
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import java.io.IOException

class ShoppingListManager(val context: Context) {

  companion object : SingletonHolder<ShoppingListManager, Context>(::ShoppingListManager)

  val appsDataStore = context.appsDataStore

  val appPrefs: Flow<Preferences> = appsDataStore.prefs
    .catch { exception ->
      if (exception is IOException) {
        log.warn("no app prefs")
        emit(emptyPreferences())
      } else {
        throw exception
      }
    }


  suspend fun createList(name: String): ShoppingList = withContext(Dispatchers.IO) {
    log.info("createList() $name")
    ShoppingList().also { list ->
      list.title = name
      list.description =
        IPFSApp.AppDescription(ShoppingList::class.java.name).dagLink(context.ipfsApi)
      log.debug("getting cid ...")
      val node = list.dagLink(context.ipfsApi)
      log.info("created list $list")
      log.info("cid is ${node.cid}")
      appsDataStore.dataStore.edit {
        it[list.appID(context.ipfsApi)] = node.cid
      }
    }
  }

  suspend fun list() = withContext(Dispatchers.IO) {
    log.info("list()")
    appsDataStore.getAppIDS<ShoppingList>().forEach {
      log.info("LIST: $it")
    }


  }


}

val Context.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = danbroid.logging.getLog(ShoppingListManager::class)
