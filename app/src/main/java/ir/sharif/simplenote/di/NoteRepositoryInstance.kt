package ir.sharif.simplenote.di

import android.content.Context
import ir.sharif.simplenote.data.local.SimpleNoteDatabase
import ir.sharif.simplenote.data.network.ConnectivityManager
import ir.sharif.simplenote.data.repository.NoteRepository
import ir.sharif.simplenote.data.sync.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object NoteRepositoryInstance {
    private var _noteRepository: NoteRepository? = null
    
    fun initialize(context: Context) {
        DatabaseInstance.initialize(context)
        val noteDao = DatabaseInstance.noteDao
        val connectivityManager = ConnectivityManager(context)
        val syncManager = SyncManager(
            noteDao = noteDao,
            noteService = RetrofitInstance.noteApi,
            connectivityManager = connectivityManager,
            coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
        
        _noteRepository = NoteRepository(
            noteDao = noteDao,
            noteService = RetrofitInstance.noteApi,
            connectivityManager = connectivityManager,
            syncManager = syncManager,
            coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
        
        // Start sync manager
        syncManager.startSync()
    }
    
    val noteRepository: NoteRepository
        get() = _noteRepository ?: throw IllegalStateException("NoteRepositoryInstance not initialized. Call initialize() first.")
}
