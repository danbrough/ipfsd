package danbroid.ipfsd.service

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import danbroid.ipfsd.R
import org.slf4j.LoggerFactory


fun createNotification(
  context: Context,
  channelID: String,
  title: String,
  contentText: String?,
  @DrawableRes smallIcon: Int = R.drawable.ic_access_point,
) = NotificationCompat.Builder(context, channelID).apply {
  setSmallIcon(smallIcon)
  setOngoing(false)
  setContentTitle(title)
  if (contentText != null)
    setContentText(contentText)
  setOnlyAlertOnce(true)
  /**
   *         .setStyle(NotificationCompat.BigTextStyle()
  .bigText("Much longer text that cannot fit one line..."))
   */
  setPriority(NotificationCompat.PRIORITY_DEFAULT)

  val settingsIntent = Intent(Intent.ACTION_VIEW).let {
    it.data = "ipfsdemo://settings".toUri()
    PendingIntent.getActivity(context, 0, it, 0)
  }
  addAction(0, "Do Something", settingsIntent)


  setDeleteIntent(NotificationReceiver.createOnDismissedIntent(context))

}

class NotificationReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    log.trace("onReceive() $context : $intent")
    context.startService(IPFSService.createStopIntent(context))
  }

  companion object {
    fun createOnDismissedIntent(context: Context) =
      Intent(context, NotificationReceiver::class.java).let {
        PendingIntent.getBroadcast(context.applicationContext, notificationID, it, 0)
      }
  }
}


@MainThread
fun Service.createNotificationChannel(
  channelID: String,
  channelName: String,
  channelDescription: String,
  importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT
) {

  val channel = NotificationChannelCompat.Builder(channelID, importance).setName(channelName)
    .setDescription(channelDescription).build()

  val notificationManager = NotificationManagerCompat.from(this)
  notificationManager.createNotificationChannel(channel)


}

const val NOTIFICATION_ID = 19381
private var notificationID = 0

@MainThread
fun Service.showNotification(
  contentText: String? = null,
  builderConfig: (NotificationCompat.Builder.() -> Unit)? = null
) {
  if (notificationID == 0)
    notificationID = NOTIFICATION_ID

  val builder = createNotification(this, IPFSService.CHANNEL_ID, "IPFS", contentText)
  builderConfig?.invoke(builder)
  with(NotificationManagerCompat.from(this)) {
    notify(notificationID, builder.build())
  }
}

@MainThread
fun Service.cancelNotification() {
  if (notificationID != 0) {
    NotificationManagerCompat.from(this).cancel(notificationID)
    notificationID = 0
  }
}


private val log = LoggerFactory.getLogger("danbroid.ipfsd.service")

/*
// Create an explicit intent for an Activity in your app
val intent = Intent(this, AlertDetails::class.java).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
}
val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle("My notification")
        .setContentText("Hello World!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that will fire when the user taps the notification
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
 */