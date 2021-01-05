package danbroid.ipfsd.demo.app


import android.content.Context
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import danbroid.ipfs.api.json
import danbroid.ipfsd.client.ipfsClient
import danbroid.ipfsd.client.model.ipfs
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
      icon = iconicsIcon(MaterialDesignIconic.Icon.gmi_folder_person)
      onClick = {
        ipfs.network.id().json().also {
          log.info("ID: $it")
        }
        false
      }
    }
  }
}

private object Content

private val log = LoggerFactory.getLogger(Content::class.java)

