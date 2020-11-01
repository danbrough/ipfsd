package danbroid.ipfs.api


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface CallExecutor {
  fun <T> exec(call: ApiCall<T>): Flow<ApiCall.ApiResponse<T>>
}




