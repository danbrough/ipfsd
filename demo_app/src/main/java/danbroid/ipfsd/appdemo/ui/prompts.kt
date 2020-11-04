package danbroid.ipfsd.appdemo.ui

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import kotlin.coroutines.resume

object Prompts {

  suspend fun getInput(context: Context, @StringRes titleID: Int, message: CharSequence): String =
    withContext(Dispatchers.Main) {
      suspendCancellableCoroutine { cont ->
        AlertDialog.Builder(context).apply {
          setTitle(titleID)
          setMessage(message)
          setPositiveButton(android.R.string.ok) { _, _ ->
            cont.resume("OK")
          }
          setNegativeButton(android.R.string.cancel) { _, _ ->
            cont.resume("CANCEL")
          }

        }.create().also { dlg ->
          cont.invokeOnCancellation {
            dlg.cancel()
          }
          dlg.show()
        }
      }
    }


}

private val log = LoggerFactory.getLogger(Prompts::class.java)