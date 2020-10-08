package danbroid.ipfsd.service

import android.content.Context
import android.content.SharedPreferences
import danbroid.util.prefs.HasPrefs
import danbroid.util.prefs.LongPref


class IPFSServicePrefs(val context: Context) : HasPrefs {
  companion object {
    const val PREFS_NAME = "ipfs_service"
  }

  enum class Key {
    DATA_IN,
    DATA_OUT
  }

  var dataIn: Long by LongPref(Key.DATA_IN, 0)
  var dataOut: Long by LongPref(Key.DATA_OUT, 0)

  override val prefs: SharedPreferences
    get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}