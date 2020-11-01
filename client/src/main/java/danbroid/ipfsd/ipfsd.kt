package danbroid.ipfsd

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import danbroid.ipfsd.client.BuildConfig

//const ipfs = new ipfsClient({ host: 'ipfs.infura.io', port: '5001', protocol: 'https' });

class IPFSD {

  companion object {
    const val URI_SERVICE_PREFIX = "${BuildConfig.ipfsd_scheme}://service"  // ipfsd:/
    const val SERVICE_PKG = "danbroid.ipfsd.service"
    const val SERVICE_CLASS = "$SERVICE_PKG.IPFSService"
    // const val SERVICE_DIALOG_ACTIVITY = "activities.DialogActivity"

  }

  class action {
    companion object {
      const val service_start = "$SERVICE_PKG.ACTION_START"
    }
  }

  class intent {
    companion object {
      @JvmField
      val service_intent =
        Intent().setComponent(ComponentName(SERVICE_PKG, SERVICE_CLASS))


      @JvmField
      val reset_stats_prompt =
        Intent(Intent.ACTION_VIEW).setData(deep_link.reset_stats_prompt.toUri())


      @JvmStatic
      fun resetStatsPromptPending(
        context: Context,
        flags: Int = PendingIntent.FLAG_CANCEL_CURRENT
      ) =
        reset_stats_prompt.let {
          PendingIntent.getActivity(context, 0, it, flags)
        }
    }
  }

  class deep_link {
    companion object {

      const val ipfs_settings = "$URI_SERVICE_PREFIX/settings"
      const val reset_stats_prompt = "$URI_SERVICE_PREFIX/reset_stats"
    }
  }
}

/*
  companion object {
    const val URI_PREFIX = "ipfsd://settings"
    val URI_RESET_STATS_PROMPT = "$URI_PREFIX/reset_stats"
    val URI_START = "ipfsd://start"

    val INTENT_RESET_STATS_PROMPT =
      Intent(Intent.ACTION_VIEW).setData(URI_RESET_STATS_PROMPT.toUri())

    @JvmStatic
    fun resetStatsPrompt(context: Context) =
      context.startActivity(INTENT_RESET_STATS_PROMPT)


  }
 */