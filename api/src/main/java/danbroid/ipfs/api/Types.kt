package danbroid.ipfs.api

import danbroid.ipfs.api.utils.Base58
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okio.ByteString.Companion.decodeBase64

class Types {

  @Serializable
  data class ID(
    val ID: String,
    val AgentVersion: String,
    val ProtocolVersion: String,
    val PublicKey: String,
    val Protocols: List<String>,
    val Addresses: List<String>,
  )


  @Serializable
  data class File(
    val Name: String,
    val Hash: String,
    val Size: Long,
  )

  @Serializable
  data class Link(@SerialName("/") val path: String)

  @Serializable
  data class CID(val Cid: Link)

  @Serializable
  data class Message(
    val from: String,
    val data: String? = null,
    val seqno: String,
    val topicIDs: Array<String>,
  ) {
    val sequenceID: Long
      get() = seqno.decodeBase64()!!.asByteBuffer().long

    val dataString: String
      get() = data?.decodeBase64()?.string(Charsets.UTF_8) ?: ""

    val fromID: String
      get() = Base58.encode(from.decodeBase64()!!.toByteArray())

    override fun toString() = "Message[from=$fromID,sequenceID:$sequenceID]"
  }


  @Serializable
  data class Object(
    val Hash: String,
    val Links: List<Link>,
  ) {

    @Serializable
    data class Link(
      val Hash: String,
      val Name: String,
      val Size: Long,
      val Target: String,
      val Type: Int,
    ) {
      val isDirectory = Type == 1
      val isFile = Type == 2
    }


    @Serializable
    data class ObjectArray(
      val Objects: List<Object>,
    )
  }

}

