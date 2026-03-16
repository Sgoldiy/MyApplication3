package com.smarttv.myapplication

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val color: Long,
    val reminderTime: Long?,
    val createdAt: Long,
    val updatedAt: Long,
    val isCompleted: Boolean = false
)
