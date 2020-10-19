package danbroid.shoppinglist

import androidx.navigation.NavController
import danbroid.util.menu.MenuActivity

class MainActivity : MenuActivity() {

  override fun createNavGraph(navController: NavController) =
    navController.createShoppingListNavGraph(this)

  override fun getRootMenu(menuID: String) = rootContent
}

private val log = org.slf4j.LoggerFactory.getLogger(MainActivity::class.java)
