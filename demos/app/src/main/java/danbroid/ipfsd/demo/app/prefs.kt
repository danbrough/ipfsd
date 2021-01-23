package danbroid.ipfsd.demo.app

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore


inline fun <reified T : IPFSApp> appKey(id: String) =
  stringPreferencesKey("$IPFSD_APP_ID_PREFIX/${T::class.java.name}/$id")

fun Context.appPrefs() = createDataStore("ipfsd_apps")