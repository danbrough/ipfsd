package danbroid.ipfsd.appdemo

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.fragment.fragment
import danbroid.ipfsd.appdemo.ui.SettingsFragment
import danbroid.util.menu.createMenuNavGraph
import danbroid.util.misc.UniqueIDS

const val URI_DEMO_PREFIX = "${BuildConfig.IPFSD_SCHEME}://demo/"
const val URI_CONTENT = "$URI_DEMO_PREFIX/content"
const val URI_SHOPPING_LISTS = "$URI_DEMO_PREFIX/lists"

object ShoppingListNavGraph : UniqueIDS {

  object dest {
    val settings = nextID()
  }

  object deep_link {
    const val settings = "$URI_CONTENT/settings"
  }
}

fun NavController.createShoppingListNavGraph(context: Context) =
  createMenuNavGraph(context, defaultMenuID = URI_CONTENT) {

    fragment<SettingsFragment>(ShoppingListNavGraph.dest.settings) {
      deepLink(ShoppingListNavGraph.deep_link.settings)
    }

  }


