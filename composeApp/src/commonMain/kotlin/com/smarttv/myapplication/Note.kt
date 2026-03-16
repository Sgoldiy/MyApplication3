package com.smarttv.myapplication

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val color: Long, // Color as Long for multiplatform
    val reminderTime: Long?, // New field for reminder
    val createdAt: Long,
    val updatedAt: Long
)
