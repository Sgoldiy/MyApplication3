package com.smarttv.myapplication

class NoOpNotificationManager : NotificationManager {
    override fun scheduleNotification(title: String, content: String, timeMillis: Long) {
        // No-op for WASM
    }

    override fun cancelNotification(id: String) {
        // No-op
    }
}

actual fun getNotificationManager(): NotificationManager = NoOpNotificationManager()
