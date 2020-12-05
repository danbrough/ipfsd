package danbroid.ipfs.api

import kotlinx.coroutines.runBlocking


interface CidStore {
  suspend fun put(id: String, cid: String)
  suspend fun get(id: String): String
  suspend fun search(predicate: (id: String) -> Boolean): Set<Pair<String, String>>
  suspend fun save()
}

private val log = org.slf4j.LoggerFactory.getLogger(CidStore::class.java)

const val CID_EMPTY_OBJECT = "QmdfTbBqBPQ7VNxZEYEj14VmRuZBkqFbiwReogJgS1zR1n"

class IPFSCidStore(
  val ipfs: IPFS,
  var cid: String = CID_EMPTY_OBJECT
) : CidStore {

  init {
    if (cid != CID_EMPTY_OBJECT) {
      runBlocking {

      }
    }
  }

  override suspend fun put(id: String, cid: String) {

  }

  override suspend fun get(id: String): String {
    TODO("Not yet implemented")
  }

  override suspend fun search(predicate: (id: String) -> Boolean): Set<Pair<String, String>> {
    TODO("Not yet implemented")
  }

  override suspend fun save() {
    TODO("Not yet implemented")
  }

}