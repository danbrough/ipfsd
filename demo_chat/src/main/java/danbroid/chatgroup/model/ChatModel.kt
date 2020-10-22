package danbroid.chatgroup.model

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import danbroid.ipfsd.client.IPFSClient

class ChatModel(context: Application) : AndroidViewModel(context) {

  val ipfsClient = IPFSClient.getInstance(context)

  init {
    log.info("created chat model")
  }
}

fun ComponentActivity.chatModel() =
  ViewModelProvider(this).get(ChatModel::class.java)

fun Fragment.chatModel() = requireActivity().chatModel()


private val log = org.slf4j.LoggerFactory.getLogger(ChatModel::class.java)
