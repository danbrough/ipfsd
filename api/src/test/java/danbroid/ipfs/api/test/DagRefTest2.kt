package danbroid.ipfs.api.test


import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.Types
import danbroid.ipfs.api.blocking
import danbroid.ipfs.api.json
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.junit.Test
import java.util.*

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IpfsLink

interface Dag

@Serializable
@IpfsLink
data class UNI(
  var name: String,
  var year: Int,
  @Contextual @IpfsLink var departments: Set<Department>
) {

  @Serializable
  data class Address(var street: String, var suburb: String, var postcode: Int, var city: String) :
    Dag

  @Serializable
  @IpfsLink
  data class Department(var name: String)

  @Serializable
  @Contextual
  @IpfsLink
  var address: Address? = null

  @Contextual
  var date: Date? = null

  @Serializable
  var message: String? = null
}

/*@Serializer(forClass = Dag::class)
class DagSerializer<T>(val ipfs: IPFS, val serializer: KSerializer<T>) : KSerializer<T> {
  override val descriptor: SerialDescriptor
    get() = TODO("Not yet implemented")

  override fun deserialize(decoder: Decoder): T {
    TODO("Not yet implemented")
  }

  override fun serialize(encoder: Encoder, value: T) {
    val data = Stuff.format(ipfs).encodeToString(serializer, value)
    log.warn("DATA: $data")
    val cid = ipfs.blocking {
      dag.put(data).json()
    }
    log.warn("CID: $cid")
    Types.Link.serializer().serialize(encoder, Types.Link(cid.Cid.path))
  }
}*/


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

  override fun deserialize(decoder: Decoder) =
    decoder.decodeLong().let { Date(it) }

  override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)
}


@InternalSerializationApi
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
