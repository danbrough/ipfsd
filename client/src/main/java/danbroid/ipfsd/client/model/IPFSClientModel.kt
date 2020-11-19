package danbroid.ipfsd.client.model

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danbroid.ipfs.api.API
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfsd.client.IPFSMessage
import danbroid.ipfsd.client.ServiceApiClient


class IPFSClientModel(val context: Application) : AndroidViewModel(context) {
  init {
    log.trace("CREATED IPFS CLIENT MODEL ")
  }

  private var apiClient = ServiceApiClient.getInstance(context)
  private val serviceClient = apiClient.serviceClient

  val callExecutor: CallExecutor = apiClient
  val api = API(callExecutor)

  suspend fun sendMessage(msg: IPFSMessage) {
    serviceClient.sendMessage(msg)
  }

  override fun onCleared() {
    log.debug("onCleared()")
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

val Activity.ipfs: API
  get() = ipfsModel.api

val Fragment.ipfs: API
  get() = ipfsModel.api


private val log = org.slf4j.LoggerFactory.getLogger(IPFSClientModel::class.java)
