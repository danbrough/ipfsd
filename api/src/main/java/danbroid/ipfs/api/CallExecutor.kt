package danbroid.ipfs.api


import kotlinx.coroutines.runBlocking

interface CallExecutor {

  interface JavaResultHandler<T> {
    fun onResult(t: Result<T>?)
  }

  suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>? = null)

  fun <T> exec(call: ApiCall<T>, handler: JavaResultHandler<T>) {
    runBlocking {
      exec(call, handler::onResult)
    }
  }

}