package danbroid.shoppinglist


import android.content.ComponentName
import android.content.Intent
import danbroid.shoppinglist.model.shoppingListModel
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
      title = "Create New"
      icon = Theme.icons.create_new
      onClick = {
        requireContext().startService(
          Intent().setComponent(
            ComponentName(
              "danbroid.ipfsd.service",
              "danbroid.ipfsd.service.IPFSService"
            )
          )
        )
        //shoppingListModel().ipfsClient.connect()
      }
    }

    onCreate = { item, model ->
      log.error("${URI_SHOPPING_LISTS}.onCreate()")
    }
  }
}

private val log = LoggerFactory.getLogger("danbroid.shoppinglist")

