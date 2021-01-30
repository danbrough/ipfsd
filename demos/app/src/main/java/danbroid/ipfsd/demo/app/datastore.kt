package danbroid.ipfsd.demo.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException


inline fun <reified T : IPFSApp> T.appID() = appPrefsKey(T::class.java.name, description.id)

fun appPrefsKey(type: String, id: String) = stringPreferencesKey("${IPFSApp.ID_PREFIX}/$type/$id")

class AppsDataStore(context: Context) {
  val dataStore: DataStore<Preferences> = context.createDataStore("ipfsd_apps")

  data class AppCID(val type: String, val id: String, val cid: String) {
    val prefsKey: Preferences.Key<String>
      get() = appPrefsKey(type, id)
  }

  val prefs: Flow<Preferences> = dataStore.data
    .catch { exception ->
      if (exception is IOException) {
        log.trace("no app prefs")
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


val Context.appsDataStore: AppsDataStore
  get() = AppsDataStore.getInstance(this)


private val log = org.slf4j.LoggerFactory.getLogger(AppsDataStore::class.java)
