package danbroid.ipfsd.service.settings

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import danbroid.ipfsd.service.IPFSService
import danbroid.ipfsd.service.R
import java.lang.IllegalArgumentException

class SettingsActivity : AppCompatActivity() {

  companion object {
    const val URI_PREFIX = "ipfsd://settings"
    val URI_RESET_STATS_PROMPT = "$URI_PREFIX/reset_stats"

    val INTENT_RESET_STATS_PROMPT =
      Intent(Intent.ACTION_VIEW).setData(URI_RESET_STATS_PROMPT.toUri())

    @JvmStatic
    fun resetStatsPrompt(context: Context) =
      context.startActivity(INTENT_RESET_STATS_PROMPT)

    @JvmStatic
    fun resetStatsPending(context: Context, flags: Int = PendingIntent.FLAG_CANCEL_CURRENT) =
      INTENT_RESET_STATS_PROMPT.let {
        PendingIntent.getActivity(context, 0, it, flags)
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    log.debug("intent: $intent")

    when (intent?.data?.toString()) {
      URI_RESET_STATS_PROMPT -> resetStatsConfirm()
      else -> {
        log.warn("no uri specified")
      }
    }
  }

  fun resetStatsConfirm() {
    AlertDialog.Builder(this)
      .setMessage(R.string.msg_confirm_reset_stats)
      .setTitle(R.string.lbl_reset_stats)
      .setNegativeButton(android.R.string.cancel, null)
      .setPositiveButton(android.R.string.ok) { _, _ ->
        IPFSService.resetStats(this)
      }.setOnDismissListener {
        finish()
      }.show()
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(SettingsActivity::class.java)
