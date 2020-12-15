package danbroid.ipfs.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Types {

  @Serializable
  data class ID(
    val ID: String,
    val AgentVersion: String,
    val ProtocolVersion: String,
    val PublicKey: String,
    val Protocols: Array<String>,
    val Addresses: Array<String>,
  )


  @Serializable
  data class File(
    val Name: String,
    val Hash: String,
    val Size: Long,
  )

  @Serializable
  data class Link(@SerialName("/") val link: String)

  @Serializable
  data class CID(val Cid: Link)

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

