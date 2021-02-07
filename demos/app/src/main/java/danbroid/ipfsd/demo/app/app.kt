package danbroid.ipfsd.demo.app

import android.content.Context
import danbroid.ipfs.api.*
import danbroid.ipfs.api.utils.SingletonHolder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*


@Serializable
open class IPFSApp() {
  companion object {
    const val ID_PREFIX = "ipfsd/apps"
  }

  @Transient
  lateinit var api: IPFS

  @Serializable
  data class AppDescription(
    val type: String,
    var id: String = UUID.randomUUID().toString(),
    val created: Long = System.currentTimeMillis()
  )


  val description: DagLink<AppDescription> =
    api.blocking { AppDescription(javaClass.name).dagLink(api) }

  override fun toString() = description.toString()


}



class AppRegistry(private val ipfs: IPFS, val context: Context) {


  companion object : SingletonHolder<AppRegistry, Pair<IPFS, Context>>({
    AppRegistry(it.first, it.second)
  })


/*  suspend fun <T : IPFSApp> get(type: KClass<T>, id: String? = null): T =
    withContext(Dispatchers.IO) {
      val prefs = defaultPrefs.prefs
      if (id != null) {
        val prefKey = "$IPFSD_APP_ID_PREFIX/${type.java.name}/$id"
        val hash = prefs.getString(prefKey, null)
        if (hash == null) throw IllegalArgumentException("$prefKey not found")
        val app = type.createInstance()
        app.description.value.id = id
        return@withContext app
      }
      val keyPrefix = "$IPFSD_APP_ID_PREFIX/${type.java.name}"
      log.trace("keyPrefix: $keyPrefix")

      val cid = prefs.all.filter { it.key.startsWith(keyPrefix) }.map {
        prefs.getString(it.key, null)
      }.firstOrNull()

      if (cid != null) {
        log.warn("loading $cid")
        return@withContext loadApp(cid, type.serializer())
      }

      log.warn("creating new app")
      save(type.createInstance(), type, prefs)
    }

  private suspend inline fun <T : Any> loadApp(
    cid: String,
    serializer: KSerializer<T>
  ): T =
    DagNode(null, serializer, cid).value()!!


  fun <T : IPFSApp> getAll(type: KClass<T>): Flow<T> = flow {
    defaultPrefs.prefs.getIDs(type.java).forEach {
      emit(loadApp(it.id, type.serializer()))
    }
  }*/

/*  data class DagID(val id: String, val cid: String)


  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun <T : IPFSApp> save(
    app: T,
    type: KClass<T>,
    prefs: SharedPreferences = defaultPrefs.prefs
  ): T =
    withContext(Dispatchers.IO) {
      val cid = app.toDag(type.serializer()).cid()
      prefs.edit(commit = true) {
        putString(
          "$IPFSD_APP_ID_PREFIX/${app.javaClass.name}/${app.description.value.id}",
          cid
        )
      }
      log.debug("saved $app cid: $cid")
      app
    }


  suspend fun test() {
    log.warn("test()")
    runCatching {
      get(ShoppingList::class).also {
        log.warn("LOADED $it")
      }
    }.exceptionOrNull()?.also {
      log.error(it.message, it)
    }
  }*/
}


/*
fun SharedPreferences.getIDs(): List<AppRegistry.DagID> =
  all.filter { it.key.startsWith("$IPFSD_APP_ID_PREFIX/") }.map {
    AppRegistry.DagID(it.key, it.value as String)
  }

fun <T> SharedPreferences.getIDs(type: Class<T>): List<AppRegistry.DagID> =
  getIDs().filter { it.id.startsWith("$IPFSD_APP_ID_PREFIX/${type.name}") }


val Context.appRegistry: AppRegistry
  get() = AppRegistry.getInstance(Pair(ipfs, this))
*/

private val log = org.slf4j.LoggerFactory.getLogger(AppRegistry::class.java)



