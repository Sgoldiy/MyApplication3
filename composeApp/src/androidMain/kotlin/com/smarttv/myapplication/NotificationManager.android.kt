package com.smarttv.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.smarttv.myapplication.MainActivity

class AndroidNotificationManager(private val context: Context) : NotificationManager {
    override fun scheduleNotification(title: String, content: String, timeMillis: Long) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, "notes_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent)
    }

    override fun cancelNotification(id: String) {
        // Implement cancel if needed
    }
}

actual fun getNotificationManager(): NotificationManager = AndroidNotificationManager(MainActivity().applicationContext)
