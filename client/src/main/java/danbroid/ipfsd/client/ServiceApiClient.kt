package danbroid.ipfsd.client

import android.content.Context
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfs.api.okhttp.OkHttpCallExecutor
import danbroid.ipfsd.IPFSD
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ServiceApiClient(
  val serviceClient: ServiceClient,
  private val executor: CallExecutor,
) : CallExecutor {

  init {
    if (!serviceClient.isServiceInstalled()) throw Exception("Package ${IPFSD.SERVICE_PKG} is not installed")
  }

  override fun <T> exec(call: ApiCall<T>): Flow<ApiCall.ApiResponse<T>> = flow {
    if (!serviceClient.isServiceInstalled()) throw Exception("Package ${IPFSD.SERVICE_PKG} is not installed")
    log.warn("ApiClient: flow started..")
    serviceClient.waitTillStarted()
    log.warn("finished waiting for connect")
    emitAll(executor.exec(call))
  }

  companion object : SingletonHolder<ServiceApiClient, Pair<ServiceClient, CallExecutor>>({
    ServiceApiClient(it.first, it.second)
  }) {
    const val DEFAULT_PORT = 5001
    const val DEFAULT_API_URL = "http://localhost:$DEFAULT_PORT/api/v0"

    fun getInstance(context: Context, urlBase: String = DEFAULT_API_URL) =
      getInstance(Pair(ServiceClient.getInstance(context), OkHttpCallExecutor(urlBase)))

    fun getInstance(ipfsdClient: ServiceClient, executor: CallExecutor) =
      getInstance(Pair(ipfsdClient, executor))

    fun getInstance(context: Context, executor: CallExecutor) =
      getInstance(Pair(ServiceClient.getInstance(context), executor))
  }
}


private val log = org.slf4j.LoggerFactory.getLogger(ServiceApiClient::class.java)
