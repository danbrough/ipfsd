package danbroid.ipfsd.demo.activities

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import danbroid.util.menu.ui.menulist.MenuListAdapter


interface ActivityInterface {

  fun onItemClicked(holder: MenuListAdapter.MenuViewHolder)

  fun open(uri: String)

  fun setToolbarTitle(title: CharSequence)

  fun showSnackbar(
    msg: CharSequence,
    length: Int = Snackbar.LENGTH_SHORT,
    actionLabel: String? = null,
    action: (() -> Unit)? = null
  )


  fun openBrowser(url: String)
}


val Fragment?.activityInterface: ActivityInterface?
  get() = (this!!.activity as ActivityInterface)