package danbroid.ipfsd.app.ui

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.navigation.findNavController
import danbroid.ipfsd.app.MainActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.slf4j.LoggerFactory
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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