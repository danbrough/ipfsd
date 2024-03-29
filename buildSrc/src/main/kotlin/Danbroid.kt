object Danbroid {


  const val IPFSD_SCHEME = "ipfsd"
  val IPFS_API = System.getenv().getOrDefault("IPFS_API", "")



  object utils {
    val useLocalUtils = false
    //const val version = "1.1.4"
    private const val pkg = "com.github.danbrough.androidutils"
    const val misc = "$pkg:misc:_"
    const val menu = "$pkg:menu:_"
    const val slf4j = "$pkg:slf4j:_"
    const val logging = "$pkg:logging:_"
    const val logging_core = "$pkg:logging_core:_"
  }


}


