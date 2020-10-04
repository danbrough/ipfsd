package danbroid.ipfsd.demo.model

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danbroid.ipfs.api.API
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.OkHttpCallExecutor
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfsd.service.IPFSClient

class IPFSClientModel(context: Context) : ViewModel() {
  init {
    log.error("CREATED IPFS CLIENT MODEL ")
  }

  val executor = object : OkHttpCallExecutor() {
    override suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>) {
      IPFSClient.getClient(context).runWhenConnected {
        super.exec(call, handler)
      }
    }
  }
  val api = API(executor)

  override fun onCleared() {
    log.warn("onCleared()")
  }

  companion object {
    class ModelFactory(val context: Context) : ViewModelProvider.NewInstanceFactory() {
      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IPFSClientModel(context) as T
      }
    }
  }
}

val Fragment.ipfsClient: IPFSClientModel
  get() = ViewModelProvider(this, IPFSClientModel.Companion.ModelFactory(requireContext())).get(
    IPFSClientModel::class.java
  )

private val log = org.slf4j.LoggerFactory.getLogger(IPFSClientModel::class.java)
