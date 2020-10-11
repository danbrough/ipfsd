package danbroid.ipfsd.demo.content


import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import danbroid.ipfs.api.API
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfsd.demo.R
import danbroid.ipfsd.demo.URL_CONTENT_BASE
import danbroid.ipfsd.demo.activities.activityInterface
import danbroid.ipfsd.demo.model.ipfsClient
import danbroid.ipfsd.demo.openBrowser
import danbroid.ipfsd.service.IPFSService
import danbroid.ipfsd.service.SettingsActivity
import danbroid.util.menu.MenuActionContext
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.*

const val URI_CONTENT_ROOT = "ipfsdemo://content"
const val DIR_XCCD = "/ipfs/QmdmQXB2mzChmMeKY47C43LxUdg1NDJ5MWcKMKxDu7RgQm"
const val dir_kitty = "/ipfs/QmaknW7EzautwWKE1q4rpR4tPnP1XuxMKGr8KiyRZKqC5T"

val log = LoggerFactory.getLogger("danbroid.ipfsd.demo.content")

private val MenuActionContext.executor: CallExecutor
  get() = fragment!!.ipfsClient.callExecutor

private inline suspend fun MenuActionContext.debug(msg: String?) {
  val message = msg ?: "null"
  withContext(Dispatchers.Main) {
    log.debug(message)
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
        executor.exec(API.id()) {
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
      id = SettingsActivity.URI_COMMAND_RESET_STATS
      title = "Reset Stats"
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
      var hashID: String?
      isBrowsable = true

      onClick = { callback ->
        val msg = "Hello from the ipfs demo at ${Date()}.\n"
        log.trace("adding message: $msg")
        val parentID = this@menu.id
        executor.exec(API.add(msg, fileName = "ipfs_test_message.txt")) { result ->
          debug("added $msg -> $result")
          hashID = result.hash
          withContext(Dispatchers.Main) {
            fragment?.activityInterface?.showSnackbar("Added: $msg")

            menu {
              id = "${parentID}/publish"
              title = "Publish:"
              subtitle = hashID!!
              onClick = {
                log.error("publishing: $hashID")
                executor.exec(API.Name.publish(hashID!!)) {
                  fragment?.activityInterface?.showSnackbar("Published: $hashID to ${it?.value}")
                  log.debug("result: $it")
                }
              }
            }
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
          val msg = it?.dataString ?: "null"
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
        executor.exec(API.Repo.gc(streamErrors = true, quiet = false)) {
          log.debug("err: $it")
        }
      }
    }
  }
