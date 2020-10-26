package danbroid.ipfsd.client

import android.content.Context
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfs.api.okhttp.OkHttpCallExecutor

class ApiClient(
  val serviceClient: IPFSDClient, port: Int = DEFAULT_PORT,
  urlBase: String = DEFAULT_API_URL
) : OkHttpCallExecutor(urlBase = urlBase) {


  override suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>?) =
    serviceClient.runWhenConnected {
      super.exec(call, handler)
    }

  companion object {
    @Volatile
    var INSTANCE: ApiClient? = null
    const val DEFAULT_PORT = 5001
    const val DEFAULT_API_URL = "http://localhost:$DEFAULT_PORT/api/v0"

    @JvmStatic
    fun getInstance(
      context: Context,
      serviceClient: IPFSDClient = IPFSDClient.getInstance(context),
      port: Int = DEFAULT_PORT,
      urlBase: String = DEFAULT_API_URL
    ) = INSTANCE ?: synchronized(ApiClient::class.java) {
      INSTANCE ?: ApiClient(serviceClient, port, urlBase).also {
        INSTANCE = it
      }
    }
  }
}


private val log = org.slf4j.LoggerFactory.getLogger(ApiClient::class.java)
