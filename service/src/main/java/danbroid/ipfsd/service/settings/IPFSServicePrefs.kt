package danbroid.ipfsd.service.settings

import android.content.Context
import danbroid.util.prefs.Prefs

const val IPFSD_PREFS_FILE = "ipfs_service"

class IPFSServicePrefs(context: Context) : Prefs(context, IPFSD_PREFS_FILE) {

  var dataIn: Long by Pref(0L)
  var dataOut: Long by Pref(0L)
  var timeout: Long by Pref(60000 * 2)

}