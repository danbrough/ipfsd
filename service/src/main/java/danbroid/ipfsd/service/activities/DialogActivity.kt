package danbroid.ipfsd.service.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.service.IPFSService
import danbroid.ipfsd.service.R


class DialogActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    log.debug("intent: $intent")

    processIntent(intent)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    processIntent(intent)
  }

  fun processIntent(intent: Intent) {
    when (intent.data?.toString()) {
      IPFSD.deep_link.reset_stats_prompt -> resetStatsConfirm()
      else -> {
        log.warn("unhandled uri ${intent.data}")
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

private val log = org.slf4j.LoggerFactory.getLogger(DialogActivity::class.java)
