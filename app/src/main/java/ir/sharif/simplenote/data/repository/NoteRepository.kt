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
    private var currentUserId: String? = null
    
    val notes: Flow<List<Note>> = noteDao.getAllNotes().map { entities ->
        Log.d("NoteRepository", "Notes flow updated: ${entities.size} notes")
        entities.map { it.toNote() }
    }
    
    fun setCurrentUser(userId: String?) {
        currentUserId = userId
        Log.d("NoteRepository", "Current user set to: $userId")
    }
    
    fun getNotesForCurrentUser(): Flow<List<Note>> {
        return if (currentUserId != null) {
            noteDao.getNotesByUser(currentUserId!!).map { entities ->
                Log.d("NoteRepository", "Notes for user $currentUserId: ${entities.size} notes")
                entities.map { it.toNote() }
            }
        } else {
            noteDao.getAllNotes().map { entities ->
                Log.d("NoteRepository", "No current user - showing all notes: ${entities.size} notes")
                entities.map { it.toNote() }
            }
        }
    }

    suspend fun loadNotes() {
        Log.d("NoteRepository", "Loading notes...")
        // Notes are automatically loaded from local database via Flow
        // If online, trigger sync to get latest data from server
        if (connectivityManager.isOnline()) {
            Log.d("NoteRepository", "Online - triggering sync")
            coroutineScope.launch {
                syncManager.startSync()
            }
        } else {
            Log.d("NoteRepository", "Offline - using local data only")
        }
    }
    
    suspend fun forceSync() {
        Log.d("NoteRepository", "Force syncing notes...")
        if (connectivityManager.isOnline()) {
            coroutineScope.launch {
                syncManager.startSync()
            }
        } else {
            Log.w("NoteRepository", "Cannot sync - offline")
        }
    }
    
    suspend fun freshSync() {
        Log.d("NoteRepository", "Starting fresh sync with smart preservation...")
        try {
            // Use smart sync that preserves unsynced notes
            if (connectivityManager.isOnline()) {
                coroutineScope.launch {
                    syncManager.startSync()
                }
            } else {
                Log.w("NoteRepository", "Cannot sync - offline")
            }
        } catch (e: Exception) {
            Log.e("NoteRepository", "Failed to perform fresh sync: ${e.message}", e)
        }
    }
    
    suspend fun forceUploadAndSync() {
        Log.d("NoteRepository", "Force uploading local changes and syncing...")
        try {
            if (connectivityManager.isOnline()) {
                coroutineScope.launch {
                    // First upload any local changes
                    syncManager.uploadLocalChanges()
                    // Then sync to get latest from server
                    syncManager.startSync()
                }
            } else {
                Log.w("NoteRepository", "Cannot sync - offline")
            }
        } catch (e: Exception) {
            Log.e("NoteRepository", "Failed to force upload and sync: ${e.message}", e)
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
                    isSynced = false, // Mark for sync
                    userId = currentUserId
                )
            } else {
                // Create new note
                NoteEntity.fromNote(note, isSynced = false, userId = currentUserId)
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
    
    suspend fun clearAllNotes() {
        try {
            Log.d("NoteRepository", "Clearing all notes from local database")
            noteDao.clearAllNotes()
            Log.d("NoteRepository", "All notes cleared from local database")
        } catch (e: Exception) {
            Log.e("NoteRepository", "Exception clearing notes: ${e.message}", e)
        }
    }

}
