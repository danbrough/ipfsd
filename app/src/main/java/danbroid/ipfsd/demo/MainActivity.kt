package danbroid.ipfsd.demo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import danbroid.ipfsd.demo.activities.ActivityInterface
import danbroid.ipfsd.demo.content.rootContent
import danbroid.util.menu.ui.MenuImplementation
import danbroid.util.menu.ui.menulist.MenuListAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ActivityInterface {

  init {
    MenuImplementation.rootContent = {
      rootContent
    }
  }

  protected val navHostFragment: NavHostFragment
    get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

  override fun onCreate(savedInstanceState: Bundle?) {
    log.info("onCreate()")
    super.onCreate(savedInstanceState)


    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    navHostFragment.navController.also { controller ->
      controller.createDemoNavGraph()
      setupActionBarWithNavController(this@MainActivity, controller)
    }
/*
    navController.graph.addAll(navController.navInflater.inflate(R.navigation.ipfsd_navigation))


    setupActionBarWithNavController(navController)*/
    log.warn("intent $intent")
    log.warn("data:${intent?.data}")
    intent?.extras?.also {
      it.keySet()?.forEach {
        log.debug("KEY: $it ${intent?.extras?.get(it)}")
      }
    }
  }


  override fun onNewIntent(intent: Intent?) {
    log.warn("onNewIntent!() $intent")
    log.warn("data:${intent?.data}")
    log.warn("extras:${intent?.extras}")

    if (intent?.action == Intent.ACTION_VIEW) {
      if (navHostFragment.navController.handleDeepLink(intent)) return
    }
    super.onNewIntent(intent)
  }

  override fun onSupportNavigateUp() =
    navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()


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
    log.info("onItemCLicked not implemented")
  }

  override fun open(uri: String) {
    log.info("open() $uri  NOT IMPLEMENTED")
  }

  override fun setToolbarTitle(title: CharSequence) {
    supportActionBar?.setTitle(title)
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


