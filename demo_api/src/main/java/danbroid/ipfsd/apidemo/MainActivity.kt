package danbroid.ipfsd.apidemo

import android.view.Menu
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import danbroid.ipfsd.apidemo.activities.ActivityInterface
import danbroid.ipfsd.apidemo.content.rootContent
import danbroid.util.menu.MenuActivity
import danbroid.util.menu.ui.MenuListAdapter


class MainActivity : MenuActivity(R.layout.activity_main), ActivityInterface {


  override fun createNavGraph(navController: NavController) = navController.createDemoNavGraph(this)

  override fun getRootMenu(menuID: String) = rootContent(this)

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

private val log = org.slf4j.LoggerFactory.getLogger(MainActivity::class.java)

