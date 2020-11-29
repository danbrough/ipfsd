package danbroid.ipfsd.demo.api.content

import danbroid.ipfsd.client.model.ipfsModel
import danbroid.util.menu.MenuDSL
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

    onClick = {
      children?.clear()
      api.basic.ls(path).get().value.objects.forEach { obj ->
        obj.links.forEach { link ->
          menu {
            id = "ipfsd://ipfs/${link.hash}"
            title = link.name
            subtitle = "${link.hash} size:${link.size}"
          }
        }
        menuViewModel().invalidate(this)
      }
      true
    }


    block?.invoke(this)
  }



