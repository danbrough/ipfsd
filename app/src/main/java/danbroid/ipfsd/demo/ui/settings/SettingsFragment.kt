package danbroid.ipfsd.demo.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import danbroid.ipfsd.demo.R

class SettingsFragment : Fragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ) = inflater.inflate(R.layout.fragment_settings,container,false)
}

private val log = org.slf4j.LoggerFactory.getLogger(SettingsFragment::class.java)
