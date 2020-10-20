package danbroid.shoppinglist.model

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import danbroid.ipfsd.client.IPFSClient

class ShoppingListModel(context: Application) : AndroidViewModel(context) {

  val ipfsClient = IPFSClient.getInstance(context)


  init {
    log.warn("created shopping list model")
  }
}

fun ComponentActivity.shoppingListModel() =
  ViewModelProvider(this).get(ShoppingListModel::class.java)

fun Fragment.shoppingListModel() = requireActivity().shoppingListModel()


private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListModel::class.java)
