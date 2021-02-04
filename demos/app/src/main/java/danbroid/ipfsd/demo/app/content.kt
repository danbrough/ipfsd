package danbroid.ipfsd.demo.app


import android.content.Context
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import danbroid.ipfs.api.json
import danbroid.ipfsd.client.androidIPFS
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

  //context.ipfsClient.connect()

  onCreate = {
    log.info("${URI_SHOPPING_LISTS}.onCreate()")


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
        log.debug("androidIPFS: ${requireContext().androidIPFS}")
        val listCount = context.appsDataStore.getAppIDS<ShoppingList>().size + 1
        context.shoppingListManager.createList("List_$listCount")
      }
    }
    menu {
      title = "Test"
      //icon = iconicsIcon(GoogleMaterial.Icon.gmd_play_arrow)
      //  icon = iconicsIcon(MaterialDesignIconic.Icon.gmi_play)
      onClick = {

        ipfsd.Ipfsd.sayHello()


        consumed = true
      }
    }


    menu {
      title = "Test Hello2"
      onClick = {
        // Toast.makeText(requireContext(), ipfsd.Ipfsd.greetings("Dan"), Toast.LENGTH_SHORT).show()
      }
    }



    menu {
      title = "Lists"
      onClick = {
        context.shoppingListManager.list()
      }
    }

    menu {
      title = "Get ID"
      icon = iconicsIcon(MaterialDesignIconic.Icon.gmi_folder_person)
      onClick = {
        context.androidIPFS.network.id().json().also {
          log.info("ID: $it")
        }
        consumed = true
      }
    }
  }
}

private object Content

private val log = LoggerFactory.getLogger(Content::class.java)

