package danbroid.ipfsd.demo.api.content


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.json
import danbroid.ipfsd.client.ipfsApi
import danbroid.ipfsd.client.ipfsServiceClient
import danbroid.ipfsd.client.model.ipfsModel
import danbroid.ipfsd.demo.api.R
import danbroid.ipfsd.demo.api.activities.activityInterface
import danbroid.ipfsd.demo.api.openBrowser
import danbroid.util.menu.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.time.ExperimentalTime

const val URI_CONTENT_ROOT = "ipfsdemo://content"
const val DIR_XCCD = "/ipfs/QmdmQXB2mzChmMeKY47C43LxUdg1NDJ5MWcKMKxDu7RgQm"
const val dir_kitty = "/ipfs/QmaknW7EzautwWKE1q4rpR4tPnP1XuxMKGr8KiyRZKqC5T"

private object Content

val log = danbroid.logging.getLog(Content::class)

val MenuItemClickContext.api: IPFS
  get() = requireContext().ipfsApi

val Fragment.api: IPFS
  get() = ipfsModel.api

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
      withContext(Dispatchers.Main) {
        requireContext().ipfsServiceClient.connect()
      }
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
          log.debug("kitty: ${api.basic.ls(dir_kitty).json()}")
          false
        }
      }

      menu {
        title = "List kitty.danbrough.org"
        onClick = {
          log.debug("kitty: ${api.basic.ls("/ipns/kitty.danbrough.org").json()}")
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


@ExperimentalTime
fun MenuItemBuilder.commands() = menu {
  title = "Commands"


  menu {
    title = "Get ID"
    onClick = {
      showSnackBar("ID: ${api.network.id().json()}")
    }
  }



  menu {
    title = "Profile Apply"
    onClick = {
      //TODO apiTest(api.config.profile.apply("lowpower"))

      log.warn("todo: implement api.config.profile.apply(\"lowpower\")")

    }
  }

  menu {
    id = "$URI_CONTENT_ROOT/add_string"
    title = "Add string"

    onClick = {
      val msg = "Hello from the ipfs demo at ${Date()}.\n"
      log.trace("adding message: $msg")
      api.basic.add(msg, fileName = "ipfs_test_message.txt").json().also {
        showSnackBar("created file: $it")
      }
    }
  }


  menu {
    title = "Bandwidth"
    onClick = {
      // apiTest(api.stats.bw(), "bandwidth")

    }
  }


  pubSubCommands()
  repoCommands()
}


fun MenuItemClickContext.showSnackBar(msg: String) {
  log.info(msg)
  fragment.activityInterface?.showSnackbar(msg)
}

/*
inline suspend fun <T> Fragment.apiTest(
  call: ApiCall<T>,
  prompt: String = "result"
): ApiCall.ApiResponse<T> {
  val response = call.get()
  if (response.isSuccessful)
    debug("$prompt: ${response.value}")
  else
    log.error("Failed: ${response.responseCode}:${response.responseMessage}")

  return response
}*/
