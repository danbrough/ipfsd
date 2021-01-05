package danbroid.ipfsd.demo.app

import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import danbroid.util.menu.DrawableProvider

object Theme {
  fun createIcon(icon: IIcon, builder: IconicsDrawable.() -> Unit = {}): DrawableProvider =
    {
      IconicsDrawable(this, icon).apply {
        paddingPx = 4
        builder()
      }
    }

  object icons {
    val create_new = createIcon(MaterialDesignIconic.Icon.gmi_view_list_alt)

    val shopping_cart = createIcon(MaterialDesignIconic.Icon.gmi_shopping_cart)

  }
}