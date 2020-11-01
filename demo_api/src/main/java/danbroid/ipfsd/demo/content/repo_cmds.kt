package danbroid.ipfsd.demo.content

import danbroid.ipfs.api.API
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu


fun MenuItemBuilder.repoCommands() = menu {
  title = "Repo Commands"

  menu {
    title = "Garbage Collect"
    onClick = {
      apiTest(API.Repo.gc())
      false
    }
  }

  menu {
    title = "Stat"
    onClick = {
      apiTest(API.Repo.stat(human = true))
      false
    }
  }

  menu {
    title = "Version"
    onClick = {
      apiTest(API.Repo.version(quiet = false))
      false
    }
  }

  menu {
    title = "Verify"
    onClick = {
      apiTest(API.Repo.verify())
      false
    }
  }

}