package danbroid.ipfsd.service.settings

import android.content.Context
import danbroid.util.prefs.Prefs


class IPFSServicePrefs(context: Context) : Prefs(context, PREFS_FILE) {
  companion object {
    const val PREFS_FILE = "ipfs_service"
  }

  var dataIn: Long by Pref(0L)
  var dataOut: Long by Pref(0L)
  var timeout: Long by Pref(60000 * 2)

}