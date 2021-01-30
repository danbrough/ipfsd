package danbroid.ipfsd.ui

import android.R
import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun Activity.snackBar(
  msg: CharSequence,
  @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT
) {
  withContext(Dispatchers.Main) {
    findViewById<View>(R.id.content).also {
      Snackbar.make(it, msg, duration).show()
    }
  }
}

