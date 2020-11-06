package danbroid.ipfsd.apidemo.content


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import danbroid.ipfs.api.API
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfsd.client.ipfsClient
import danbroid.ipfsd.client.model.ipfsModel
import danbroid.ipfsd.apidemo.R
import danbroid.ipfsd.apidemo.activities.activityInterface
import danbroid.ipfsd.apidemo.openBrowser
import danbroid.util.menu.MENU_TINT_DISABLED
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

val Fragment.executor: CallExecutor
  get() = ipfsModel.callExecutor

open class MenuTheme {
  companion object {

  }

  open val test = 1
}

@Suppress("NOTHING_TO_INLINE")
inline suspend fun Fragment.debug(msg: String?) {
  val message = msg ?: "null"
  log.debug(message)
  withContext(Dispatchers.Main) {
    activityInterface?.showSnackbar(message)
  }
}

fun rootContent(context: Context): MenuItemBuilder =
  context.rootMenu {
    id = URI_CONTENT_ROOT
    titleID = R.string.app_name

    onCreate = {
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
          log.debug("kitty: ${API.Basic.ls(dir_kitty).get(executor)}")
          false
        }
      }

      menu {
        title = "List kitty.danbrough.org"
        onClick = {
          log.debug("kitty: ${API.Basic.ls("/ipns/kitty.danbrough.org").get(executor)}")
          false
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
            false
          }
        }

        menu {
          title = "WebUI embedded browser"
          onClick = {
            findNavController().openBrowser(url_webui)
            false
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
      apiTest(API.Network.id(), "id")
      false
    }
  }



  menu {
    title = "Profile Apply"
    onClick = {
      apiTest(API.Config.Profile.apply("lowpower"))
      false
    }
  }

  menu {
    id = "$URI_CONTENT_ROOT/add_string"
    title = "Add string"

    onClick = {
      val msg = "Hello from the ipfs demo at ${Date()}.\n"
      log.trace("adding message: $msg")
      apiTest(API.Basic.add(msg, fileName = "ipfs_test_message.txt"), "added")
      false
    }
  }


  menu {
    title = "Bandwidth"
    onClick = {
      apiTest(API.Stats.bw(), "bandwidth")
      false
    }
  }




  pubSubCommands()
  repoCommands()
}


inline suspend fun <T> Fragment.apiTest(
  call: ApiCall<T>,
  prompt: String = "result"
): ApiCall.ApiResponse<T> {
  val response = call.get(executor)
  if (response.isSuccessful)
    debug("$prompt: ${response.value}")
  else
    log.error("Failed: ${response.responseCode}:${response.responseMessage}")

  return response
}