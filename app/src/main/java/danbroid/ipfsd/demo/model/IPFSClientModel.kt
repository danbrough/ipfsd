package danbroid.ipfsd.demo.model

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.CallExecutor
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfsd.service.ApiClient
import danbroid.ipfsd.service.IPFSMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class IPFSClientModel(val context: Application) : AndroidViewModel(context) {
  init {
    log.error("CREATED IPFS CLIENT MODEL ")
  }

  private var apiClient = ApiClient(context)

  val callExecutor = apiClient

  override fun onCleared() {
    log.error("onCleared()")
    apiClient.close()
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

val Fragment.ipfsClient: IPFSClientModel
  get() = requireActivity().ipfsClient

val Activity.ipfsClient: IPFSClientModel
  get() = ViewModelProvider(
    this as ComponentActivity,
    IPFSClientModel.Companion.ModelFactory(this)
  ).get(
    IPFSClientModel::class.java
  )


private val log = org.slf4j.LoggerFactory.getLogger(IPFSClientModel::class.java)
