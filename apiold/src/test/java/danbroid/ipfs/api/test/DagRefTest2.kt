package danbroid.ipfs.api.test


import com.google.gson.annotations.SerializedName
import danbroid.ipfs.api.DagObjectRef
import danbroid.ipfs.api.IPFS
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.junit.Test
import java.util.*

@Serializable
@IpfsLink
data class UNI2(var name: String, var year: Int, @IpfsLink var departments: Set<Department>) {


  @Serializable
  data class Address(var street: String, var suburb: String, var postcode: Int, var city: String)

  @Serializable
  @IpfsLink
  data class Department(var name: String)

  @Serializable
  @IpfsLink
  var address: Address? = null

  @Contextual
  var date: Date? = null

  @Serializable
  var message: String? = null
}

@Serializable
data class CID(
  @SerialName("/")
  @SerializedName("/") val hash: String
)

object Stuff {
  fun module(ipfs: IPFS) = SerializersModule {
    // contextual(Student::class, IPFSObjectSerializer(ipfs, Student::class))
    //contextual(School::class, IPFSObjectSerializer(ipfs, School::class))
    contextual(Date::class, DateAsLongSerializer)
  }

  fun format(ipfs: IPFS) = Json {
    serializersModule = module(ipfs)
  }
}

object DateAsLongSerializer : KSerializer<Date> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder) =
    decoder.decodeLong().let { Date(it) }

  override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)
}


object TestData {
  val uni =
    UNI2(
      "Victoria University",
      1860,
      setOf(UNI2.Department("Maths"), UNI2.Department("Music"))
    ).also {
      it.date = Date()
      it.address = UNI2.Address("14 Somewhere Street", "Kelburn", 6231, "Wellington")
    }
}

@InternalSerializationApi
class DagRefTest2 {

  val correct_uni_cid = "bafyreidfuhxrvbplh6362xzrzuc7jventfu6prjjstobsf5ljbiwh7hgri"

  @Test
  fun test() {
    log.info("test()")


    val json = Stuff.format(ipfs).encodeToString(TestData.uni)
    log.info("uni: $json")

    val ref =
      DagObjectRef<Date>(
        "bafyreidv7skmnbm7gxq5hk3bevn5xusqsqw4mwcos3yf2cptesq2l3aliy",
        ipfs
      )


  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagRefTest::class.java)
