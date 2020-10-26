package danbroid.ipfsd.app.shopping

import android.app.Activity
import android.content.Context
import danbroid.ipfs.api.API
import danbroid.ipfs.api.ResultHandler
import danbroid.ipfsd.client.model.ipfsModel
import kotlinx.coroutines.*
import org.json.JSONObject

class ShoppingListManager(context: Context) {

  val ipfsModel = (context as Activity).ipfsModel


  companion object {
    @Volatile
    private var INSTANCE: ShoppingListManager? = null

    @JvmStatic
    fun getInstance(context: Context) = INSTANCE ?: synchronized(ShoppingListManager::class.java) {
      INSTANCE ?: ShoppingListManager(context).also {
        INSTANCE = it
      }
    }
  }

  suspend fun createList(handler: ResultHandler<ShoppingList>) {
    val json = JSONObject().apply {
      put("name", "Test")
    }
    ipfsModel.callExecutor.exec(API.Dag.put().addData(json.toString())) {
      log.debug("result: $it")
    }
  }
}

val Activity.shoppingListManager: ShoppingListManager
  get() = ShoppingListManager.getInstance(this)

private val log = org.slf4j.LoggerFactory.getLogger(ShoppingListManager::class.java)
