package danbroid.ipfs.api.test

import danbroid.ipfs.api.Dag
import danbroid.ipfs.api.dag
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

  override fun toString() = "Thang[$name:$age:$date:$stuff]"

}


class DagTest : CallTest() {


  @Test
  fun test() {
    log.info("test()")
    ipfs.blocking {
      val msg = "Hello World"
      val cid = dag.put(data = msg).get().value.cid.cid
      log.info("DAG: $cid")
      dag<String>(cid).also {
        log.info("msg is $it")
        Assert.assertEquals("Message is incorrect", msg, it)
      }
      val thang1 = Thang("Mr Man", 111)
      log.info("thang1: $thang1")
      val cid2 = dag.put(data = thang1).get().value.cid.cid
      log.info("added dag: $cid2")
      val thang2 = dag<Thang>(cid2)
      log.info("thang2: $thang2")
      Assert.assertEquals("Dag objects are different", true, thang1 == thang2)
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(DagTest::class.java)
