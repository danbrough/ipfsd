package danbroid.ipfsd.client

import android.content.Context
import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.Request
import danbroid.ipfs.api.okhttp.OkHttpExecutor
import danbroid.ipfsd.IPFSD
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.launch

class ServiceApiClient private constructor(
  val serviceClient: ServiceClient,
  private val executor: IPFS.Executor,
) : IPFS.Executor {

  init {
    if (!serviceClient.isServiceInstalled()) {
      log.error("Package ${IPFSD.SERVICE_PKG} is not installed")
      showIPFSDNotInstalledDialog(serviceClient.context)
    }
  }

  override suspend fun <T> invoke(request: Request<T>): IPFS.ApiResponse<T> {
    serviceClient.waitTillStarted()
    return executor.invoke(request)
  }


  override fun <T> invoke(request: Request<T>, callback: IPFS.Executor.Callback<T>) {
    request.callContext.coroutineScope.launch {
      serviceClient.waitTillStarted()
      request.callContext.executor.invoke(request, callback)
    }
  }


  /*TODO override fun <T> exec(call: ApiCall<T>): Flow<ApiCall.ApiResponse<T>> = flow {
    serviceClient.waitTillStarted()
    emitAll(executor.exec(call))
  }*/

  companion object : SingletonHolder<ServiceApiClient, Pair<ServiceClient, IPFS.Executor>>({
    ServiceApiClient(it.first, it.second)
  }) {
    const val DEFAULT_PORT = 5001
    const val DEFAULT_API_URL = "http://localhost:$DEFAULT_PORT/api/v0"

    fun getInstance(context: Context, executor: IPFS.Executor): ServiceApiClient =
      getInstance(
        Pair(
          ServiceClient.getInstance(context),
          executor,
        )
      )

/*    fun getInstance(ipfsdClient: ServiceClient, executor: CallExecutor): ServiceApiClient =
      getInstance(Pair(ipfsdClient, executor))

    fun getInstance(context: Context, executor: CallExecutor): ServiceApiClient =
      getInstance(Pair(ServiceClient.getInstance(context), executor))*/
  }


}

private class AndroidIPFS(context: Context) :
  IPFS(CallContext(ServiceApiClient.getInstance(context, OkHttpExecutor()))) {
  companion object : SingletonHolder<AndroidIPFS, Context>(::AndroidIPFS)
}

val Context.androidIPFS: IPFS
  get() = AndroidIPFS.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ServiceApiClient::class.java)


