package danbroid.ipfsd

import android.content.ComponentName
import android.content.Intent
import danbroid.ipfsd.client.BuildConfig


class IPFSD {

  companion object {
    const val URI_SERVICE_PREFIX = "${BuildConfig.ipfsd_scheme}:/"  // ipfsd:/
    const val SERVICE_PKG = "danbroid.ipfsd.service"
    const val SERVICE_CLASS = "IPFSService"
  }

  class action {
    companion object {
      @JvmField
      val service_start = "$SERVICE_PKG.ACTION_START"
    }
  }

  class intent {
    companion object {
      @JvmField
      val service_intent =
        Intent().setComponent(ComponentName(SERVICE_PKG, "$SERVICE_PKG.$SERVICE_CLASS"))

      @JvmField
      val service_start = service_intent.setAction(action.service_start)
    }
  }

  class deep_link {
    companion object {
      @JvmField
      val ipfs_settings = "$URI_SERVICE_PREFIX/settings"
    }
  }
}