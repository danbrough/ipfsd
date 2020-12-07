package danbroid.ipfs.api.test

import danbroid.ipfs.api.IPFS
import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.junit.Test

@Serializable
@IpfsLink
data class UNI2(var name: String, var year: Int, @IpfsLink var departments: Set<Department>) {


  @Serializable
  data class Address(var street: String, var suburb: String, var postcode: Int, var city: String)

  @Serializable
  @IpfsLink
  data class Department(var name: String)

  @Contextual
  @IpfsLink
  val address = Address("14 Somewhere Street", "Kelburn", 6231, "Wellington")


}

class DagRefTest2 {

  val ipfs = IPFS()
  val correct_uni_cid = "bafyreidfuhxrvbplh6362xzrzuc7jventfu6prjjstobsf5ljbiwh7hgri"

  @InternalSerializationApi
  @Test
  fun test() {
    log.info("test()")
    val uni =
      UNI2("Victoria University", 1860, setOf(UNI2.Department("Maths"), UNI2.Department("Music")))

    @InternalSerializationApi
    fun module(ipfs: IPFS) = SerializersModule {
      // contextual(Student::class, IPFSObjectSerializer(ipfs, Student::class))
      //contextual(School::class, IPFSObjectSerializer(ipfs, School::class))

    }

    @InternalSerializationApi
    fun format(ipfs: IPFS) = Json {
      serializersModule = module(ipfs)
    }

    val json = format(ipfs).encodeToString(uni)
    log.info("uni: $json")


  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagRefTest::class.java)
