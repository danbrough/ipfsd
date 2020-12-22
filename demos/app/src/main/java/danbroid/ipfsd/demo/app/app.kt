package danbroid.ipfsd.demo.app

import danbroid.ipfs.api.Dag
import java.util.*

const val IPFSD_APP_ID_PREFIX = "/ipfsd/apps"

open class IPFSApp : Dag {

  @Transient
  var cid: String? = null

  data class AppDescription(
    val type: String,
    var id: String = UUID.randomUUID().toString(),
    val created: Long = System.currentTimeMillis()
  ) : Dag

  val description = AppDescription(javaClass.name)

  override fun toString() = description.toString()

  suspend fun save() {

  }
}


/*

class AppRegistry(private val ipfs: IPFS, val context: Context) {


  companion object : SingletonHolder<AppRegistry, Pair<IPFS, Context>>({
    AppRegistry(it.first, it.second)
  })


  private val defaultPrefs: Prefs
    get() = context.appPrefs


  suspend fun <T : IPFSApp> get(type: Class<T>, id: String? = null): T =
    withContext(Dispatchers.IO) {
      val prefs = defaultPrefs.prefs
      if (id != null) {
        val prefKey = "$IPFSD_APP_ID_PREFIX/${type.name}/$id"
        val hash = prefs.getString(prefKey, null)
        if (hash == null) throw IllegalArgumentException("$prefKey not found")
        val app = type.newInstance()
        app.description.id = id
        return@withContext app
      }
      val keyPrefix = "$IPFSD_APP_ID_PREFIX/${type.name}"
      log.trace("keyPrefix: $keyPrefix")

      val cid = prefs.all.filter { it.key.startsWith(keyPrefix) }.map {
        prefs.getString(it.key, null)
      }.firstOrNull()

      if (cid != null) {
        log.warn("loading $cid")
        return@withContext loadApp(cid, type)
      }

      log.warn("creating new app")
      save(type.newInstance(), prefs)
    }

  private suspend fun <T : IPFSApp> loadApp(cid: String, type: Class<T>): T =
    ipfs.dag(cid, type).valueOrThrow().also {
      it.cid = cid
    }


  fun <T : IPFSApp> getAll(type: Class<T>): Flow<T> = flow {
    getIDs(type).forEach {
      emit(loadApp(it.id, type))
    }
  }

  fun getIDs(prefs: SharedPreferences = defaultPrefs.prefs): List<DagID> =
    prefs.all.filter { it.key.startsWith("$IPFSD_APP_ID_PREFIX/") }.map {
      DagID(it.key, it.value as String)
    }

  fun <T> getIDs(type: Class<T>, prefs: SharedPreferences = defaultPrefs.prefs) =
    getIDs(prefs).filter { it.id.startsWith("$IPFSD_APP_ID_PREFIX/${type.name}") }


  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun <T : IPFSApp> save(app: T, prefs: SharedPreferences = defaultPrefs.prefs): T =
    withContext(Dispatchers.IO) {
      val cid =
        ipfs.dag.put(pin = true, data = app).get().value.cid.cid
      prefs.edit(commit = true) {
        putString(
          "$IPFSD_APP_ID_PREFIX/${app.javaClass.name}/${app.description.id}",
          cid
        )
      }
      log.debug("saved $app cid: $cid")
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
  get() = AppRegistry.getInstance(Pair(this.ipfs, this))
  private val log = org.slf4j.LoggerFactory.getLogger(AppRegistry::class.java)
*/


