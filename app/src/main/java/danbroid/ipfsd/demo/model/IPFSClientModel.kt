package danbroid.ipfsd.demo.model

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danbroid.ipfs.api.API
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.OkHttpCallExecutor
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfsd.service.IPFSClient

class IPFSClientModel(context: Application) : AndroidViewModel(context) {
  init {
    log.error("CREATED IPFS CLIENT MODEL ")
  }

  val ipfsClient = IPFSClient.getClient(context).also {
    it.connect()
  }

  val executor = object : OkHttpCallExecutor() {
    override suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>) {
      ipfsClient.runWhenConnected {
        super.exec(call, handler)
      }
    }
  }
  val api = API(executor)

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
