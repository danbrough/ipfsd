package danbroid.ipfs.api.utils


import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*

fun CharSequence.uriEncode(): String = URLEncoder.encode(toString(), "UTF-8")
fun CharSequence.uriDecode(): String = URLDecoder.decode(toString(), "UTF-8")

fun String.addUrlArgs(vararg keywords: Pair<String, Any?>): String = StringBuilder(this).apply {
  var firstArg = true
  keywords.forEach { arg ->
    if (arg.second == null) return@forEach
    append(if (firstArg) '?' else '&')
    firstArg = false
    append("${arg.first}=${arg.second!!.toString().uriEncode()}")
  }
}.toString()






fun createGsonBuilder() =
  GsonBuilder().registerTypeAdapter(Date::class.java, object : TypeAdapter<Date>() {
    override fun write(out: JsonWriter, value: Date) {
      out.value(value.time)
    }

    override fun read(`in`: JsonReader): Date {
      return Date(`in`.nextLong())
    }

  }.nullSafe()).serializeNulls()


open class SingletonHolder<out T : Any, in A>(creator: (A) -> T) {
  private var creator: ((A) -> T)? = creator

  @Volatile
  private var instance: T? = null

  fun getInstance(arg: A): T = instance ?: synchronized(this) {
    instance ?: creator!!.invoke(arg).also {
      instance = it
      creator = null
    }
  }
}



