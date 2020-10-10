package danbroid.ipfsd.service

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import danbroid.ipfsd.R

class SettingsActivity : AppCompatActivity() {

  companion object {
    const val URI_PREFIX = "ipfsd_settings:/"
    const val URI_COMMAND_RESET_STATS = "$URI_PREFIX/reset_stats"

    val INTENT_RESET_STATS = Intent(Intent.ACTION_VIEW).setData(URI_COMMAND_RESET_STATS.toUri())

    @JvmStatic
    fun resetStats(context: Context) =
      context.startActivity(INTENT_RESET_STATS)

    @JvmStatic
    fun resetStatsPending(context: Context, flags: Int = PendingIntent.FLAG_CANCEL_CURRENT) =
      INTENT_RESET_STATS.let {
        PendingIntent.getActivity(context, 0, it, flags)
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    log.warn("intent: $intent")
    AlertDialog.Builder(this)
      //.setMessage(R.string.msg_confirm_reset_stats)
      .setMessage("Are you sure?????")
      .setTitle(R.string.lbl_reset_stats)
      .setNegativeButton(android.R.string.cancel, null)
      .setPositiveButton(android.R.string.ok) { _, _ ->
        IPFSService.resetStatsIntent(this)
      }.setOnDismissListener {
        finish()
      }.show()
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(SettingsActivity::class.java)
