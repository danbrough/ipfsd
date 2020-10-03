package danbroid.ipfsd.demo.content


import android.content.Intent
import android.net.Uri
import danbroid.ipfs.api.API
import danbroid.ipfsd.demo.R
import danbroid.ipfsd.demo.activities.activityInterface
import danbroid.ipfsd.demo.ipfsClient
import danbroid.util.menu.MenuActionContext
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.*

const val URI_CONTENT_ROOT = "ipfsdemo://content"
private const val DIR_XCCD = "QmdmQXB2mzChmMeKY47C43LxUdg1NDJ5MWcKMKxDu7RgQm"

private val log = LoggerFactory.getLogger("danbroid.ipfsd.demo.content")

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
      title = "Add string"
      onClick = {
        val msg = "Hello from the ipfs demo at ${Date()}.\n"
        api.add(msg, fileName = "ipfs_test_message.txt").exec { result ->
          log.debug("added $msg -> $result")
          withContext(Dispatchers.Main) {
            fragment?.activityInterface?.showSnackbar("Added: $msg")
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
          false
        }
      }
    }

    menu {
      title = "Pubsub Test Subscribe"
      onClick = {

        api.pubSub.subscribe("poiqwe098123").asFlow().collect {
          log.debug("message: from:${it?.fromID} seqNo:${it?.sequenceID} msg:${it?.dataString}")
        }

      }
    }

    menu {
      title = "List kitty"
      onClick = {
        api.ls("QmaknW7EzautwWKE1q4rpR4tPnP1XuxMKGr8KiyRZKqC5T").exec()
      }
    }

    menu {
      title = "List kitty.danbrough.org"
      onClick = {
        api.ls("/ipns/kitty.danbrough.org").exec()
      }
    }

    menu {
      title = "List XCCD"
      onClick = {
        api.ls(DIR_XCCD, stream = false).exec {
          log.info("result: $it")
          true
        }
      }
    }

    menu {
      title = "WebUI"
      id = "http://localhost:5001/webui/"
      onClick = {
        context.startActivity(Intent(Intent.ACTION_VIEW).also {
          it.data = Uri.parse(id)
        })
        /*(fragment?.requireActivity() as MainActivity).findNavController(R.id.nav_host_fragment)
          .also {
            it.navigate(BrowserFragmentDirections.actionGlobalNavigationBrowser(id!!))
          }*/
      }
    }


/*    menu {
      title = "Pubsub Subscribe"
      onClick = {
        withContext(Dispatchers.IO) {
          val api = LocalIPFS()
          log.debug("bandwidth: ${api.stats.bandWidth()}")

          api.pubSub.sub("poiqwe098123") {
            log.info("DATA: $it")
          }
        }
      }
    }*/


/*    menu {
      title = "Config 1"
      onClick = {
        runCommand { ipfs ->
          val config = ipfs.config.toString(1)
          File(
            context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS),
            "libipfs.so"
          ).setExecutable(true)

          File(
            context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS),
            "config.txt"
          )
            .writeText(config)
          log.debug("config: $config")
          val swarm = ipfs.config.getJSONObject("Swarm")
          log.info("swarm: ${swarm.toString(1)}")

        }
      }
    }
    menu {
      title = "Get"
      onClick = {
        runCommand { ipfs ->
          ipfs.newRequest("cat")
            .withArgument("/ipns/QmfBFfV72Rw9Vm9wWr2HFARydNWt4ap2xvU8j7Uw2xzzDR").send()
            .also {
              log.debug("received: ${it.decodeToString()}")
              withContext(Dispatchers.Main) {
                Toast.makeText(context, it.decodeToString(), Toast.LENGTH_SHORT)
                  .show()
              }
            }
        }
      }
    }*/

/*    menu {
      title = "Add"
      onClick = {
        runCommand { ipfs ->
          val msg = "Hello world at ${Date()}"
          val f = File(context.filesDir, "message.txt")
          f.writeText(msg)

          ipfs.newRequest("files/write").withArgument("/data/1.txt")
            .withOption("create", true)
            .withOption("parents", true)


//            .withHeader("Content-type", "text/plain")
//            .withHeader("Content-Disposition","form-data; name=\"file\"; filename=\"message.txt\"")

            .withHeader(
              "Content-Type",
              "multipart/form-data; boundary=-------BOUNDARY"
            )
            .withBody(
              """---------BOUNDARY
Content-Disposition: form-data; name="file"
Content-Type: text/plain

$msg

---------BOUNDARY--"""
            )

            .send().also {
              log.debug("received: ${it?.decodeToString()}")
              withContext(Dispatchers.Main) {
                Toast.makeText(context, it?.decodeToString(), Toast.LENGTH_SHORT)
                  .show()
              }
            }
        }
      }
    }*/

/*
    var dataID: String? = null
    menu {
      title = "Stat"
      onClick = {
        runCommand { ipfs ->

          ipfs.newRequest("files/stat").withArgument("/data")
            .send().also {
              it ?: return@also
              val data = JSONObject(it.decodeToString())
              dataID = data.getString("Hash")
              log.debug("hash: $dataID")
              log.debug("received: ${it.decodeToString()}")
              withContext(Dispatchers.Main) {
                Toast.makeText(context, it.decodeToString(), Toast.LENGTH_SHORT)
                  .show()
              }
            }
        }
      }
    }
*/
/*
    menu {
      title = "Publish"
      onClick = {
        runCommand { ipfs ->

          dataID ?: return@runCommand
          ipfs.newRequest("name/publish").withArgument(dataID!!)
            .send().also {
              log.debug("received: ${it.decodeToString()}")
              withContext(Dispatchers.Main) {
                Toast.makeText(context, it.decodeToString(), Toast.LENGTH_SHORT)
                  .show()
              }
            }
        }
      }
    }*/
/*   "/dnsaddr/h1.danbrough.org/ipfs/QmfBFfV72Rw9Vm9wWr2HFARydNWt4ap2xvU8j7Uw2xzzDR",
                        "/dnsaddr/h1.danbrough.org/ipfs/QmSUVX7in38z9DkJUZJd8Ko9SCoktLW8YV91RqPDjEtVwT",
                        "/dnsaddr/h1.danbrough.org/ipfs/QmSaby9qHtxo2nSMrs7QrQmVEU5k2tLsFYzpVFKJZoxTaL"*/

/*
    menu {
      title = "Connect"
      onClick = {

        runCommand { ipfs ->
          ipfs.newRequest("swarm/connect")
            .withArgument("/ip4/216.189.156.39/tcp/4001/p2p/QmfBFfV72Rw9Vm9wWr2HFARydNWt4ap2xvU8j7Uw2xzzDR")
            .send()?.also {
              log.debug("received: ${it.decodeToString()}")
              withContext(Dispatchers.Main) {
                Toast.makeText(context, it.decodeToString(), Toast.LENGTH_SHORT)
                  .show()
              }
            } ?: log.error("cmd returned null")
        }

        runCommand { ipfs ->
          ipfs.newRequest("swarm/connect")
            .withArgument("/ip4/192.168.1.2/tcp/4001/p2p/QmSUVX7in38z9DkJUZJd8Ko9SCoktLW8YV91RqPDjEtVwT")
            .send()?.also {
              log.debug("received: ${it.decodeToString()}")
              withContext(Dispatchers.Main) {
                Toast.makeText(context, it.decodeToString(), Toast.LENGTH_SHORT)
                  .show()
              }
            } ?: log.error("cmd returned null")
        }

      }
    }
*/

/*    menu {
      title = "Swarm"
      onClick = {

        runCommand { ipfs ->
          ipfs.newRequest("swarm/addrs")
            .send()?.also {
              log.debug("received: ${it.decodeToString()}")
              withContext(Dispatchers.Main) {
                Toast.makeText(context, it.decodeToString(), Toast.LENGTH_SHORT)
                  .show()
              }
            } ?: log.error("cmd returned null")
        }
      }
    }*/
/*
    menu {
      title = "Subscribe"
      onClick = {


        runCommand { ipfs ->
          ipfs.newRequest("pubsub/sub").withArgument("poiqwe098123")
            .send()?.also {

              log.debug("received: ${it.decodeToString()}")
              withContext(Dispatchers.Main) {
                Toast.makeText(context, it.decodeToString(), Toast.LENGTH_SHORT).show()
              }
            } ?: log.error("cmd returned null")
        }
      }
    }*/

/*    menu {
      title = "Publish"
      onClick = {

        runCommand { ipfs ->
          ipfs.newRequest("pubsub/pub").withArgument("poiqwe098123")
            .withArgument("Hello at ${Date()}!")
            .send()?.also {
              log.debug("received: ${it.decodeToString()}")
              withContext(Dispatchers.Main) {
                Toast.makeText(context, it.decodeToString(), Toast.LENGTH_SHORT)
                  .show()
              }private val httpClient = OkHttpClient().newBuilder().build()
            } ?: log.error("cmd returned null")
        }
      }
    }

    menu {
      title = "Reconfigure"
      onClick = {

        runCommand { ipfs ->
          httpClient.newCall(
            Request.Builder().url("http://192.168.1.2/config.json").build()
          )
            .execute().body?.also {
              log.debug("got config")
              File(ipfs.repoAbsolutePath, "config").writeText(it.string())


              ipfs.restart()
            }
        }
      }
    }*/


  }

}


