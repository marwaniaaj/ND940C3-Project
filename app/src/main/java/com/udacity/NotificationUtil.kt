package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, pendingIntent: PendingIntent) {

    // Build the notification
    val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.notification_channel_id)
    )
    // Set title, text, and icon builder
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)

    // Set content intent
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

    // Set action
            .addAction(
                    R.drawable.cloud_download,
                    applicationContext.getString(R.string.notification_button),
                    pendingIntent)

    // Set priority
            .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Deliver the notification
    notify(NOTIFICATION_ID, builder.build())
}

// Cancel all notification
fun NotificationManager.cancelNotification() {
    cancelAll()
}