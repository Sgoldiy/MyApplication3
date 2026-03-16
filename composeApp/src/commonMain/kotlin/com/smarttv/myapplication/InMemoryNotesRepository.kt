package com.smarttv.myapplication

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryNotesRepository : NotesRepository {
    private val notes = MutableStateFlow<List<Note>>(emptyList())
    private var idCounter = 0L

    override suspend fun getAllNotes(): List<Note> = notes.value

    override suspend fun getNoteById(id: String): Note? = notes.value.find { it.id == id }

    override suspend fun addNote(note: Note) {
        notes.value = notes.value + note
    }

    override suspend fun updateNote(note: Note) {
        notes.value = notes.value.map { if (it.id == note.id) note else it }
    }

    override suspend fun deleteNote(id: String) {
        notes.value = notes.value.filter { it.id != id }
    }

    fun getNotesFlow(): Flow<List<Note>> = notes.asStateFlow()

    fun generateId(): String = (++idCounter).toString()
}
