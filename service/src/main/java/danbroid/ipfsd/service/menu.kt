package danbroid.ipfsd.service

import android.content.Context
import danbroid.ipfs.api.flow
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.client.androidIPFS
import danbroid.ipfsd.ui.snackBar
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu
import kotlinx.coroutines.flow.firstOrNull
import org.slf4j.LoggerFactory

internal fun rootContent(context: Context): MenuItemBuilder = context.rootMenu {
  id = IPFSD.deep_link.ipfsd_home
  titleID = R.string.app_name


  menu {
    title = "ID"
    //icon = iconicsIcon(GoogleMaterial.Icon.gmd_play_arrow)
    //  icon = iconicsIcon(MaterialDesignIconic.Icon.gmi_play)
    onClick = {
      requireContext().androidIPFS.network.id().flow().firstOrNull()?.also {
        requireActivity().snackBar("result: $it")
      }
    }
  }


  menu {
    id = IPFSDNavGraph.deep_link.settings
    titleID = R.string.lbl_settings
    //icon = iconicsIcon(GoogleMaterial.Icon.gmd_settings)
    // icon = iconicsIcon(MaterialDesignIconic.Icon.gmi_settings)
  }

  menu {
    title = "Start Service"
    onClick = {
      IPFSService.startService(context)
      /*     context.startService(
             Intent().setComponent(
               ComponentName(
                 IPFSService::class.java.`package`!!.name,
                 IPFSService::class.java.name
               )
             )
           )*/
    }
  }

  menu {
    title = "Stop Service"
    onClick = {
      IPFSService.stopService(context)
      /* context.stopService(
         Intent().setComponent(
           ComponentName(
             IPFSService::class.java.`package`!!.name,
             IPFSService::class.java.name
           )
         )
       )*/

    }
  }
}


private val log = LoggerFactory.getLogger("danbroid.ipfsd.service")