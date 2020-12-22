package danbroid.ipfsd.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import danbroid.ipfs.api.flow
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.client.model.ipfs
import danbroid.ipfsd.ui.snackBar
import danbroid.util.menu.Icons.iconicsIcon
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu
import org.slf4j.LoggerFactory

internal fun rootContent(context: Context): MenuItemBuilder = context.rootMenu {
  id = IPFSD.deep_link.ipfsd_home
  titleID = R.string.app_name

  menu {
    title = "ID"
    icon = iconicsIcon(GoogleMaterial.Icon.gmd_play_arrow)
    onClick = {
      ipfs.network.id().flow().collect {
        log.warn("GOT RESULT: $it")
        requireActivity().snackBar("result: $it")
      }
      false
    }
  }


  menu {
    id = IPFSDNavGraph.deep_link.settings
    titleID = R.string.lbl_settings
    icon = iconicsIcon(GoogleMaterial.Icon.gmd_settings)
  }

  menu {
    title = "Test1"
    onClick = {
      context.startService(
        Intent().setComponent(
          ComponentName(
            "danbroid.ipfsd.service",
            "danbroid.ipfsd.service.IPFSService"
          )
        )
      )
      false
    }
  }

  menu {
    title = "Test2"
    onClick = {
      context.startService(Intent(context, IPFSService::class.java))
      false
    }
  }
}


private val log = LoggerFactory.getLogger("danbroid.ipfsd.service")