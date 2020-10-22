package danbroid.chatgroup

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.fragment.fragment
import danbroid.chatgroup.ui.SettingsFragment
import danbroid.util.menu.createMenuNavGraph
import danbroid.util.misc.UniqueIDS

const val URI_SHOPPING_PREFIX = "${BuildConfig.URI_SCHEME}:/"
const val URI_SHOPPING_CONTENT = "$URI_SHOPPING_PREFIX/content"
const val URI_SHOPPING_LISTS = "$URI_SHOPPING_CONTENT/lists"

object ShoppingListNavGraph : UniqueIDS {

  object dest {
    val settings = nextID()
  }

  object deep_link {
    const val settings = "$URI_SHOPPING_CONTENT/settings"
  }
}

fun NavController.createShoppingListNavGraph(context: Context) =
  createMenuNavGraph(context, defaultMenuID = URI_SHOPPING_CONTENT) {

    fragment<SettingsFragment>(ShoppingListNavGraph.dest.settings) {
      deepLink(ShoppingListNavGraph.deep_link.settings)
    }

  }


