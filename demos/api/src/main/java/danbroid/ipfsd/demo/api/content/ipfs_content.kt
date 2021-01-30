package danbroid.ipfsd.demo.api.content

import danbroid.ipfs.api.json
import danbroid.util.menu.MenuDSL
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.invalidateMenu
import danbroid.util.menu.menu


@MenuDSL
fun MenuItemBuilder.ipfsDir(
  path: String,
  block: (MenuItemBuilder.() -> Unit)? = null
): MenuItemBuilder =
  menu {
    id = "ipfsd:/$path"

    onClick = {
      children?.clear()
      api.basic.ls(path).json().Objects.forEach { obj ->
        obj.Links.forEach { link ->
          menu {
            id = "ipfsd://ipfs/${link.Hash}"
            title = link.Name
            subtitle = "${link.Hash} size:${link.Size}"
          }
        }
        invalidateMenu()
      }
      true
    }


    block?.invoke(this)
  }



