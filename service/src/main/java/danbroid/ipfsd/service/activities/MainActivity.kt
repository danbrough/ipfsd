package danbroid.ipfsd.service.activities

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.service.IPFSNavGraph
import danbroid.ipfsd.service.IPFSService
import danbroid.ipfsd.service.R
import danbroid.ipfsd.service.rootMenu
import danbroid.util.menu.MenuActivity
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.createMenuNavGraph

class MainActivity : MenuActivity() {

  companion object {
    const val URI_PREFIX = "ipfsd://settings"
    val URI_RESET_STATS_PROMPT = "$URI_PREFIX/reset_stats"
    val URI_START = "ipfsd://start"

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

  override fun createNavGraph(navController: androidx.navigation.NavController) =
    navController.createMenuNavGraph(
      this,
      defaultMenuID = IPFSD.deep_link.ipfs_settings
    ) {

    }

  override fun getRootMenu(menuID: String) = rootMenu

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
      URI_RESET_STATS_PROMPT -> resetStatsConfirm()
      URI_START -> {
        log.info("should start service")
        title = "Starting"
        startService(Intent(this, IPFSService::class.java))
        finish()
      }
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

private val log = org.slf4j.LoggerFactory.getLogger(MainActivity::class.java)
