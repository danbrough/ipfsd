package danbroid.ipfsd.app

import android.content.Context
import danbroid.ipfs.api.API
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfsd.client.ServiceApiClient
import danbroid.util.misc.SingletonHolder


class AppRegistry(val context: Context) {

  companion object : SingletonHolder<AppRegistry, Context>(::AppRegistry) {
    const val IPFSD_FILES_PREFIX = "/ipfsd/apps"
  }


  private val apiClient = ServiceApiClient.getInstance(context)
  private val executor: CallExecutor = apiClient

  suspend fun <T : IPFSApp> getOrCreate(name: String): T {
    val conf = "$IPFSD_FILES_PREFIX/$name"
    TODO("IMPLEMENT")
  }


  suspend fun test() {
    log.warn("test()")

    API.Files.read("/tedst.txt").get(executor).also {
      if (it.isSuccessful) {
        log.debug("read: ${it.bodyText}")
      } else {
        log.error("failed to read file: code:${it.responseCode} message:${it.responseCode} text:${it.bodyText}")
      }
    }
  }
}


val Context.appRegistry: AppRegistry
  get() = AppRegistry.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(AppRegistry::class.java)
