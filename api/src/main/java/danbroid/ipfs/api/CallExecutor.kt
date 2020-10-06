package danbroid.ipfs.api

interface CallExecutor {
  suspend fun <T> exec(call: ApiCall<T>, handler: ResultHandler<T>)

}