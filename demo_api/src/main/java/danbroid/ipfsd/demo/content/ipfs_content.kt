package danbroid.ipfsd.demo.content

import danbroid.ipfs.api.API
import danbroid.ipfsd.client.model.ipfsModel
import danbroid.util.menu.MenuDSL
import danbroid.util.menu.MenuItem
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.model.menuViewModel


@MenuDSL
fun MenuItemBuilder.ipfsDir(
  path: String,
  block: (MenuItemBuilder.() -> Unit)? = null
): MenuItemBuilder =
  menu {
    id = "ipfsd:/$path"

    onClick = { callback ->
      ipfsModel.callExecutor.exec(API.Basic.ls(path)) {
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



