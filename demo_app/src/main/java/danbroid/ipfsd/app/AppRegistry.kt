package danbroid.ipfsd.app

import android.content.Context
import danbroid.ipfs.api.API
import danbroid.ipfs.api.ApiCall
import danbroid.ipfsd.client.ApiClient
import danbroid.ipfsd.client.ipfsClient
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine


class AppRegistry(val context: Context) {

  companion object : SingletonHolder<AppRegistry, Context>(::AppRegistry) {
    const val IPFSD_FILES_PREFIX = "/ipfsd/apps"
  }

  private val apiClient: ApiClient
    get() = ApiClient.getInstance(context)

  suspend fun <T : IPFSApp> getOrCreate(name: String): T =
    suspendCoroutine {
      val conf = "$IPFSD_FILES_PREFIX/$name"
      apiClient.exec(API.Files.read())
    }
}


val Context.registry: AppRegistry
  get() = AppRegistry.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(AppRegistry::class.java)
