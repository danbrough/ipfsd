package danbroid.ipfsd.service

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.toAndroidIconCompat
import danbroid.ipfsd.IPFSD

class NotificationManager(
  val context: Context,
  val channelID: String,
  channelName: String,
  channelDescription: String,
  importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT,
  var listener: NotificationListener? = null
) {

  interface NotificationListener {
    fun onAction(intent: Intent)
  }

  companion object {
    const val NOTIFICATION_ID = 19381
    const val EXTRA_INSTANCE_ID = "extra_instance_id"

  }

  private var notificationID = 0
  private val intentFilter = IntentFilter()

  init {
    val channel = NotificationChannelCompat.Builder(channelID, importance).setName(channelName)
      .setDescription(channelDescription).build()
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.createNotificationChannel(channel)

/*    intentFilter.addAction(IPFSService.ACTION_SETTINGS)
    intentFilter.addAction(IPFSService.ACTION_CLEAR_NET_STATS)*/

  }


  private val notificationReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      listener?.onAction(intent) ?: log.trace("intent: $intent")
    }
  }

  private fun createBroadcastIntent(
    action: String,
    instanceId: Int,
    flags: Int = PendingIntent.FLAG_CANCEL_CURRENT
  ): PendingIntent =
    Intent(action).setPackage(context.packageName).let {
      it.putExtra(EXTRA_INSTANCE_ID, instanceId)
      PendingIntent.getBroadcast(context, instanceId, it, flags)
    }


  private fun createNotification(
    title: String? = null,
    contentText: String? = null,
    @DrawableRes smallIcon: Int = R.drawable.ic_access_point,
  ) = NotificationCompat.Builder(context, channelID).apply {
    setSmallIcon(smallIcon)
    setOngoing(true)
    //setContentTitle(title ?: context.getString(R.string.title_service))
    setContentText(contentText ?: "COntent Text")
    setOnlyAlertOnce(true)
    setLargeIcon(
      ResourcesCompat.getDrawable(context.resources, R.drawable.ipfs_icon, context.theme)
        ?.toBitmap(256, 256)
    )
    /**
     *         .setStyle(NotificationCompat.BigTextStyle()
    .bigText("Much longer text that cannot fit one line..."))
     */
    setPriority(NotificationCompat.PRIORITY_DEFAULT)
    setContentIntent(
      context.packageManager?.getLaunchIntentForPackage(context.packageName)?.let { sessionIntent ->
        PendingIntent.getActivity(context, 0, sessionIntent, PendingIntent.FLAG_CANCEL_CURRENT)
      })

    setDeleteIntent(
      Intent(context, IPFSService::class.java).setAction(IPFSService.ACTION_STOP).let {
        PendingIntent.getService(context, 0, it, PendingIntent.FLAG_CANCEL_CURRENT)
      })


    addAction(
      NotificationCompat.Action.Builder(
        IconicsDrawable(
          context,
          GoogleMaterial.Icon.gmd_settings
        ).toAndroidIconCompat(), context.getString(R.string.lbl_settings),
        Intent(Intent.ACTION_VIEW).setData("ipfsdemo://settings".toUri()).let {
          PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_CANCEL_CURRENT)
        }
      ).build()
    )


    addAction(
      NotificationCompat.Action.Builder(
        IconicsDrawable(
          context,
          GoogleMaterial.Icon.gmd_clear
        ).toAndroidIconCompat(), "Reset Stats",
        IPFSD.intent.resetStatsPromptPending(context)
      ).build()
    )
    /*addAction(
      0,
      context.getString(R.string.lbl_reset_stats),
      IPFSD.intent.resetStatsPromptPending(context)
    )*/
  }

  @MainThread
  fun showNotification(
    contentText: String? = null,
    title: String? = null,
    builderConfig: (NotificationCompat.Builder.() -> Unit)? = null
  ) {

    if (notificationID == 0) {
      notificationID = NOTIFICATION_ID
      context.registerReceiver(notificationReceiver, intentFilter)
    }

    val builder = createNotification(title, contentText)


    builderConfig?.invoke(builder)
    with(NotificationManagerCompat.from(context)) {
      notify(notificationID, builder.build())
    }
  }

  @MainThread
  fun cancelNotification() {
    if (notificationID != 0) {
      NotificationManagerCompat.from(context).cancel(notificationID)
      notificationID = 0
      context.unregisterReceiver(notificationReceiver)
    }
  }

}

private val log = org.slf4j.LoggerFactory.getLogger(NotificationManager::class.java)
