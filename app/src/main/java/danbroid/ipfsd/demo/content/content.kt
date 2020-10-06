package danbroid.ipfsd.demo.content


import android.content.Intent
import android.net.Uri
import danbroid.ipfs.api.API
import danbroid.ipfsd.demo.R
import danbroid.ipfsd.demo.activities.activityInterface
import danbroid.ipfsd.demo.model.ipfsClient
import danbroid.ipfsd.demo.openBrowser
import danbroid.ipfsd.service.IPFSService
import danbroid.util.menu.MenuActionContext
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.*

const val URI_CONTENT_ROOT = "ipfsdemo://content"
const val DIR_XCCD = "/ipfs/QmdmQXB2mzChmMeKY47C43LxUdg1NDJ5MWcKMKxDu7RgQm"
const val dir_kitty = "/ipfs/QmaknW7EzautwWKE1q4rpR4tPnP1XuxMKGr8KiyRZKqC5T"

val log = LoggerFactory.getLogger("danbroid.ipfsd.demo.content")

private val MenuActionContext.api: API
  get() = fragment!!.ipfsClient.api


val rootContent: MenuItemBuilder by lazy {

  rootMenu<MenuItemBuilder> {
    id = URI_CONTENT_ROOT
    titleID = R.string.app_name

    menu {
      title = "Get ID"
      onClick = {

        /*   api.version().asFlow().first()?.also {
             log.debug("version: $it")
           }*/

        api.id().asFlow().first().also {
          log.debug("id: $it")
        }

      }
    }

    menu {
      title = "Stop"
      onClick = {
        context.startService(
          Intent(
            context,
            IPFSService::class.java
          ).setAction(IPFSService.ACTION_STOP)
        )
      }
    }

    menu {
      title = "Add string"
      var hashID: String? = null
      isBrowsable = true

      onClick = { callback ->
        val msg = "Hello from the ipfs demo at ${Date()}.\n"
        log.trace("adding message: $msg")
        val parentID = this@menu.id
        api.add(msg, fileName = "ipfs_test_message.txt").exec { result ->
          log.debug("added $msg -> $result")
          hashID = result!!.hash
          withContext(Dispatchers.Main) {
            fragment?.activityInterface?.showSnackbar("Added: $msg")

            menu {
              id = "${parentID}/publish"
              title = "Publish:"
              subtitle = hashID!!
              onClick = {
                log.error("publishing: $hashID")
                api.name.publish(hashID!!).exec {
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
      title = "GC"
      onClick = {
        System.gc()
        api.stats.bw().exec {
          log.debug("bandwidth: $it")
        }
      }
    }

    menu {
      title = "PubSub Publish"
      onClick = {
        api.pubSub.publish("poiqwe098123", "Hello from the IPFS app at ${Date()}\n ").exec {
          log.debug("RESULT: $it")
        }
      }
    }

    menu {
      title = "Pubsub Test Subscribe"
      onClick = {

        api.pubSub.subscribe("poiqwe098123", discover = true).exec {
          log.debug("message: from:${it?.fromID} seqNo:${it?.sequenceID} msg:${it?.dataString}")
        }

      }
    }

    menu {
      title = "List kitty"
      onClick = {
        api.ls(dir_kitty).exec {
          log.debug("GOT KITTY: $it")
        }
      }
    }

    menu {
      title = "List kitty.danbrough.org"
      onClick = {
        api.ls("/ipns/kitty.danbrough.org").exec()
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


