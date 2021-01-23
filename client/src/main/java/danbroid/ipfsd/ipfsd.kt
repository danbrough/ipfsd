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
    const val URI_ACTION_PREFIX = "${BuildConfig.ipfsd_scheme}://action"
    const val SERVICE_PKG = "danbroid.ipfsd.service"
    const val SERVICE_CLASS = "$SERVICE_PKG.IPFSService"
  }

  class action {
    companion object {
      const val service_start = "$SERVICE_PKG.ACTION_START"
      const val service_stop = "$SERVICE_PKG.ACTION_STOP"

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
      const val ipfsd_home = URI_SERVICE_PREFIX
      const val ipfsd_settings = "$URI_SERVICE_PREFIX/settings"
      const val reset_stats_prompt = "$URI_ACTION_PREFIX/reset_stats"
    }
  }
}

