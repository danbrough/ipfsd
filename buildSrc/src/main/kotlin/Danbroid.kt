object Danbroid {


  const val IPFSD_SCHEME = "ipfsd"
  const val bridge_version = "0.9.0_01"
  const val ipfsd_bridge = "com.github.danbrough.ipfsd:bridge:${bridge_version}"
  val IPFS_API = System.getenv().getOrDefault("IPFS_API", "")
  val BRIDGE_BUILD = true


  object utils {
    val useLocalUtils = false
    const val version = "1.1.4"
    private const val pkg = "com.github.danbrough.androidutils"
    const val misc = "$pkg:misc:_"
    const val menu = "$pkg:menu:_"
    const val slf4j = "$pkg:slf4j:_"
    const val logging = "$pkg:logging:_"
    const val logging_core = "$pkg:logging_core:_"
  }


}


