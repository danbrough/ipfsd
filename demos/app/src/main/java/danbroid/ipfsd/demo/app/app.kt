package danbroid.ipfsd.demo.app

import android.content.Context
import danbroid.ipfs.api.API
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfsd.client.ServiceApiClient
import danbroid.ipfsd.demo.app.shopping.ShoppingList
import danbroid.util.misc.SingletonHolder
import danbroid.util.prefs.edit
import java.util.*

const val IPFSD_APP_PREF_PREFIX = "/ipfsd/apps"

open class IPFSApp {

  var id: String = UUID.randomUUID().toString()
  var hash: String? = null


}


class AppRegistry(val context: Context) {

  companion object : SingletonHolder<AppRegistry, Context>(::AppRegistry) {

  }

  init {
    context.appPrefs.prefs.all.filter { it.key.startsWith(IPFSD_APP_PREF_PREFIX) }.forEach {

    }

  }

  private val apiClient = ServiceApiClient.getInstance(context)
  private val executor: CallExecutor = apiClient


  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun <T : IPFSApp> get(type: Class<T>, id: String? = null): T {
    val prefs = context.appPrefs.prefs
    if (id != null) {
      val prefKey = "$IPFSD_APP_PREF_PREFIX/${type.name}/$id"
      val hash = prefs.getString(prefKey, null)
      if (hash == null) throw IllegalArgumentException("$prefKey not found")
      val app = type.newInstance()
      app.id = id
      app.hash = hash
      return app
    }
    val keyPrefix = "$IPFSD_APP_PREF_PREFIX/${type.name}/"
    return prefs.all.filter { it.key.startsWith(keyPrefix) }.map {
      type.newInstance().apply {
        this.hash = prefs.getString(it.key, null)
        this.id = it.key.substring(keyPrefix.length)
      }
    }.getOrNull(0) ?: let {
      API.Dag.put(pin = true).addData("").get(executor).let {
        TODO("RESULT: ${it.value}")
      }
    }
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun <T : IPFSApp> save(app: T, type: Class<T>) {
    context.appPrefs.edit {
    }
  }


  suspend fun test() {
    log.warn("test()")
    kotlin.runCatching {
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
