package danbroid.ipfsd.demo.app

import danbroid.ipfs.api.DagLink
import danbroid.ipfs.api.Serializable
import java.util.*

abstract class IPFSApp() {
  companion object {
    const val ID_PREFIX = "ipfsd/apps"
  }

  open var title: String = "Untitled"

  @Serializable
  data class AppDescription(
    val type: String,
    var id: String = UUID.randomUUID().toString(),
    val created: Long = System.currentTimeMillis()
  )

  lateinit var description: DagLink<AppDescription>

  override fun toString() = description.toString()


}


private val log = danbroid.logging.getLog(IPFSApp::class)



