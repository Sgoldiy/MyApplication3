package com.smarttv.myapplication

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.PrintWriter
import java.io.StringWriter

fun main() {
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        val sw = StringWriter()
        throwable.printStackTrace(PrintWriter(sw))
        System.err.println("Uncaught exception in thread ${thread.name}:")
        System.err.println(sw.toString())
    }

    try {
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "MyApplication",
            ) {
                App()
            }
        }
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}
