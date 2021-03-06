package danbroid.ipfsd.demo.api

import android.view.Menu
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import danbroid.ipfsd.demo.api.activities.ActivityInterface
import danbroid.ipfsd.demo.api.content.rootContent
import danbroid.logging.AndroidLog
import danbroid.logging.LogConfig
import danbroid.util.menu.MenuActivity
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.ui.MenuListAdapter


class MainActivity : MenuActivity(R.layout.activity_main), ActivityInterface {

  companion object {
    val log = LogConfig.let {
      val log = AndroidLog("IPFSD")
      it.DEBUG = BuildConfig.DEBUG
      it.COLOURED = BuildConfig.DEBUG
      it.GET_LOG = { log }
      log
    }
  }

  override fun createNavGraph(navController: NavController) = navController.createDemoNavGraph(this)

  private val rootContent by lazy {
    rootContent(this)
  }

  override fun getRootMenu(): MenuItemBuilder = rootContent

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }


  override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
      R.id.action_settings -> {
        log.debug("navingating to settings")
        navHostFragment.navController.navigate(DemoNavGraph.action.toSettings)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onItemClicked(holder: MenuListAdapter.MenuViewHolder) {
    TODO("Not yet implemented")
  }

  override fun open(uri: String) {
    log.info("open() $uri  NOT IMPLEMENTED")
  }


  override fun showSnackbar(
    msg: CharSequence,
    length: Int,
    actionLabel: String?,
    action: (() -> Unit)?
  ) =
    Snackbar.make(navHostFragment.requireView(), msg, length).setAction(actionLabel) {
      action?.invoke()
    }.show()


  override fun openBrowser(url: String) {
    TODO("Not yet implemented")
  }

}



