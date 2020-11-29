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

open class IPFSApp : Dag {

  data class AppDescription(
    val type: String,
    var id: String = UUID.randomUUID().toString(),
    var cid: String? = null,
    val created: Long = System.currentTimeMillis()
  ) : Dag

  val description = AppDescription(javaClass.name)

  override fun toString() = description.toString()
}


class AppRegistry(val context: Context) {

  companion object : SingletonHolder<AppRegistry, Context>(::AppRegistry)

  private val apiClient = ServiceApiClient.getInstance(context)
  private val executor: CallExecutor = apiClient
  private val ipfs = API(executor)

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
        app.description.id = id
        app.description.cid = hash
        return@withContext app
      }
      val keyPrefix = "$IPFSD_APP_PREF_PREFIX/${type.name}"
      log.trace("keyPrefix: $keyPrefix")

      val cid = prefs.all.filter { it.key.startsWith(keyPrefix) }.map {
        prefs.getString(it.key, null)
      }.firstOrNull()

      if (cid != null) {
        log.warn("loading $cid")
        return@withContext ipfs.dag.get(cid).get().parseJson(type).also {
          it.description.cid = cid
          writeDag(ipfs, it)
        }
      }

      log.warn("creating new app")
      save(type.newInstance(), prefs)
    }

  suspend fun <T : IPFSApp> getAll(type: Class<T>): List<T> =
    withContext(Dispatchers.IO) {
      getIDs(type).entries.map {
        ipfs.dag.get(it.value!!.toString()).get().parseJson(type)
      }
    }


  fun <T> getIDs(
    type: Class<T>,
    prefs: SharedPreferences = defaultPrefs.prefs
  ): Map<String, Any?> =
    prefs.all.filter {
      it.key.startsWith("$IPFSD_APP_PREF_PREFIX/${type.name}")
    }


  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun <T : IPFSApp> save(app: T, prefs: SharedPreferences = defaultPrefs.prefs): T =
    withContext(Dispatchers.IO) {
      app.description.cid =
        ipfs.dag.put(pin = true).apply { addData(app.toJson().toByteArray()) }.get().value.cid.cid
      prefs.edit(commit = true) {
        putString(
          "$IPFSD_APP_PREF_PREFIX/${app.javaClass.name}/${app.description.id}",
          app.description.cid
        )
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
