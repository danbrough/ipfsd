package danbroid.ipfsd.appdemo

import android.content.Context
import danbroid.util.prefs.Prefs


class AppPrefs(context: Context) : Prefs(context, PREFS_FILE) {

  var shoppingListApp: String? by Pref(null)

  companion object {
    const val PREFS_FILE = "app_prefs"
  }
}

val Context.appPrefs: AppPrefs
  get() = AppPrefs(this)