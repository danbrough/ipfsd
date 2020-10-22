package danbroid.ipfsd.service

import danbroid.ipfsd.IPFSD
import danbroid.util.misc.UniqueIDS

object IPFSNavGraph : UniqueIDS {

  object dest {
    val settings = nextID()
  }

  object deep_link {
    val settings = IPFSD.deep_link.ipfs_settings
  }

}