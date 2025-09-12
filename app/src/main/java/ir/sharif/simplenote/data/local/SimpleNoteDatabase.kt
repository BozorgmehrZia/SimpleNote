package ir.sharif.simplenote.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SimpleNoteDatabase : RoomDatabase() {
    
    abstract fun noteDao(): NoteDao
    
    companion object {
        @Volatile
        private var INSTANCE: SimpleNoteDatabase? = null
        
        fun getDatabase(context: Context): SimpleNoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SimpleNoteDatabase::class.java,
                    "simple_note_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
