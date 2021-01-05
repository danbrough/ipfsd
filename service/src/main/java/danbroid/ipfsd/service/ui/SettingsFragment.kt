package danbroid.ipfsd.service.ui

import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsSize
import com.mikepenz.iconics.dsl.ExperimentalIconicsDSL
import com.mikepenz.iconics.dsl.iconicsDrawable
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import danbroid.ipfsd.service.R

class SettingsFragment : PreferenceFragmentCompat() {

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.settings, rootKey)
  }

  @ExperimentalIconicsDSL
  override fun setPreferenceScreen(preferenceScreen: PreferenceScreen) {

    preferenceScreen.findPreference<PreferenceCategory>(getString(R.string.key_general))?.apply {
      //icon = requireContext().iconicsDrawable(GoogleMaterial.Icon.gmd_3d_rotation) {
      icon = requireContext().iconicsDrawable(MaterialDesignIconic.Icon.gmi_3d_rotation) {
        size = IconicsSize.dp(24)
        color = IconicsColor.colorRes(R.color.colorPrimary)
      }

      preferenceScreen.findPreference<CheckBoxPreference>(getString(R.string.key_test))?.apply {
        //icon = requireContext().iconicsDrawable(GoogleMaterial.Icon.gmd_switch_camera) {
        icon = requireContext().iconicsDrawable(MaterialDesignIconic.Icon.gmi_camera_switch) {
          size = IconicsSize.dp(24)
          color = IconicsColor.colorRes(R.color.colorPrimary)
        }


        /*requireContext().iconicsDrawable(GoogleMaterial.Icon.gmd_settings) {
          backgroundColor = IconicsColor.colorInt(Color.BLUE)
        }*/

        super.setPreferenceScreen(preferenceScreen)
      }
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(SettingsFragment::class.java)

