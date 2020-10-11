package danbroid.ipfs.api

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import danbroid.ipfs.api.utils.Base58
import okio.ByteString.Companion.decodeBase64

object Types {

  data class ID(
    @SerializedName("ID")
    val id: String,
    @SerializedName("AgentVersion")
    val agentVersion: String,
    @SerializedName("ProtocolVersion")
    val protocolVersion: String,
    @SerializedName("PublicKey")
    val publicKey: String,
    @SerializedName("Protocols")
    val protocols: Array<String>,
    @SerializedName("Addresses")
    val addresses: Array<String>,
  )


  data class File(
    @SerializedName("Bytes")
    val bytes: Long,
    @SerializedName("Hash")
    val hash: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Size")
    val size: Long
  )

  data class Link(
    @SerializedName("Hash")
    val hash: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Size")
    val size: Long,
    @SerializedName("Target")
    val target: String,
    @SerializedName("Type")
    val type: Int
  ) {
    val isDirectory = type == 1
    val isFile = type == 2
  }

  data class Object(
    @SerializedName("Hash")
    val hash: String,
    @SerializedName("Links")
    val links: Array<Link>
  )

  data class Objects(
    @SerializedName("Objects")
    val objects: Array<Object>
  )

  data class Version(
    @SerializedName("Commit")
    val commit: String,
    @SerializedName("Golang")
    val goLang: String,
    @SerializedName("Repo")
    val repo: String,
    @SerializedName("Version")
    val version: String,
    @SerializedName("System")
    val system: String
  )

  class PubSub {
    data class Message(
      val from: String,
      val data: String,
      val seqno: String,
      val topicIDs: Array<String>
    ) {
      val sequenceID: Long
        get() = seqno.decodeBase64()!!.asByteBuffer().long

      val dataString: String
        get() = data.decodeBase64()!!.string(Charsets.UTF_8)

      val fromID: String
        get() = Base58.encode(from.decodeBase64()!!.toByteArray())

      override fun toString() = "Message[from=$fromID,sequenceID:$sequenceID,$dataString]"
    }
  }

  data class Key(
    @SerializedName("Name")
    val name: String,
    @SerializedName("Id")
    val id: String
  )

  data class KeyError(
    @SerializedName("Key")
    val key: GcError
  ) {
    data class GcError(
      @SerializedName("/")
      val cid: String
    )
  }

  data class Keys(@SerializedName("Keys") val keys: Array<Key>)

  class Stats {

    data class Bandwidth(
      @SerializedName("TotalIn")
      val totalIn: Long,
      @SerializedName("TotalOut")
      val totalOut: Long,
      @SerializedName("RateIn")
      val rateIn: Double,
      @SerializedName("RateOut")
      val rateOut: Double
    )
  }

  data class NameValue(
    @SerializedName("Name")
    val name: String,
    @SerializedName("Value")
    val value: String
  )

  class Config {
    data class ConfigChange(
      @SerializedName("NewCfg")
      val newConfig: JsonObject,
      @SerializedName("OldCfg")
      val oldConfig: JsonObject
    )
  }

}

