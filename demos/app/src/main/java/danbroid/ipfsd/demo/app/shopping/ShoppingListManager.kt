package danbroid.ipfsd.demo.app.shopping

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import danbroid.ipfs.api.cid
import danbroid.ipfsd.demo.app.appID
import danbroid.ipfsd.demo.app.appsDataStore
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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


  suspend fun createList(name: String): ShoppingList =
    ShoppingList(name).also { list ->
      val cid = list.cid()
      log.info("created list $list  with cid: $cid")
      appsDataStore.dataStore.edit {
        it[list.appID()] = cid
      }
    }

  suspend fun list() {
    log.info("list()")
    appsDataStore.getAppIDS<ShoppingList>().forEach {
      log.info("LIST: $it")
    }


  }


}

val Context.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListManager::class.java)
