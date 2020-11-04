package danbroid.ipfsd.appdemo

import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
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
    val create_new = createIcon(GoogleMaterial.Icon.gmd_add)
    val shopping_cart = createIcon(GoogleMaterial.Icon.gmd_shopping_cart)
    val shopping_cart2 = createIcon(FontAwesome.Icon.faw_shopping_bag)

  }
}