package com.smarttv.myapplication

interface NotesRepository {
    suspend fun getAllNotes(): List<Note>
    suspend fun getNoteById(id: String): Note?
    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(id: String)
}
