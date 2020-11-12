package danbroid.ipfsd.demo.app

import androidx.navigation.NavController
import danbroid.util.menu.MenuActivity

class MainActivity : MenuActivity() {

  val rootContent by lazy {
    rootContent(this)
  }

  override fun createNavGraph(navController: NavController) =
    navController.createShoppingListNavGraph(this)

  override fun getRootMenu() = rootContent

}

private val log = org.slf4j.LoggerFactory.getLogger(MainActivity::class.java)
