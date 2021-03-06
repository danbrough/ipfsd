package danbroid.ipfsd.demo.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import danbroid.ipfs.api.IPFS
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException


inline suspend fun <reified T : IPFSApp> T.appID(api: IPFS) =
  stringPreferencesKey("${IPFSApp.ID_PREFIX}/${T::class.java.name}/${description.value(api).id}")

class AppsDataStore(context: Context) {
  val dataStore: DataStore<Preferences> = context.prefsDatastore


  data class AppCID(val type: String, val id: String, val cid: String)

  val prefs: Flow<Preferences> = dataStore.data
    .catch { exception ->
      if (exception is IOException) {
        //log.trace("no app prefs")
        emit(emptyPreferences())
      } else {
        throw exception
      }
    }

  suspend inline fun <reified T : IPFSApp> getAppIDS() = getAppIDS(T::class.java.name)

  suspend fun getAppIDS(type: String): List<AppCID> =
    prefs.firstOrNull()?.asMap()?.filter { it.key.isAppKeyType(type) }?.map {
      AppCID(type, it.key.name.substringAfterLast('/'), it.value as String)
    }?.toList() ?: emptyList()


  companion object : SingletonHolder<AppsDataStore, Context>(::AppsDataStore)
}

fun Preferences.Key<*>.isAppKeyType(type: String) =
  name.startsWith("${IPFSApp.ID_PREFIX}/${type}")

private val Context.prefsDatastore by preferencesDataStore(name = "ipfsd_apps")

val Context.appsDataStore: AppsDataStore
  get() = AppsDataStore.getInstance(this)


//private val log = org.slf4j.LoggerFactory.getLogger(AppsDataStore::class.java)
