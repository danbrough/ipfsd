package danbroid.ipfs.api.utils

import java.lang.ref.WeakReference
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

  fun getInstance(): T = instance ?: throw Exception("instance is null")
}

open class WeakSingletonHolder<out T : Any, in A>(private val creator: (A) -> T) {

  @Volatile
  private var instance: WeakReference<T>? = null

  fun getInstance(arg: A): T = instance?.get() ?: synchronized(this) {
    instance?.get() ?: creator.invoke(arg).also {
      instance = WeakReference(it)
    }
  }

  fun getInstance(): T = instance?.get() ?: throw Exception("instance is null")
}


