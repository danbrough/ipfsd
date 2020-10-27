package danbroid.ipfs.api


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

interface CallExecutor {

  interface JavaResultHandler<T> {
    fun onResult(t: Result<T>?)
  }

  fun <T> exec(call: ApiCall<T>): Flow<T>

  suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>? = null)

  fun <T> exec(call: ApiCall<T>, handler: JavaResultHandler<T>) {
    runBlocking {
      exec(call, handler::onResult)
    }
  }

}