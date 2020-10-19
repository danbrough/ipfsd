package danbroid.shoppinglist


import danbroid.shoppinglist.model.shoppingModel
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.menu
import danbroid.util.menu.rootMenu

val rootContent = rootMenu<MenuItemBuilder> {

  id = URI_SHOPPING_CONTENT
  titleID = R.string.app_name

  menu {
    id = URI_SHOPPING_LISTS
    title = "Shopping Lists"
    icon = Theme.icons.shopping_cart

    menu {
      id = "$URI_SHOPPING_LISTS/new"
      title = "Create New"
      icon = Theme.icons.create_new
    }

    onCreate = { item, model ->
      shoppingModel()
    }
  }
}

