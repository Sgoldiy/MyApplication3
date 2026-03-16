package com.smarttv.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _notes.value = repository.getAllNotes()
        }
    }

    fun addNote(title: String, content: String, color: Long, reminderTime: Long?) {
        viewModelScope.launch {
            val note = Note(
                id = (repository as InMemoryNotesRepository).generateId(),
                title = title,
                content = content,
                color = color,
                reminderTime = reminderTime,
                createdAt = 0L,
                updatedAt = 0L
            )
            repository.addNote(note)
            reminderTime?.let {
                getNotificationManager().scheduleNotification(title, content, it)
            }
            loadNotes()
        }
    }

    fun updateNote(id: String, title: String, content: String, color: Long, reminderTime: Long?) {
        viewModelScope.launch {
            val existing = repository.getNoteById(id)
            if (existing != null) {
                val updated = existing.copy(
                    title = title,
                    content = content,
                    color = color,
                    reminderTime = reminderTime,
                    updatedAt = 0L
                )
                repository.updateNote(updated)
                reminderTime?.let {
                    getNotificationManager().scheduleNotification(title, content, it)
                }
                loadNotes()
            }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            repository.deleteNote(id)
            loadNotes()
        }
    }

    fun setCurrentNote(note: Note?) {
        _currentNote.value = note
    }
}
