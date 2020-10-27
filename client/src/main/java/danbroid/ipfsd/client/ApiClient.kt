package danbroid.ipfsd.client

import android.content.Context
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfs.api.okhttp.OkHttpCallExecutor
import danbroid.util.misc.SingletonHolder

class ApiClient(
  val serviceClient: IPFSDClient,
  urlBase: String = DEFAULT_API_URL
) : OkHttpCallExecutor(urlBase = urlBase) {


  override suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>?) =
    serviceClient.runWhenConnected {
      super.exec(call, handler)
    }

  companion object : SingletonHolder<ApiClient, Pair<Context, String>>({
    ApiClient(IPFSDClient.getInstance(it.first), it.second)
  }) {
    const val DEFAULT_PORT = 5001
    const val DEFAULT_API_URL = "http://localhost:$DEFAULT_PORT/api/v0"

    fun getInstance(context: Context, urlBase: String = DEFAULT_API_URL) =
      getInstance(Pair(context, urlBase))
  }

/*    @Volatile
    var INSTANCE: ApiClient? = null


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
  }*/
}


private val log = org.slf4j.LoggerFactory.getLogger(ApiClient::class.java)
