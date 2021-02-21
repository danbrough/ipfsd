package danbroid.ipfsd.client

import android.content.Context
import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.Request
import danbroid.ipfs.api.okhttp.OkHttpExecutor
import danbroid.ipfsd.IPFSD
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ServiceApiClient private constructor(
  val serviceClient: ServiceClient,
  private val executor: IPFS.Executor,
  override val coroutineScope: CoroutineScope = executor.coroutineScope,
) : IPFS.Executor {

  init {
    if (!serviceClient.isServiceInstalled()) {
      log.error("Package ${IPFSD.SERVICE_PKG} is not installed")
      showIPFSDNotInstalledDialog(serviceClient.context)
    }
  }

  override suspend fun <T> invoke(request: Request<T>): IPFS.ApiResponse<T> {
    log.info("invoke() $request")
    serviceClient.waitTillStarted()
    return executor.invoke(request)
  }


  override fun <T> invoke(request: Request<T>, callback: IPFS.Executor.Callback<T>) {
    executor.coroutineScope.launch {
      serviceClient.waitTillStarted()
      executor.invoke(request, callback)
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

object _androidIPFS : danbroid.ipfs.api.utils.SingletonHolder<IPFS, Context>({
  IPFS(
    OkHttpExecutor {
      urlBase = "https://home.danbrough.org/api/v0"
      setCredentials("dan", "poiqwe098123")
    }
    /*ServiceApiClient.getInstance(
      it,
      OkHttpExecutor()
    )*/
  )
})

val Context.ipfsApi: IPFS
  get() = _androidIPFS.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ServiceApiClient::class.java)


