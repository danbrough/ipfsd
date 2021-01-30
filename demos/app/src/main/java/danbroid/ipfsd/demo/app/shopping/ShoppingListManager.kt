package danbroid.ipfsd.demo.app.shopping

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import danbroid.ipfs.api.cid
import danbroid.ipfs.api.dagNode
import danbroid.ipfsd.demo.app.appID
import danbroid.ipfsd.demo.app.appsDataStore
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.suspendCoroutine

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

  suspend fun createList(lifecycleOwner: LifecycleOwner): ShoppingList? =
    suspendCoroutine<String?> { continuation ->
      MaterialDialog(context).show {
        lifecycleOwner(lifecycleOwner)
        input("Enter a name:") { _, text ->
          continuation.resumeWith(Result.success(text.toString()))
        }
        title(text = "New Shopping List")
        positiveButton(android.R.string.ok)
        negativeButton(android.R.string.cancel) {
          continuation.resumeWith(Result.success(null))
        }
      }
    }.let {
      if (it != null) {
        log.trace("creating list with title: $it")
        createList(it)
      } else null
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

  suspend fun test() = withContext(Dispatchers.IO) {
    log.info("test()")
    val listCid = appsDataStore.getAppIDS<ShoppingList>().firstOrNull() ?: return@withContext
    log.debug("list: ${listCid.cid}")
    val list = dagNode<ShoppingList>(listCid.cid).value()
    log.debug("list: $list")
    val count = (list.thang?.value()?.count ?: 0) + 1
    list.thang = ShoppingList.Thang(count, "Thang:$count").dagNode()
    log.debug("list now: $list")
    val newCid = list.dagNode().cid()
    log.debug("new cid: $newCid with appID: ${list.appID().name}")
    appsDataStore.dataStore.edit {
      it[list.appID()] = newCid
    }


  }

  suspend fun deleteList(id: String, action: () -> Unit) {
    log.info("deleteList() $id")
    MaterialDialog(context).show {
      title(text = "Delete $id")
      message(text = "Are you sure?")
      positiveButton(android.R.string.ok) {
        runBlocking {
          appsDataStore.dataStore.edit {
            it.remove(stringPreferencesKey(id))
          }
        }
        action.invoke()
      }
      negativeButton(android.R.string.cancel)
    }
  }


}

val Context.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListManager::class.java)
