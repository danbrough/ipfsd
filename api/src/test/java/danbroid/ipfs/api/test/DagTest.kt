package danbroid.ipfs.api.test

import danbroid.ipfs.api.Dag
import danbroid.ipfs.api.dag
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.util.*

class Thang(val name: String, val age: Int, val date: Date? = Date()) : Dag {
  data class Stuff(val message: String, val active: Boolean) : Dag

  val stuff = Stuff("Stuff1", true)

  override fun equals(other: Any?): Boolean {
    if (other == null || other !is Thang) return false
    return other.name == name && other.age == age && other.date?.equals(date) == true
        && other.stuff == stuff
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + age
    result = 31 * result + (date?.hashCode() ?: 0)
    result = 31 * result + stuff.hashCode()
    return result
  }
}


class DagTest : CallTest() {


  @Test
  fun versionTest() {

    runBlocking {
      ipfs {

        log.debug("we are here..")
        basic.version().also {
          log.debug("version is $it")
        }
        delay(1000)
        log.debug("finished")
      }
    }
  }


  @Test
  fun test() {
    log.info("test()")
    runBlocking {
      ipfs {
        val cid = dag.put(data = "Hello World").get().value.cid.cid
        log.info("DAG: $cid")
        dag<String>(cid).value.also {
          log.info("msg is $it")
        }

        val thang1 = Thang("Mr Man", 111)
        log.info("thang1: $thang1")
        val cid2 = dag.put(data = thang1).get().value.cid.cid
        log.info("added dag: $cid2")

        val thang2 = dag<Thang>(cid2).valueOrThrow()
        log.info("thang2: $thang2")

        Assert.assertEquals("Dag objects are different", true, thang1 == thang2)


      }.join()
    }

  }

}

private val log = org.slf4j.LoggerFactory.getLogger(DagTest::class.java)
