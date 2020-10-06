package danbroid.ipfsd.service

import android.os.Message
import android.os.Messenger
import android.os.Parcelable
import androidx.core.os.bundleOf
import kotlinx.android.parcel.Parcelize

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

fun Message.toIPFSMessage(): IPFSMessage = data.getParcelable(MESSAGE_KEY)!!

