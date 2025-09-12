package ir.sharif.simplenote.di

import android.content.Context
import ir.sharif.simplenote.data.local.SimpleNoteDatabase

object DatabaseInstance {
    private var _database: SimpleNoteDatabase? = null
    
    fun initialize(context: Context) {
        _database = SimpleNoteDatabase.getDatabase(context)
    }
    
    val database: SimpleNoteDatabase
        get() = _database ?: throw IllegalStateException("DatabaseInstance not initialized. Call initialize() first.")
    
    val userDao
        get() = database.userDao()
    
    val noteDao
        get() = database.noteDao()
}
