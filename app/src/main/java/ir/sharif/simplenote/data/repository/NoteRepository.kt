package ir.sharif.simplenote.data.repository

import android.util.Log
import ir.sharif.simplenote.data.local.NoteDao
import ir.sharif.simplenote.data.local.NoteEntity
import ir.sharif.simplenote.data.model.NoteRequest
import ir.sharif.simplenote.data.model.NoteResponse
import ir.sharif.simplenote.data.network.ConnectivityManager
import ir.sharif.simplenote.data.services.NoteService
import ir.sharif.simplenote.data.sync.SyncManager
import ir.sharif.simplenote.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteRepository(
    private val noteDao: NoteDao,
    private val noteService: NoteService,
    private val connectivityManager: ConnectivityManager,
    private val syncManager: SyncManager,
    private val coroutineScope: CoroutineScope
) {
    val notes: Flow<List<Note>> = noteDao.getAllNotes().map { entities ->
        entities.map { it.toNote() }
    }

    suspend fun loadNotes() {
        // Notes are automatically loaded from local database via Flow
        // If online, trigger sync to get latest data from server
        if (connectivityManager.isOnline()) {
            coroutineScope.launch {
                syncManager.startSync()
            }
        }
    }

    suspend fun addOrUpdateNote(note: Note) {
        try {
            Log.d("NoteRepository", "Saving note locally: ${note.title}")
            
            // Check if this is an existing note
            val existingEntity = noteDao.getNoteById(note.id)
            
            val noteEntity = if (existingEntity != null) {
                // Update existing note
                existingEntity.copy(
                    title = note.title,
                    content = note.content,
                    lastModified = note.lastModified,
                    isSynced = false // Mark for sync
                )
            } else {
                // Create new note
                NoteEntity.fromNote(note, isSynced = false)
            }
            
            // Save to local database
            noteDao.insertNote(noteEntity)
            Log.d("NoteRepository", "Note saved locally: ${note.title}")
            
            // If online, try to sync immediately
            if (connectivityManager.isOnline()) {
                coroutineScope.launch {
                    syncManager.startSync()
                }
            }
            
        } catch (e: Exception) {
            Log.e("NoteRepository", "Exception saving note: ${e.message}", e)
        }
    }

    suspend fun getNoteById(id: String): Note? {
        return try {
            val noteEntity = noteDao.getNoteById(id)
            noteEntity?.toNote()
        } catch (e: Exception) {
            Log.e("NoteRepository", "Exception getting note by ID: ${e.message}", e)
            null
        }
    }

    suspend fun deleteNote(id: String) {
        try {
            Log.d("NoteRepository", "Deleting note with ID: $id")
            
            // Mark note as deleted in local database
            noteDao.markNoteAsDeleted(id)
            Log.d("NoteRepository", "Note marked as deleted locally: $id")
            
            // If online, try to sync immediately
            if (connectivityManager.isOnline()) {
                coroutineScope.launch {
                    syncManager.startSync()
                }
            }
            
        } catch (e: Exception) {
            Log.e("NoteRepository", "Exception deleting note: ${e.message}", e)
        }
    }

}
