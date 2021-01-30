package danbroid.ipfs.api.test

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import org.junit.Test
import java.util.*

class FlowTest {

  val nameFlow: Flow<String> = flow {
    log.trace("flow started")
    emit("The date is ${Date()}")
  }


  fun getMessage(scope: CoroutineScope = GlobalScope): Deferred<String> =
    scope.async(Dispatchers.IO, start = CoroutineStart.LAZY) {
      ""
    }


  suspend fun getMessage(): String = coroutineScope {
    getMessage(this).await()
  }


  @Test
  fun test1() {
    runBlocking {
      var s = nameFlow.firstOrNull()
      log.debug("S1 is $s")
      delay(1000)
      s = nameFlow.firstOrNull()
      log.debug("S2 is $s")
    }
  }
}

private val log = org.slf4j.LoggerFactory.getLogger(FlowTest::class.java)
