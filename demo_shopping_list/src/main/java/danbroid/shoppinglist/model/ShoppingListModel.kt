package danbroid.shoppinglist.model

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import danbroid.ipfsd.service.IPFSClient

class ShoppingListModel(context: Application) : AndroidViewModel(context) {

  private val ipfsClient = IPFSClient.getInstance(context)

  

  init {
    log.warn("created shopping list model")
  }
}

fun ComponentActivity.shoppingModel() =
  ViewModelProvider(this).get(ShoppingListModel::class.java)

fun Fragment.shoppingModel() = requireActivity().shoppingModel()


private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListModel::class.java)
