package ir.sharif.simplenote.data.sync

import android.util.Log
import ir.sharif.simplenote.data.local.NoteDao
import ir.sharif.simplenote.data.local.NoteEntity
import ir.sharif.simplenote.data.network.ConnectivityManager
import ir.sharif.simplenote.data.services.NoteService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

class SyncManager(
    private val noteDao: NoteDao,
    private val noteService: NoteService,
    private val connectivityManager: ConnectivityManager,
    private val coroutineScope: CoroutineScope
) {
    
    fun startSync() {
        coroutineScope.launch {
            connectivityManager.getConnectivityFlow().collect { isOnline ->
                if (isOnline) {
                    Log.d("SyncManager", "Network is online, starting sync...")
                    syncData()
                } else {
                    Log.d("SyncManager", "Network is offline")
                }
            }
        }
    }
    
    private suspend fun syncData() {
        try {
            // 1. Upload local changes to server
            uploadLocalChanges()
            
            // 2. Download server changes to local
            downloadServerChanges()
            
            Log.d("SyncManager", "Sync completed successfully")
        } catch (e: Exception) {
            Log.e("SyncManager", "Sync failed: ${e.message}", e)
        }
    }
    
    private suspend fun uploadLocalChanges() {
        // Upload notes that need sync
        val notesToSync = noteDao.getNotesNeedingSync()
        Log.d("SyncManager", "Found ${notesToSync.size} notes to sync")
        
        for (noteEntity in notesToSync) {
            try {
                if (noteEntity.isDeleted) {
                    // Delete from server
                    noteEntity.serverId?.let { serverId ->
                        val response = noteService.deleteNote(serverId)
                        if (response.isSuccessful) {
                            noteDao.deleteNotePermanently(noteEntity.id)
                            Log.d("SyncManager", "Deleted note ${noteEntity.id} from server")
                        }
                    }
                } else {
                    // Create or update on server
                    val request = ir.sharif.simplenote.data.model.NoteRequest(
                        title = noteEntity.title,
                        description = noteEntity.content
                    )
                    
                    val response = if (noteEntity.serverId != null) {
                        // Update existing note
                        noteService.updateNote(noteEntity.serverId, request)
                    } else {
                        // Create new note
                        noteService.createNote(request)
                    }
                    
                    if (response.isSuccessful) {
                        val noteResponse = response.body()
                        if (noteResponse != null) {
                            if (noteEntity.serverId == null) {
                                // New note - update with server ID
                                noteDao.insertNote(noteEntity.copy(serverId = noteResponse.id, isSynced = true))
                            } else {
                                noteDao.insertNote(noteEntity.copy(isSynced = true))
                            }
                            Log.d("SyncManager", "Synced note ${noteEntity.id} to server")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("SyncManager", "Failed to sync note ${noteEntity.id}: ${e.message}")
            }
        }
        
        // Clean up deleted notes that were successfully synced
        val deletedNotes = noteDao.getNotesNeedingSync().filter { it.isDeleted }
        deletedNotes.forEach { deletedNote ->
            if (deletedNote.serverId != null) {
                // Try to delete from server
                try {
                    val response = noteService.deleteNote(deletedNote.serverId)
                    if (response.isSuccessful) {
                        noteDao.deleteNotePermanently(deletedNote.id)
                    }
                } catch (e: Exception) {
                    Log.e("SyncManager", "Failed to delete note from server: ${e.message}")
                }
            } else {
                // Local-only note, just delete permanently
                noteDao.deleteNotePermanently(deletedNote.id)
            }
        }
    }
    
    private suspend fun downloadServerChanges() {
        try {
            val response = noteService.getNotes()
            if (response.isSuccessful) {
                val serverNotes = response.body() ?: emptyList()
                Log.d("SyncManager", "Downloaded ${serverNotes.size} notes from server")
                
                // Convert server notes to entities and insert them
                serverNotes.forEach { noteResponse ->
                    val noteEntity = NoteEntity(
                        id = noteResponse.id.toString(),
                        serverId = noteResponse.id,
                        title = noteResponse.title,
                        content = noteResponse.description,
                        lastModified = parseDate(noteResponse.updated_at),
                        isSynced = true,
                        isDeleted = false
                    )
                    noteDao.insertNote(noteEntity)
                }
                Log.d("SyncManager", "Updated local database with server notes")
            }
        } catch (e: Exception) {
            Log.e("SyncManager", "Failed to download server changes: ${e.message}")
        }
    }
    
    private fun parseDate(dateString: String): Long {
        return try {
            val formatter = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", java.util.Locale.getDefault())
            formatter.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            Log.w("SyncManager", "Failed to parse date: $dateString", e)
            System.currentTimeMillis()
        }
    }
}
