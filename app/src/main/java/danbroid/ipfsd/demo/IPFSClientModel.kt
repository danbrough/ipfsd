package danbroid.ipfsd.demo

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import danbroid.ipfs.api.API
import danbroid.ipfsd.service.ApiClient

class IPFSClientModel(context: Application) : AndroidViewModel(context) {
  init {
    log.error("CREATED IPFS CLIENT MODEL ")
  }

  val client = ApiClient(context)
  val api = API(client)

  override fun onCleared() {
    client.close()
  }
}

val Fragment.ipfsClient: IPFSClientModel
  get() = (requireActivity() as MainActivity).model

private val log = org.slf4j.LoggerFactory.getLogger(IPFSClientModel::class.java)
