package ir.sharif.simplenote.ui.screens.notedetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.sharif.simplenote.data.repository.NoteRepository
import ir.sharif.simplenote.model.Note
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class NoteDetailViewModel( // We'll need to provide this via a ViewModelFactory later
    private val noteRepository: NoteRepository,
    internal val noteId: String? // Null if it's a new note
) : ViewModel() {

    var noteTitle by mutableStateOf("")
    var noteContent by mutableStateOf("")
    var lastUpdatedTimestamp by mutableStateOf(System.currentTimeMillis())

    private var currentNoteId: String? = noteId
    private var autoSaveJob: Job? = null

    init {
        if (noteId != null) {
            loadNoteData(noteId)
        }
    }

    private fun loadNoteData(id: String) {
        viewModelScope.launch {
            val note = noteRepository.getNoteById(id)
            if (note != null) {
                noteTitle = note.title
                noteContent = note.content
                lastUpdatedTimestamp = note.lastModified
                currentNoteId = note.id
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        noteTitle = newTitle
        triggerAutoSave()
    }

    fun onContentChange(newContent: String) {
        noteContent = newContent
        triggerAutoSave()
    }

    private fun triggerAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(500L) // Debounce: wait 500ms after last change before saving
            saveNote()
        }
    }

    private fun saveNote() {
        if (noteTitle.isBlank() && noteContent.isBlank()) {
            // Don't save empty notes
            return
        }

        // Generate a unique ID for new notes
        if (currentNoteId.isNullOrEmpty()) {
            currentNoteId = UUID.randomUUID().toString()
        }

        val noteToSave = Note(
            id = currentNoteId!!, // Now we have a guaranteed unique ID
            title = noteTitle.trim(),
            content = noteContent.trim(),
            lastModified = System.currentTimeMillis()
        )
        
        viewModelScope.launch {
            noteRepository.addOrUpdateNote(noteToSave)
            lastUpdatedTimestamp = noteToSave.lastModified
        }
    }

    fun deleteNote() {
        currentNoteId?.let { id ->
            if (id.isNotEmpty()) {
                viewModelScope.launch {
                    noteRepository.deleteNote(id)
                    currentNoteId = null
                }
            }
        }
    }
}