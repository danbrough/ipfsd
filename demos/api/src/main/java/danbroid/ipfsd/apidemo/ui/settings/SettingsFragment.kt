package danbroid.ipfsd.apidemo.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import danbroid.ipfsd.apidemo.R


class SettingsFragment : PreferenceFragmentCompat() {

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.settings_preferences, rootKey)
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(SettingsFragment::class.java)
