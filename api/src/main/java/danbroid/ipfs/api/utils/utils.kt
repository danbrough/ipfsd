package danbroid.ipfs.api.utils


import java.net.URLDecoder
import java.net.URLEncoder

fun CharSequence.uriEncode(): String = URLEncoder.encode(toString(), "UTF-8")
fun CharSequence.uriDecode(): String = URLDecoder.decode(toString(), "UTF-8")

fun String.addUrlArgs(vararg keywords: Pair<String, Any?>): String {
  var url: String = this
  var firstArg = true
  keywords.forEach { arg ->
    if (arg.second == null) return@forEach
    url += if (firstArg) '?' else '&'
    firstArg = false
    url += "${arg.first}=${arg.second!!.toString().uriEncode()}"
  }
  return url
}

