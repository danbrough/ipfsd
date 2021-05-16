package danbroid.ipfsd.service.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.service.BuildConfig
import danbroid.ipfsd.service.IPFSService
import danbroid.ipfsd.service.R
import danbroid.logging.AndroidLog
import danbroid.logging.LogConfig


class DialogActivity : AppCompatActivity() {

  companion object {
    val log = LogConfig.let {
      val log = AndroidLog("IPFSD")
      it.DEBUG = BuildConfig.DEBUG
      it.COLOURED = BuildConfig.DEBUG
      it.GET_LOG = { log }
      log.debug("created log")
      log
    }
  }


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

private val log = danbroid.logging.getLog(DialogActivity::class)
