package danbroid.ipfsd.demo.api.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import danbroid.ipfsd.demo.api.R


class SettingsFragment : PreferenceFragmentCompat() {

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.settings_preferences, rootKey)
  }
}

private val log = danbroid.logging.getLog(SettingsFragment::class)
