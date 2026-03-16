package com.smarttv.myapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform