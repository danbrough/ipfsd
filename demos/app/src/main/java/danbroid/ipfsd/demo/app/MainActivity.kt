package danbroid.ipfsd.demo.app

import androidx.navigation.NavController
import danbroid.logging.AndroidLog
import danbroid.logging.LogConfig
import danbroid.util.menu.MenuActivity

class MainActivity : MenuActivity() {

  companion object {
    val log = LogConfig.let {
      it.COLOURED = BuildConfig.DEBUG
      it.DETAILED = true
      val log = AndroidLog("IPFSD")
      it.GET_LOG = { log }
      log
    }
  }

  val rootContent by lazy {
    rootContent(this)
  }

  override fun createNavGraph(navController: NavController) =
    navController.createShoppingListNavGraph(this)

  override fun getRootMenu() = rootContent

}

