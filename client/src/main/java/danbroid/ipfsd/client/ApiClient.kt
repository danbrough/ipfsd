package danbroid.ipfsd.client

import android.content.Context
import androidx.lifecycle.asFlow
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfs.api.okhttp.OkHttpCallExecutor
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.flow.*

class ApiClient(
  val serviceClient: IPFSDClient,
  val executor: CallExecutor,
) : CallExecutor {

  override fun <T> exec(call: ApiCall<T>): Flow<T> = flow {
    log.warn("ApiClient: flow started..")
    serviceClient.waitTillStarted()
    log.warn("finished waiting for connect")
    emitAll(executor.exec(call))
  }

  companion object : SingletonHolder<ApiClient, Pair<IPFSDClient, CallExecutor>>({
    ApiClient(it.first, it.second)
  }) {
    const val DEFAULT_PORT = 5001
    const val DEFAULT_API_URL = "http://localhost:$DEFAULT_PORT/api/v0"

    fun getInstance(context: Context, urlBase: String = DEFAULT_API_URL) =
      getInstance(Pair(IPFSDClient.getInstance(context), OkHttpCallExecutor(urlBase)))

    fun getInstance(ipfsdClient: IPFSDClient, executor: CallExecutor) =
      getInstance(Pair(ipfsdClient, executor))

    fun getInstance(context: Context, executor: CallExecutor) =
      getInstance(Pair(IPFSDClient.getInstance(context), executor))
  }
}


private val log = org.slf4j.LoggerFactory.getLogger(ApiClient::class.java)
