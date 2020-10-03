package danbroid.ipfsd.service

import android.content.Context
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.OkHttpCallExecutor
import danbroid.ipfs.api.ResultHandler

class ApiClient(
  val context: Context, port: Int = 5001,
  urlBase: String = "http://localhost:$port/api/v0"
) : OkHttpCallExecutor(urlBase = urlBase) {

  val ipfsClient = IPFSClient.getClient(context).also {
    it.timeout = 10000L
  }

  override suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>) =
    ipfsClient.runWhenConnected {
      super.exec(call, handler)
    }
}


private val log = org.slf4j.LoggerFactory.getLogger(ApiClient::class.java)
