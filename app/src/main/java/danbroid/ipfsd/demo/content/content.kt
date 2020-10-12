package danbroid.ipfsd.demo.content


import android.content.Intent
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import danbroid.ipfs.api.API
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfsd.demo.R
import danbroid.ipfsd.demo.URL_CONTENT_BASE
import danbroid.ipfsd.demo.activities.activityInterface
import danbroid.ipfsd.demo.model.ipfsClient
import danbroid.ipfsd.demo.openBrowser
import danbroid.ipfsd.service.IPFSService
import danbroid.ipfsd.service.settings.SettingsActivity
import danbroid.util.menu.MenuActionContext
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.io.EOFException
import java.util.*

const val URI_CONTENT_ROOT = "ipfsdemo://content"
const val DIR_XCCD = "/ipfs/QmdmQXB2mzChmMeKY47C43LxUdg1NDJ5MWcKMKxDu7RgQm"
const val dir_kitty = "/ipfs/QmaknW7EzautwWKE1q4rpR4tPnP1XuxMKGr8KiyRZKqC5T"

val log = LoggerFactory.getLogger("danbroid.ipfsd.demo.content")

private val MenuActionContext.executor: CallExecutor
  get() = fragment!!.ipfsClient.callExecutor

private inline fun MenuActionContext.debug(msg: String?) {
  val message = msg ?: "null"
  log.debug(message)
  fragment?.lifecycleScope?.launch(Dispatchers.Main) {
    fragment.activityInterface?.showSnackbar(message)
  }
}

val rootContent: MenuItemBuilder by lazy {

  rootMenu<MenuItemBuilder> {
    id = URI_CONTENT_ROOT
    titleID = R.string.app_name

    menu {
      title = "Get ID"
      onClick = {
        executor.exec(API.Network.id()) {
          debug("id: $it")

        }
      }
    }

    repoMenu()

    menu {
      title = "Stop"
      onClick = {
        /*     context.startService(
               Intent(
                 context,
                 IPFSService::class.java
               ).setAction(IPFSService.ACTION_STOP)
             )*/
        IPFSService.stopService(context)
      }
    }

    menu {
      title = "Reset Stats"
      onClick = {
        SettingsActivity.resetStatsPrompt(context)
      }
    }

    menu {
      title = "Profile Apply"
      onClick = {
        executor.exec(API.Config.Profile.apply("lowpower")) {
          debug("result: $it")
        }
      }
    }

    menu {
      title = "Add string"
      var hashID: String? = null
      isBrowsable = true

      val menuPublish = menu {
        title = "Publish:"
        onClick = {
          log.error("publishing: $id")
          executor.exec(API.Name.publish(hashID!!)) { response ->
            debug("Published: $id to ${response?.value}")
          }
        }
      }

      onClick = { callback ->
        val msg = "Hello from the ipfs demo at ${Date()}.\n"
        log.trace("adding message: $msg")

        executor.exec(API.add(msg, fileName = "ipfs_test_message.txt")) { result ->
          debug("added $msg -> $result")
          menuPublish.title = "Publish $hashID"
          hashID = result?.hash
          fragment?.lifecycleScope?.launch(Dispatchers.Main) {
            callback.invoke(true)
          }
        }
      }
    }


    menu {
      title = "Bandwidth"
      onClick = {
        executor.exec(API.Stats.bw()) {
          debug("bandwidth: $it")
        }
      }
    }

    menu {
      title = "PubSub Publish"
      onClick = {
        executor.exec(
          API.PubSub.publish(
            "poiqwe098123",
            "Hello from the IPFS app at ${Date()}\n "
          )
        ) {
          debug("RESULT: $it")
        }
      }
    }

    menu {
      title = "Pubsub Test Subscribe"
      onClick = {

        executor.exec(API.PubSub.subscribe("poiqwe098123", discover = true)) {
          val msg = it?.dataString
          debug(msg)
        }

      }
    }

    menu {
      title = "List kitty"
      onClick = {
        executor.exec(API.ls(dir_kitty)) {
          debug("GOT KITTY: $it")
        }
      }
    }

    menu {
      title = "List kitty.danbrough.org"
      onClick = {
        executor.exec(API.ls("/ipns/kitty.danbrough.org")) {
          debug("result: $it")
        }
      }
    }

    ipfsDir(DIR_XCCD) {
      title = "DIR XCCD"
      isBrowsable = true
    }


    val url_webui = "http://localhost:5001/webui/"

    menu {
      title = "WebUI"

      menu {
        title = "WebUI external browser"

        onClick = {
          context.startActivity(Intent(Intent.ACTION_VIEW).also {
            it.data = Uri.parse(url_webui)
          })
        }
      }

      menu {
        title = "WebUI embedded browser"
        onClick = {
          navController?.openBrowser(url_webui)
        }
      }
    }
  }

}

fun MenuItemBuilder.repoMenu() =
  menu {
    id = "$URL_CONTENT_BASE/repo"
    title = "Repo"

    menu {
      title = "Garbage Collect"
      onClick = {
        API.Repo.gc().onResult {
          log.debug("msg: $it")
        }.onError {
          log.warn("GOT ERROR: ${it.javaClass}")
          if (it is EOFException || it.cause is EOFException) {
            log.debug("no response")
          } else {
            log.error(it.message, it)
          }
        }.also {
          executor.exec(it)
        }
      }
    }

    menu {
      title = "Stat"
      onClick = {
        executor.exec(API.Repo.stat(human = true)) {
          debug("err: $it")
        }
      }
    }

    menu {
      title = "Version"
      onClick = {
        executor.exec(API.Repo.version(quiet = false)) {
          debug("err: $it")
        }
      }
    }

    menu {
      title = "Verify"
      onClick = {
        executor.exec(API.Repo.verify()) {
          debug("err: $it")
        }
      }
    }


  }
