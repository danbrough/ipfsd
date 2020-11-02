package danbroid.ipfsd.service.activities

import androidx.navigation.NavController
import danbroid.ipfsd.service.createIPFSDNavGraph
import danbroid.ipfsd.service.rootContent
import danbroid.util.menu.MenuActivity

class MainActivity : MenuActivity() {


  override fun createNavGraph(navController: NavController) =
    navController.createIPFSDNavGraph(this)


  override fun getRootMenu(menuID: String) = rootContent(this)


}

private val log = org.slf4j.LoggerFactory.getLogger(MainActivity::class.java)
