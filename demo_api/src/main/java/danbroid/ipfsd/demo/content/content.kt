package danbroid.ipfsd.demo.content


import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import danbroid.ipfs.api.API
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfsd.client.ipfsClient
import danbroid.ipfsd.client.model.ipfsModel
import danbroid.ipfsd.demo.R
import danbroid.ipfsd.demo.activities.activityInterface
import danbroid.ipfsd.demo.openBrowser
import danbroid.util.menu.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.util.*

const val URI_CONTENT_ROOT = "ipfsdemo://content"
const val DIR_XCCD = "/ipfs/QmdmQXB2mzChmMeKY47C43LxUdg1NDJ5MWcKMKxDu7RgQm"
const val dir_kitty = "/ipfs/QmaknW7EzautwWKE1q4rpR4tPnP1XuxMKGr8KiyRZKqC5T"

val log = LoggerFactory.getLogger("danbroid.ipfsd.demo.content")

val Fragment.executor: CallExecutor
  get() = ipfsModel.callExecutor

open class MenuTheme {
  companion object {

  }

  open val test = 1
}

inline fun Fragment.debug(msg: String?) {
  val message = msg ?: "null"
  log.debug(message)
  lifecycleScope.launch(Dispatchers.Main) {
    activityInterface?.showSnackbar(message)
  }
}

val rootContent: MenuItemBuilder =
  rootMenu {
    id = URI_CONTENT_ROOT
    titleID = R.string.app_name

    onCreate = { _, _ ->
      //auto connect to the IPFS service
      requireContext().ipfsClient.connect()
    }

    commands()


    menu {
      title = "Stop Service"
      imageURI = "https://ipfs.io/ipfs/QmU4zgfoWCR6UdeveKbzff1JZeE5fGFnT573YepsJJFA2i"
      onClick = {
        //TODO IPFSService.stopService(requireContext())
        TODO("Fix this")
      }
    }

    menu {
      title = "Reset Stats"
      imageID = R.drawable.ipfs_logo_128
      tintRes = MENU_TINT_DISABLED
      onClick = {
        //SettingsActivity.resetStatsPrompt(requireContext())
        TODO("Fix this")
      }
    }


    menu {
      title = "Misc"
      menu {
        title = "List kitty"
        onClick = {
          executor.exec(API.Basic.ls(dir_kitty)) {
            debug("GOT KITTY: $it")
          }
        }

      }

      menu {
        title = "List kitty.danbrough.org"
        onClick = {
          executor.exec(API.Basic.ls("/ipns/kitty.danbrough.org")) {
            debug("result: $it")
          }
        }
      }

      ipfsDir(DIR_XCCD) {
        title = "DIR XCCD"
      }
    }

    menu {

      title = "WebUI"

      val url_webui = "http://localhost:5001/webui/"

      menu {
        title = "WebUI"

        menu {
          title = "WebUI external browser"

          onClick = {
            requireContext().startActivity(Intent(Intent.ACTION_VIEW).also {
              it.data = Uri.parse(url_webui)
            })
          }
        }

        menu {
          title = "WebUI embedded browser"
          onClick = {
            findNavController().openBrowser(url_webui)
          }
        }
      }
    }
  }


fun MenuItemBuilder.commands() = menu {
  title = "Commands"


  menu {
    title = "Get ID"
    onClick = {
      executor.exec(API.Network.id()) {
        debug("id: $it")
      }
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
    id = "$URI_CONTENT_ROOT/add_string"
    title = "Add string"
    val parent = this

    onClick = { callback ->
      val msg = "Hello from the ipfs demo at ${Date()}.\n"
      log.trace("adding message: $msg")
      executor.exec(API.Basic.add(msg, fileName = "ipfs_test_message.txt")) { result ->
        debug("added $msg -> $result")
        val hashID = result.getOrThrow().hash
        log.debug("hashID: $hashID")
        lifecycleScope.launch(Dispatchers.Main) {
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
    title = "Repo Stat Test"
    onClick = {
      //TODO fragment.ipfsClient?.sendMessage(IPFSMessage.REPO_STAT)
    }
  }



  pubSubCommands()
  repoCommands()
}