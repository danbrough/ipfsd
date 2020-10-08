package danbroid.ipfsd.demo

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.fragment.fragment
import danbroid.ipfsd.demo.ui.QRCodeFragment
import danbroid.ipfsd.demo.ui.TestFragment
import danbroid.ipfsd.demo.ui.settings.SettingsFragment
import danbroid.ipfsd.demo.ui.www.BrowserFragment
import danbroid.util.menu.navigation.UniqueIDS
import danbroid.util.menu.navigation.createMenuGraph

private const val pkg = "danbroid.ipfsd.demo"
const val ACTION_SETTINGS = "$pkg.ACTION_SETTINGS"
const val ACTION_QR_CODE = "$pkg.ACTION_QR_CODE"


object DemoNavGraph : UniqueIDS {

  object dest {
    val browser_id = nextID()
    val test_id = nextID()
    val settings_id = nextID()
    val qrcode_id = nextID()
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
    val data = "data"
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

    fragment<QRCodeFragment>(DemoNavGraph.dest.qrcode_id) {
      label = "QRCode"
      deepLink {
        action = ACTION_QR_CODE
        uriPattern = "$URL_BASE/qrcode/{data}"
      }
      argument(DemoNavGraph.args.data) {
        type = NavType.StringType
      }
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

    action(DemoNavGraph.action.toSettings) {
      destinationId = DemoNavGraph.dest.settings_id
    }

    deepLink {
      uriPattern = "$URL_BASE/.*"
    }
  }


fun NavController.openBrowser(url: String) =
  navigate(DemoNavGraph.action.toBrowser, bundleOf(DemoNavGraph.args.url to url))


private val log = org.slf4j.LoggerFactory.getLogger(DemoNavGraph::class.java)
