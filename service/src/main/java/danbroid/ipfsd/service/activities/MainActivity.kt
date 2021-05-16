package danbroid.ipfsd.service.activities

import androidx.navigation.NavController
import danbroid.ipfsd.service.BuildConfig
import danbroid.ipfsd.service.createIPFSDNavGraph
import danbroid.ipfsd.service.rootContent
import danbroid.logging.AndroidLog
import danbroid.logging.LogConfig
import danbroid.util.menu.MenuActivity

class MainActivity : MenuActivity() {

  companion object {
    val log = LogConfig.let {
      val log = AndroidLog("IPFSD")
      it.DEBUG = BuildConfig.DEBUG
      it.COLOURED = BuildConfig.DEBUG
      it.GET_LOG = { log }
      log.debug("created log")
      log
    }
  }

  val rootContent by lazy {
    rootContent(this)
  }

  override fun createNavGraph(navController: NavController) =
    navController.createIPFSDNavGraph(this)


  override fun getRootMenu() = rootContent


}


