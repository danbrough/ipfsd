package danbroid.ipfs.api.test

import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.okhttp.OkHttpExecutor

const val apiUrl = "http://localhost:5001/api/v0" //"http://localhost:9999/api/v0"

object api : IPFS(OkHttpExecutor(apiUrl).also {
  IPFS.getInstance(it)
})


