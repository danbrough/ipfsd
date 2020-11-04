package danbroid.ipfsd.app

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.fragment.fragment
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.app.R
import danbroid.ipfsd.service.ui.SettingsFragment
import danbroid.util.menu.createMenuNavGraph
import danbroid.util.misc.UniqueIDS

object IPFSDNavGraph : UniqueIDS {

  object dest {
    val home = nextID()
    val settings = nextID()
  }

  object deep_link {
    val settings = IPFSD.deep_link.ipfsd_settings
    val home = IPFSD.deep_link.ipfsd_home
  }

}

fun NavController.createIPFSDNavGraph(context: Context) =
  createMenuNavGraph(context, defaultMenuID = IPFSDNavGraph.deep_link.home) {
    fragment<SettingsFragment>(IPFSDNavGraph.dest.settings) {
      label = context.getString(R.string.lbl_settings)
      deepLink(IPFSDNavGraph.deep_link.settings)
    }
  }
