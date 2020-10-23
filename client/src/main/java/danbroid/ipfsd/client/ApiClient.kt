package danbroid.ipfsd.client

import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfs.api.okhttp.OkHttpCallExecutor

class ApiClient(
  val serviceClient: IPFSClient, port: Int = 5001,
  urlBase: String = "http://localhost:$port/api/v0"
) : OkHttpCallExecutor(urlBase = urlBase) {


  override suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>?) =
    serviceClient.runWhenConnected {
      super.exec(call, handler)
    }

  fun close() {

  }
}


private val log = org.slf4j.LoggerFactory.getLogger(ApiClient::class.java)
