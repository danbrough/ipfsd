package danbroid.ipfsd.service.activities

import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.service.rootContent
import danbroid.util.menu.MenuActivity
import danbroid.util.menu.createMenuNavGraph

class MainActivity : MenuActivity() {


  override fun createNavGraph(navController: androidx.navigation.NavController) =
    navController.createMenuNavGraph(this, defaultMenuID = IPFSD.deep_link.ipfs_settings) {

    }

  override fun getRootMenu(menuID: String) = rootContent(this)


}

private val log = org.slf4j.LoggerFactory.getLogger(MainActivity::class.java)
