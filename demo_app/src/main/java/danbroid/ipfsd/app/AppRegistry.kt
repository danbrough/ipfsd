package danbroid.ipfsd.app

import android.content.Context
import danbroid.ipfs.api.API
import danbroid.ipfsd.client.ApiClient
import danbroid.util.misc.SingletonHolder


class AppRegistry(val context: Context) {

  companion object : SingletonHolder<AppRegistry, Context>(::AppRegistry) {
    const val IPFSD_FILES_PREFIX = "/ipfsd/apps"
  }


  private val apiClient = ApiClient.getInstance(context)
  private val executor = apiClient.executor

  suspend fun <T : IPFSApp> getOrCreate(name: String): T {
    val conf = "$IPFSD_FILES_PREFIX/$name"
    TODO("IMPLEMENT")
  }


  suspend fun test() {
    log.warn("test()")

    API.Files.read("/tedst.txt").get(executor).also {
      log.debug(
        "read: ${
          if (it.isSuccessful) it.getReader()
            .readText() else "failed: ${it.responseMessage} : ${it.getReader().readText()}"
        }"
      )
    }
  }
}


val Context.appRegistry: AppRegistry
  get() = AppRegistry.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(AppRegistry::class.java)
