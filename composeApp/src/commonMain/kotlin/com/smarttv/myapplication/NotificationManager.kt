package com.smarttv.myapplication

interface NotificationManager {
    fun scheduleNotification(title: String, content: String, timeMillis: Long)
    fun cancelNotification(id: String)
}

expect fun getNotificationManager(): NotificationManager
