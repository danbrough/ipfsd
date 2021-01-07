package danbroid.ipfs.api.test


import danbroid.ipfs.api.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.junit.Test
import java.util.*


class DagSerializer<T>(val serializer: KSerializer<T>) : KSerializer<T> {
  override val descriptor: SerialDescriptor = Types.Link.serializer().descriptor

  override fun deserialize(decoder: Decoder): T {
    log.error("not implemented")
    TODO("Not yet implemented")
  }

  override fun serialize(encoder: Encoder, value: T) {
    log.info("serialize() $value")
    val data = Stuff.format(ipfs).encodeToString(serializer, value)
    log.warn("DATA: $data")
    val cid = ipfs.blocking {
      dag.put(data).json()
    }
    log.warn("CID: $cid")
    Types.Link.serializer().serialize(encoder, Types.Link(cid.Cid.path))
  }
}

object Stuff {
  val module = SerializersModule {
    // contextual(Student::class, IPFSObjectSerializer(ipfs, Student::class))
    //contextual(School::class, IPFSObjectSerializer(ipfs, School::class))
    contextual(DateAsLongSerializer)
    contextual(DagSerializer(UNI.Address.serializer()))
    contextual(DagSerializer(UNI.Department.serializer()))

  }

  fun format(ipfs: IPFS) = Json {
    serializersModule = module
  }
}

object DateAsLongSerializer : KSerializer<Date> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder) = Date(decoder.decodeLong())

  override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)
}


class DagRefTest2 {

  val correct_uni_cid = "bafyreidfuhxrvbplh6362xzrzuc7jventfu6prjjstobsf5ljbiwh7hgri"

  @Test
  fun test() {
    log.info("test()")


    val json = Stuff.format(ipfs).encodeToString(TestData.uni)
    log.info("uni: $json")

/*
    val ref =
      DagObjectRef<Date>(
        "bafyreidv7skmnbm7gxq5hk3bevn5xusqsqw4mwcos3yf2cptesq2l3aliy",
        ipfs
      )
*/


  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagRefTest2::class.java)
