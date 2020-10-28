package com.example.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val NOTIFICATION_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buttons: Play, Pause, Backward, Forward
        btn_notification.setOnClickListener {
            createNotificationChannel(
                    this,
                    NotificationManager.IMPORTANCE_DEFAULT,
                    false,
                    getString(R.string.app_name),
                    "App notification channel"
            )
            pushNotification()
        }
    }

    /*
     * Create the NotificationChannel, but only on API 26+
     * because the NotificationChannel class is new and not in the support library
     */
    private fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean,
                                          channelName: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$channelName"
            val channel = NotificationChannel(channelId, channelName, importance).apply{
                description = channelDescription
                setShowBadge(showBadge)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun pushNotification() {
        val channelId = "$packageName-${getString(R.string.app_name)}"
        val title = "My Android Notification"
        val content = "Click to start new activity!"

        val intent = Intent(baseContext, NewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(baseContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(this).apply {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}