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
import androidx.core.net.toUri
import danbroid.ipfsd.R

class NotificationManager(
  val context: Context,
  val channelID: String,
  channelName: String,
  channelDescription: String,
  importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT,
  val actionHandler: ((Intent) -> Unit)? = null
) {

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

    intentFilter.addAction(IPFSService.ACTION_SETTINGS)
  }


  private val notificationReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      actionHandler?.invoke(intent) ?: log.trace("intent: $intent")
    }
  }

  private fun createBroadcastIntent(action: String, instanceId: Int): PendingIntent =
    Intent(action).setPackage(context.packageName).let {
      it.putExtra(EXTRA_INSTANCE_ID, instanceId)
      PendingIntent.getBroadcast(context, instanceId, it, PendingIntent.FLAG_UPDATE_CURRENT)
    }


  private fun createNotification(
    title: String? = null,
    contentText: String? = null,
    @DrawableRes smallIcon: Int = R.drawable.ic_access_point,
  ) = NotificationCompat.Builder(context, channelID).apply {
    setSmallIcon(smallIcon)
    setOngoing(false)
    if (title != null)
      setContentTitle(title)
    if (contentText != null)
      setContentText(contentText)
    setOnlyAlertOnce(true)
    /**
     *         .setStyle(NotificationCompat.BigTextStyle()
    .bigText("Much longer text that cannot fit one line..."))
     */
    setPriority(NotificationCompat.PRIORITY_DEFAULT)


    addAction(0, "Settings", createBroadcastIntent(IPFSService.ACTION_SETTINGS, notificationID))

  }

  @MainThread
  fun showNotification(
    title: String? = null,
    contentText: String? = null,
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
