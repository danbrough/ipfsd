package danbroid.ipfsd.service.activities

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import danbroid.ipfsd.IPFSD
import danbroid.ipfsd.service.IPFSNavGraph
import danbroid.ipfsd.service.IPFSService
import danbroid.ipfsd.service.R
import danbroid.ipfsd.service.rootMenu
import danbroid.util.menu.MenuActivity
import danbroid.util.menu.MenuItemBuilder
import danbroid.util.menu.createMenuNavGraph

class MainActivity : MenuActivity() {


  override fun createNavGraph(navController: androidx.navigation.NavController) =
    navController.createMenuNavGraph(this, defaultMenuID = IPFSD.deep_link.ipfs_settings) {

    }

  override fun getRootMenu(menuID: String) = rootMenu


}

private val log = org.slf4j.LoggerFactory.getLogger(MainActivity::class.java)
