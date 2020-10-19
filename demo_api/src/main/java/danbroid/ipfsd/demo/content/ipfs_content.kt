package danbroid.ipfsd.demo.content

import androidx.appcompat.app.AppCompatActivity
import danbroid.ipfs.api.API
import danbroid.ipfsd.demo.model.IPFSClientModel
import danbroid.ipfsd.demo.model.ipfsClient
import danbroid.util.menu.MenuDSL
import danbroid.util.menu.MenuItem
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.model.menuViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@MenuDSL
fun MenuItemBuilder.ipfsDir(
  path: String,
  block: (MenuItemBuilder.() -> Unit)? = null
): MenuItemBuilder =
  menu {
    id = "ipfsd:/$path"

    onClick = { callback ->
      ipfsClient.callExecutor.exec(API.Basic.ls(path)) {
        val items = mutableListOf<MenuItem>()
        it.getOrNull()?.objects?.forEach { file ->
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
        log.warn("updaing children with $items")
        menuViewModel().also {
          it.updateChildren(items)
        }
        callback.invoke(true)
      }
    }

    block?.invoke(this)
  }



