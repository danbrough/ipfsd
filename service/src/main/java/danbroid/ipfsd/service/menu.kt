package danbroid.ipfsd.service

import android.content.Context
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import danbroid.ipfs.api.API
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.client.model.ipfsModel
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
      API.Network.id().get(ipfsModel.callExecutor).also {
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
}


private val log = LoggerFactory.getLogger("danbroid.ipfsd.service")