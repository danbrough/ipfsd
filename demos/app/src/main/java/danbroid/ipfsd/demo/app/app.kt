package danbroid.ipfsd.demo.app

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import danbroid.ipfs.api.API
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfs.api.utils.toJson
import danbroid.ipfsd.client.ServiceApiClient
import danbroid.ipfsd.demo.app.shopping.ShoppingList
import danbroid.util.misc.SingletonHolder
import danbroid.util.prefs.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

const val IPFSD_APP_PREF_PREFIX = "/ipfsd/apps"

open class IPFSApp {

  var id: String = UUID.randomUUID().toString()
  var hash: String? = null
  val type: String = javaClass.name

  override fun toString() = "$type<$id:$hash>"

}


class AppRegistry(val context: Context) {

  companion object : SingletonHolder<AppRegistry, Context>(::AppRegistry)


  private val apiClient = ServiceApiClient.getInstance(context)
  private val executor: CallExecutor = apiClient
  private val defaultPrefs: Prefs
    get() = context.appPrefs


  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun <T : IPFSApp> get(type: Class<T>, id: String? = null): T =
    withContext(Dispatchers.IO) {
      val prefs = defaultPrefs.prefs
      if (id != null) {
        val prefKey = "$IPFSD_APP_PREF_PREFIX/${type.name}/$id"
        val hash = prefs.getString(prefKey, null)
        if (hash == null) throw IllegalArgumentException("$prefKey not found")
        val app = type.newInstance()
        app.id = id
        app.hash = hash
        return@withContext app
      }
      val keyPrefix = "$IPFSD_APP_PREF_PREFIX/${type.name}"

      val cid = prefs.all.filter { it.key.startsWith(keyPrefix) }.map {
        prefs.getString(it.key, null)
      }.firstOrNull()

      if (cid != null) {
        log.debug("loading $cid")
        return@withContext API.Dag.get(cid).get(executor).parseJson(type).also {
          it.hash = cid
        }
      }

      save(type.newInstance(), prefs)
    }


  fun <T> getIDs(type: Class<T>, prefs: SharedPreferences = defaultPrefs.prefs): List<String> =
    prefs.all.filter { it.key.startsWith("$IPFSD_APP_PREF_PREFIX/${type.name}") }
      .map { it.key }


  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun <T : IPFSApp> save(app: T, prefs: SharedPreferences = defaultPrefs.prefs): T =
    withContext(Dispatchers.IO) {
      app.hash = API.Dag.put(pin = true).addData(app.toJson()).get(executor).value.cid.cid
      prefs.edit(commit = true) {
        putString("$IPFSD_APP_PREF_PREFIX/${app.javaClass.name}/${app.id}", app.hash)
      }
      app
    }


  suspend fun test() {
    log.warn("test()")
    runCatching {
      get(ShoppingList::class.java).also {
        log.warn("LOADED $it")
      }
    }.exceptionOrNull()?.also {
      log.error(it.message, it)
    }
  }
}


val Context.appRegistry: AppRegistry
  get() = AppRegistry.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(AppRegistry::class.java)
