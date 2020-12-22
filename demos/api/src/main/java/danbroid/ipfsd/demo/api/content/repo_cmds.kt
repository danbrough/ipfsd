package danbroid.ipfsd.demo.api.content

import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu


fun MenuItemBuilder.repoCommands() = menu {
  title = "Repo Commands"

  menu {
    title = "Garbage Collect"
    onClick = {
      //TODO apiTest(api.repo.gc())
      false
    }
  }

  menu {
    title = "Stat"
    onClick = {
      //TODO apiTest(api.repo.stat(human = true))
      false
    }
  }

  menu {
    title = "Version"
    onClick = {
      //TODO apiTest(api.repo.version(quiet = false))
      false
    }
  }

  menu {
    title = "Verify"
    onClick = {
      //TODO apiTest(api.repo.verify())
      false
    }
  }

}