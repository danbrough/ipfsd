package danbroid.ipfsd.demo.app

import android.content.Context
import danbroid.ipfs.api.CID_EMPTY_OBJECT
import danbroid.util.prefs.Prefs


class AppPrefs(context: Context) : Prefs(context, PREFS_FILE) {

  var shoppingListApp: String by Pref(CID_EMPTY_OBJECT)

  companion object {
    const val PREFS_FILE = "app_prefs"
  }
}

val Context.appPrefs: AppPrefs
  get() = AppPrefs(this)