package danbroid.ipfsd.client.model

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.okhttp.OkHttpExecutor
import danbroid.ipfsd.client.IPFSMessage
import danbroid.ipfsd.client.ServiceApiClient
import danbroid.ipfsd.client.ServiceClient


class IPFSClientModel(val context: Application) : AndroidViewModel(context) {

  private val serviceClient = ServiceClient.getInstance(context)
  private var apiClient = ServiceApiClient.getInstance(Pair(serviceClient, OkHttpExecutor()))

  val api by lazy {
    IPFS(apiClient)
  }


  suspend fun sendMessage(msg: IPFSMessage) {
    serviceClient.sendMessage(msg)
  }

  override fun onCleared() {
    log.error("onCleared()")
  }

  companion object {
    class ModelFactory(val context: Context) : ViewModelProvider.NewInstanceFactory() {
      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IPFSClientModel(context.applicationContext as Application) as T
      }
    }
  }
}

val Fragment.ipfsModel: IPFSClientModel
  get() = requireActivity().ipfsModel

val Activity.ipfsModel: IPFSClientModel
  get() = ViewModelProvider(
    this as ComponentActivity,
    IPFSClientModel.Companion.ModelFactory(this)
  ).get(IPFSClientModel::class.java)

val Activity.ipfs: IPFS
  get() = ipfsModel.api

val Fragment.ipfs: IPFS
  get() = ipfsModel.api


private val log = org.slf4j.LoggerFactory.getLogger(IPFSClientModel::class.java)
