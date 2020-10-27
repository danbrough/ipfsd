package danbroid.ipfsd.app.shopping

import android.app.Activity
import android.content.Context
import danbroid.ipfs.api.API
import danbroid.ipfs.api.ApiCall
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfsd.app.AppRegistry
import danbroid.ipfsd.client.ipfsClient
import danbroid.ipfsd.client.model.ipfsModel
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.suspendCoroutine

class ShoppingListManager(context: Context) {

  val executor = (context as Activity).ipfsModel.callExecutor


  companion object : SingletonHolder<ShoppingListManager, Context>(::ShoppingListManager) {
    const val SHOPPING_FILES_PREFIX = "${AppRegistry.IPFSD_FILES_PREFIX}/shopping_lists"
  }

  suspend fun createList() {
    val name = "list_${UUID.randomUUID()}.json"
    val path = "$SHOPPING_FILES_PREFIX/$name"
    log.debug("checking for : $path")

  }

}

val Context.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListManager::class.java)
