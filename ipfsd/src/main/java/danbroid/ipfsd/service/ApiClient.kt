package danbroid.ipfsd.service

import android.content.Context
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.okhttp.OkHttpCallExecutor
import danbroid.ipfs.api.ResultHandler

class ApiClient(
  val context: Context, port: Int = 5001,
  urlBase: String = "http://localhost:$port/api/v0"
) : OkHttpCallExecutor(urlBase = urlBase) {

  var ipfsClient: IPFSClient? = IPFSClient.getInstance(context)

  override suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>) =
    ipfsClient!!.runWhenConnected {
      super.exec(call, handler)
    }

  fun close() {
    ipfsClient = null
  }
}


private val log = org.slf4j.LoggerFactory.getLogger(ApiClient::class.java)
