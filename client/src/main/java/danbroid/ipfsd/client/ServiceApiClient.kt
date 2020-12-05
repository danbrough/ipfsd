package danbroid.ipfsd.client

import android.content.Context
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.okhttp.OkHttpCallExecutor
import danbroid.ipfsd.IPFSD
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ServiceApiClient private constructor(
  val serviceClient: ServiceClient,
  private val executor: CallExecutor,
) : CallExecutor {

  init {
    if (!serviceClient.isServiceInstalled()) {
      log.error("Package ${IPFSD.SERVICE_PKG} is not installed")
      showIPFSDNotInstalledDialog(serviceClient.context)
    }
  }


  override fun <T> exec(call: ApiCall<T>): Flow<ApiCall.ApiResponse<T>> = flow {
    serviceClient.waitTillStarted()
    emitAll(executor.exec(call))
  }

  companion object : SingletonHolder<ServiceApiClient, Pair<ServiceClient, CallExecutor>>({
    ServiceApiClient(it.first, it.second)
  }) {
    const val DEFAULT_PORT = 5001
    const val DEFAULT_API_URL = "http://localhost:$DEFAULT_PORT/api/v0"

    fun getInstance(context: Context, urlBase: String = DEFAULT_API_URL): ServiceApiClient =
      getInstance(Pair(ServiceClient.getInstance(context), OkHttpCallExecutor(urlBase)))

/*    fun getInstance(ipfsdClient: ServiceClient, executor: CallExecutor): ServiceApiClient =
      getInstance(Pair(ipfsdClient, executor))

    fun getInstance(context: Context, executor: CallExecutor): ServiceApiClient =
      getInstance(Pair(ServiceClient.getInstance(context), executor))*/
  }
}

private class AndroidIPFS(context: Context) :
  IPFS() {
  companion object : SingletonHolder<AndroidIPFS, Context>(::AndroidIPFS)
}

val Context.ipfs: IPFS
  get() = AndroidIPFS.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ServiceApiClient::class.java)


