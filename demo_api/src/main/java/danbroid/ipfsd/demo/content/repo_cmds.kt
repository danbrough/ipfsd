package danbroid.ipfsd.demo.content

import danbroid.ipfs.api.API
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu

fun MenuItemBuilder.repoCommands() = menu {
  title = "Repo Commands"

  menu {
    title = "Garbage Collect"
    onClick = {
      executor.exec(API.Repo.gc()) {
        debug("result: $it")
      }
    }
  }

  menu {
    title = "Stat"
    onClick = {
      executor.exec(API.Repo.stat(human = true)) {
        debug("err: $it")
      }
    }
  }

  menu {
    title = "Version"
    onClick = {
      executor.exec(API.Repo.version(quiet = false)) {
        debug("err: $it")
      }
    }
  }

  menu {
    title = "Verify"
    onClick = {
      executor.exec(API.Repo.verify()) {
        debug("err: $it")
      }
    }
  }

}