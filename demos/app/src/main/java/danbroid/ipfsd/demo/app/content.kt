package danbroid.ipfsd.demo.app


import android.content.Context
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import danbroid.ipfs.api.API
import danbroid.ipfsd.client.ipfsClient
import danbroid.ipfsd.client.model.ipfsModel
import danbroid.ipfsd.demo.app.shopping.ShoppingList
import danbroid.ipfsd.demo.app.shopping.shoppingListManager
import danbroid.util.menu.Icons.iconicsIcon
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu
import org.slf4j.LoggerFactory

fun rootContent(context: Context) = context.rootMenu<MenuItemBuilder> {

  id = URI_CONTENT
  titleID = R.string.app_name

  onCreate = {
    log.info("${URI_SHOPPING_LISTS}.onCreate()")
    requireContext().ipfsClient.connect()
  }

  menu {
    id = URI_SHOPPING_LISTS
    title = "Shopping Lists"
    icon = Theme.icons.shopping_cart


    menu {
      id = "$URI_SHOPPING_LISTS/new"
      titleID = R.string.title_new_shopping_list
      icon = Theme.icons.create_new
      onClick = {
        context.shoppingListManager.test()
        false
      }
    }

    menu {
      inlineChildren = true
      isBrowsable = true
      menu {
        title = "Test1"
      }
      menu {
        title = "Test2"
      }
    }

    menu {
      title = "Lists"
      onClick = {

        false
      }
    }

    menu {
      title = "Get ID"
      icon = iconicsIcon(FontAwesome.Icon.faw_accessible_icon)
      onClick = {
        API.Network.id().get(ipfsModel.callExecutor).also {
          log.warn("ID: $it")
        }
        false
      }
    }
  }
}

private val log = LoggerFactory.getLogger("danbroid.ipfsd.app")

