package danbroid.ipfsd.app.activities

import androidx.navigation.NavController
import danbroid.ipfsd.app.createIPFSDNavGraph
import danbroid.ipfsd.app.rootContent
import danbroid.util.menu.MenuActivity

class MainActivity : MenuActivity() {


  override fun createNavGraph(navController: NavController) =
    navController.createIPFSDNavGraph(this)


  override fun getRootMenu(menuID: String) = rootContent(this)


}

private val log = org.slf4j.LoggerFactory.getLogger(MainActivity::class.java)
