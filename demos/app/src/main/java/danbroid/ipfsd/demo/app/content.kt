package danbroid.ipfsd.demo.app


import android.content.Context
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import danbroid.ipfs.api.json
import danbroid.ipfsd.client.androidIPFS
import danbroid.ipfsd.demo.app.shopping.ShoppingList
import danbroid.ipfsd.demo.app.shopping.shoppingListManager
import danbroid.util.menu.Icons.iconicsIcon
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.invalidateMenu
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu
import org.slf4j.LoggerFactory


fun rootContent(context: Context) = context.rootMenu<MenuItemBuilder> {

  id = URI_CONTENT
  titleID = R.string.app_name

  //context.ipfsClient.connect()

  onCreate = {
    log.warn("${URI_SHOPPING_LISTS}.onCreate()")
  }

  context.androidIPFS

  menu {
    id = URI_SHOPPING_LISTS
    title = "Shopping Lists"
    icon = Theme.icons.shopping_cart


    menu {
      id = "$URI_SHOPPING_LISTS/new"
      titleID = R.string.title_new_shopping_list
      icon = Theme.icons.create_new
      onClick = {
        context.shoppingListManager.createList(fragment)
        invalidateMenu()
      }
    }

    menu {
      inlineChildren = true
      onCreate = {
        children?.clear()
        context.shoppingListManager.appsDataStore.getAppIDS<ShoppingList>().forEach {
          menu {
            id = it.prefsKey.name
            title = it.prefsKey.name
            onLongClick = {
              context.shoppingListManager.deleteList(id) {
                children?.remove(this@menu)
                invalidateMenu()
              }
            }
          }
        }
      }
    }

    menu {
      title = "Test"
      onClick = {
        context.shoppingListManager.test()
      }
    }


    menu {
      title = "List"
      onClick = {
        context.shoppingListManager.list()
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
      title = "Get ID"
      icon = iconicsIcon(MaterialDesignIconic.Icon.gmi_folder_person)
      onClick = {
        context.androidIPFS.network.id().json().also {
          log.info("ID: $it")
        }
      }
    }
  }
}

private object Content

private val log = LoggerFactory.getLogger(Content::class.java)

