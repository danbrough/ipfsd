package danbroid.ipfs.api

import OkHttpCallExecutor
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.Reader

fun <T> parseDAG(reader: Reader, type: Class<T>): T {
  return GsonBuilder().create().fromJson(reader, type)
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T> CallExecutor.dag(cid: String): ApiCall.ApiResponse<T> {
  return API.Dag.get(cid).exec(this).map {
    if (it.isSuccessful) (it as ApiCall.ApiResponse<T>).value =
      parseDAG(it.getReader(), T::class.java)
    it as ApiCall.ApiResponse<T>
  }.first()
}




