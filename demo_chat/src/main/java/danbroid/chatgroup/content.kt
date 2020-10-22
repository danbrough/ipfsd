package danbroid.chatgroup


import android.content.ComponentName
import android.content.Intent
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import danbroid.chatgroup.model.chatModel
import danbroid.ipfsd.IPFSD
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu
import org.slf4j.LoggerFactory

val rootContent = rootMenu<MenuItemBuilder> {

  id = URI_SHOPPING_CONTENT
  titleID = R.string.app_name

  menu {
    id = URI_SHOPPING_LISTS
    title = "Shopping Lists"
    icon = Theme.icons.shopping_cart

    menu {
      id = "$URI_SHOPPING_LISTS/new"
      title = "Start Service"
      icon = Theme.icons.create_new
      onClick = {
        requireContext().startService(IPFSD.intent.service_start)
        //shoppingListModel().ipfsClient.connect()
      }
    }

    menu {
      title = "Start Service"
      icon = Theme.createIcon(FontAwesome.Icon.faw_address_book)
      onClick = {
        requireContext().startService(
          Intent().setComponent(
            ComponentName(
              "danbroid.ipfsd.service",
              "danbroid.ipfsd.service.IPFSService"
            )
          ).setAction("danbroid.ipfsd.service.ACTION_START")
        )
        //shoppingListModel().ipfsClient.connect()
      }
    }


    menu {
      title = "Bind Service"
      icon = Theme.createIcon(FontAwesome.Icon.faw_qrcode)
      onClick = {
        chatModel().ipfsClient.connect()

        //shoppingListModel().ipfsClient.connect()
      }
    }

    onCreate = { item, model ->
      log.error("${URI_SHOPPING_LISTS}.onCreate()")
    }
  }
}

private val log = LoggerFactory.getLogger("danbroid.chatgroup.shoppinglist")

