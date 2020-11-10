package danbroid.ipfs.api.utils


import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.StringReader
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

inline fun <reified T> CharSequence.parseJson() =
  GsonBuilder().create().fromJson(StringReader(this.toString()), T::class.java)

fun <T> CharSequence.parseJson(type: Class<T>) =
  GsonBuilder().create().fromJson(StringReader(this.toString()), type)

fun CharSequence.toJson(): JsonElement =
  JsonParser.parseString(this.toString())

fun Any.toJson(): String = GsonBuilder().create().toJson(this)
