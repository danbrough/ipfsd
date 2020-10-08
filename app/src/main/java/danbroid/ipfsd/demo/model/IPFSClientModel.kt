package danbroid.ipfsd.demo.model

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.OkHttpCallExecutor
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfsd.service.IPFSClient
import java.lang.ref.WeakReference

class IPFSClientModel(val context: Application) : AndroidViewModel(context) {
  init {
    log.error("CREATED IPFS CLIENT MODEL ")
  }

  val ipfsClient: IPFSClient
    get() = getIPFSClient(context)


  val executor = object : OkHttpCallExecutor() {
    override suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>) {
      log.trace("run")
      ipfsClient.runWhenConnected {
        log.trace("connected")
        super.exec(call, handler)
      }
    }
  }

  override fun onCleared() {
    log.error("onCleared()")
   // ipfsClient.disconnect()
    ref = null
  }

  companion object {

    @Volatile
    private var ref: WeakReference<IPFSClient>? = null

    @JvmStatic
    fun getIPFSClient(context: Context) = ref?.get() ?: synchronized(IPFSClientModel::class.java) {
      ref?.get() ?: IPFSClient(context).also {
        ref = WeakReference(it)
        it.connect()
      }
    }

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
