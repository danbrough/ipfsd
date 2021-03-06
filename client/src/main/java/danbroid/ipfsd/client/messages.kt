package danbroid.ipfsd.client

import android.os.Message
import android.os.Messenger
import android.os.Parcelable
import androidx.core.os.bundleOf
import kotlinx.parcelize.Parcelize

private const val MESSAGE_KEY = "_message"

sealed class IPFSMessage : Parcelable {

  @Parcelize
  object CLIENT_CONNECT : IPFSMessage()

  @Parcelize
  object CLIENT_DISCONNECT : IPFSMessage()

  @Parcelize
  object SERVICE_STARTED : IPFSMessage()

  @Parcelize
  object SERVICE_STOPPING : IPFSMessage()

  @Parcelize
  object TIMEOUT_RESET : IPFSMessage()


  @Parcelize
  object STATS_RESET : IPFSMessage()

  @Parcelize
  object REPO_STAT : IPFSMessage()

  @Parcelize
  object TIMEOUT_INCREMENT_LOCK : IPFSMessage()

  @Parcelize
  object TIMEOUT_DECREMENT_LOCK : IPFSMessage()

  @Parcelize
  data class BANDWIDTH(
    val totalIn: Long,
    val totalOut: Long,
    val rateIn: Double,
    val rateOut: Double
  ) : IPFSMessage()

  @Parcelize
  data class SET_CONFIGURATION(
    val inactivityTimeout: Long,
    val resetStats: Boolean = false
  ) : IPFSMessage()

  fun toMessage(replyTo: Messenger? = null) =
    Message.obtain().also {
      it.data = bundleOf(MESSAGE_KEY to this)
      it.replyTo = replyTo
    }


  override fun toString() = "IPFSMessage<${javaClass.simpleName}>"

}

fun Message.toIPFSMessage(classLoader: ClassLoader): IPFSMessage = let {
  data.classLoader = classLoader
  data.getParcelable(MESSAGE_KEY)!!
}

