package danbroid.ipfsd.demo.content

import androidx.appcompat.app.AppCompatActivity
import danbroid.ipfs.api.API
import danbroid.ipfsd.demo.model.IPFSClientModel
import danbroid.ipfsd.demo.model.ipfsClient
import danbroid.util.menu.MenuDSL
import danbroid.util.menu.MenuItem
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@MenuDSL
fun MenuItemBuilder.ipfsDir(
  path: String,
  block: (MenuItemBuilder.() -> Unit)? = null
): MenuItemBuilder =
  menu {
    isBrowsable = true
    id = "ipfsd:/$path"

    log.error("CREATED BUILDER: $id")

/*    provides = {
      log.trace("provides: $it")
      if (it.startsWith("ipfsd:/"))
        ipfsDir(it.substring(6)).also {
          log.error("RETURNING $it")
        }
      else null
    }*/

    liveChildren = { context, id, oldItem ->
      val items = mutableListOf<MenuItem>()
      val ipfsClient: IPFSClientModel
      log.warn("LIVE CHILDREN")
      runBlocking(Dispatchers.Main) {
        ipfsClient = (context as AppCompatActivity).ipfsClient

        log.debug("calling ls on $path")
        ipfsClient.callExecutor.exec(API.ls(path)) { result ->
          log.debug("result: $result")
          result.getOrNull()?.objects?.forEach { file ->
            file.links.forEach { link ->
              items.add(
                MenuItem(
                  "ipfsd://ipfs/${link.hash}",
                  link.name,
                  "${link.hash} size:${link.size}"
                )
              )
            }
          }
        }

        log.warn("returning ${items.size} items")
        items
      }
    }
    block?.invoke(this)
  }




