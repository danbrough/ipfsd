package danbroid.ipfsd.app


import android.app.AlertDialog
import androidx.navigation.fragment.findNavController
import danbroid.ipfsd.app.shopping.ShoppingListManager
import danbroid.ipfsd.app.shopping.shoppingListManager
import danbroid.ipfsd.app.ui.Prompts
import danbroid.ipfsd.client.ipfsClient
import danbroid.ipfsd.client.model.ipfsModel
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.navigateToHome
import danbroid.util.menu.rootMenu
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume

val rootContent = rootMenu<MenuItemBuilder> {

  id = URI_CONTENT
  titleID = R.string.app_name

  onCreate = { _, _ ->
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
        runCatching {
          requireContext().shoppingListManager.createList()
        }.exceptionOrNull()?.also {
          log.error("FAILED: $it")
        }
      }
    }
  }
}

private val log = LoggerFactory.getLogger("danbroid.ipfsd.app.shoppinglist")

