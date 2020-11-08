package danbroid.ipfsd.demo.app.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import danbroid.ipfsd.demo.app.R

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.settings_prefs, rootKey)
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(SettingsFragment::class.java)
