package danbroid.ipfsd.service

import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import danbroid.ipfs.api.API
import danbroid.ipfs.api.ApiCall
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.client.model.ipfsClient
import danbroid.util.menu.Icons
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu
import org.slf4j.LoggerFactory

internal val rootMenu: MenuItemBuilder = rootMenu {
  id = IPFSD.deep_link.ipfs_settings
  titleID = R.string.app_name

  menu {
    title = "Start Service"
    icon = Icons.iconicsIcon(GoogleMaterial.Icon.gmd_playlist_play)
    onClick = {
      ipfsClient.callExecutor.exec(API.Network.id()) {
        log.info("ID: $it")
      }
    }
  }

  menu {
    title = "Stop Service"
    icon = Icons.iconicsIcon(GoogleMaterial.Icon.gmd_local_play)
  }

  menu {
    title = "Menu 2"
  }
}

private val log = LoggerFactory.getLogger("danbroid.ipfsd.service")