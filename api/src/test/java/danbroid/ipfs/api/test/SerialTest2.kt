package danbroid.ipfs.api.test

import danbroid.ipfs.api.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer
import org.junit.Test


private interface DAG<T : Any> {
  val cid: String
  val value: T
}


@Serializable(with = IDagSerializer::class)
private class IDag<T : Any>(val serializer: KSerializer<T>, override var cid: String) : DAG<T> {
  private val _t by lazy {
    ipfs.blocking {
      dag.get<T>(cid).invoke().text.let {
        Json.decodeFromString(serializer, it)
      }
    }
  }

  override val value: T
    get() = _t

  override fun toString(): String = "IDag<$cid>"
}

private class IDagSerializer<T : Any>(val serializer: KSerializer<T>) : KSerializer<IDag<T>> {
  override val descriptor: SerialDescriptor = serializer.descriptor

  override fun deserialize(decoder: Decoder): IDag<T> {
    log.info("deserialize()")
    val link = decoder.decodeSerializableValue(Types.Link.serializer())
    log.debug("link: $link")
    return IDag(serializer, link.path)
  }

  override fun serialize(encoder: Encoder, value: IDag<T>) {
    TODO("Not yet implemented")
  }
}

private class ODagSerializer<T : Any>(serializer: KSerializer<T>) : KSerializer<ODag<T>> {
  private val linkSerializer = Types.Link.serializer()
  override val descriptor: SerialDescriptor = serializer.descriptor

  override fun deserialize(decoder: Decoder): ODag<T> {
    TODO("Not implemented")
  }

  override fun serialize(encoder: Encoder, value: ODag<T>) {
    log.trace("serialize(): $value")
    val cid = value.cid
    log.trace("cid: $cid")
    linkSerializer.serialize(encoder, Types.Link(cid))
  }
}

@Serializable(with = ODagSerializer::class)
data class ODag<T : Any>(val serializer: KSerializer<T>, override val value: T) : DAG<T> {

  private val _cid by lazy {
    ipfs.blocking {
      Json.encodeToString(serializer, value).let {
        dag.put(it).json().Cid.path
      }
    }
  }

  override val cid: String
    get() = _cid
}

private inline fun <reified T : Any> T.toDag(): DAG<T> = ODag(T::class.serializer(), this)

private val format = Json {
  serializersModule = SerializersModule {
    polymorphic(DAG::class) {
      subclass(ODag::class)
    }
/*    polymorphic(DAG::class) {
      subclass(IDag::class)
      subclass(ODag::class)
    }*/
  }
}


@Serializable
private data class ZOO(var name: String = "", var age: Int)

class SerialTest2 {


  @Test
  fun test() {
    log.info("test()")
    val zoo = ZOO("Happy Place", 1972)
    log.debug("zoo: $zoo")
    val link = zoo.dagLink()
    log.debug("link: $link")
    val json = format.encodeToString(link)
    log.debug("json: $json")
    val zoo2Link = format.decodeFromString<IDag<ZOO>>(json)
    log.info("zoo2Link: $zoo2Link")
    val zoo2 = zoo2Link.value
    log.debug("zoo2: $zoo2")
    require(zoo == zoo2) {
      "zoo != zoo2"
    }

  }
}

private val log = org.slf4j.LoggerFactory.getLogger(SerialTest2::class.java)
