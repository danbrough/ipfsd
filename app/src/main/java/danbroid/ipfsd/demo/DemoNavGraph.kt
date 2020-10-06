package danbroid.ipfsd.demo

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.fragment
import danbroid.ipfsd.demo.ui.TestFragment
import danbroid.ipfsd.demo.ui.settings.SettingsFragment
import danbroid.ipfsd.demo.ui.www.BrowserFragment
import danbroid.util.menu.navigation.UniqueIDS
import danbroid.util.menu.navigation.createMenuGraph

private const val pkg = "danbroid.ipfsd.demo"
const val ACTION_SETTINGS = "$pkg.ACTION_SETTINGS"

object DemoNavGraph : UniqueIDS {

  object dest {
    val browser_id = nextID()
    val test_id = nextID()
    val settings_id = nextID()
  }

  object deep_link {
    val rnz = nextID()
  }

  object action {
    val toBrowser = nextID()
    val toSettings = nextID()
  }

  object args {
    val url = "url"
  }
}

const val URL_BASE = "ipfsdemo:/"
const val URL_CONTENT_BASE = "$URL_BASE/content"

fun NavController.createDemoNavGraph() =

  createMenuGraph(deeplinkPrefix = URL_CONTENT_BASE) {

    fragment<BrowserFragment>(DemoNavGraph.dest.browser_id) {
      label = "WebUI"
      argument(DemoNavGraph.args.url) {
        defaultValue = "https://localhost:5001/webui/"
      }
    }

    fragment<TestFragment>(DemoNavGraph.dest.test_id) {
      label = "Test Fragment"
    }

    fragment<SettingsFragment>(DemoNavGraph.dest.settings_id) {
      label = "Settings"
      deepLink {
        action = ACTION_SETTINGS
        uriPattern = "$URL_BASE/settings"
      }
    }

    action(DemoNavGraph.action.toBrowser) {
      destinationId = DemoNavGraph.dest.browser_id
    }


    deepLink {
      uriPattern = "ipfsd://.*"
    }
  }


fun NavController.openBrowser(url: String) =
  navigate(DemoNavGraph.action.toBrowser, bundleOf(DemoNavGraph.args.url to url))


private val log = org.slf4j.LoggerFactory.getLogger(DemoNavGraph::class.java)
