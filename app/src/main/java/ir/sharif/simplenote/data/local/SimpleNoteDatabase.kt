package ir.sharif.simplenote.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(
    entities = [NoteEntity::class, UserEntity::class],
    version = 3,
    exportSchema = false
)
abstract class SimpleNoteDatabase : RoomDatabase() {
    
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao
    
    companion object {
        @Volatile
        private var INSTANCE: SimpleNoteDatabase? = null
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS user_info (
                        id INTEGER NOT NULL PRIMARY KEY,
                        email TEXT NOT NULL,
                        name TEXT NOT NULL,
                        username TEXT NOT NULL,
                        isSynced INTEGER NOT NULL DEFAULT 0,
                        lastUpdated INTEGER NOT NULL
                    )
                """)
            }
        }
        
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    ALTER TABLE notes ADD COLUMN userId TEXT
                """)
            }
        }
        
        fun getDatabase(context: Context): SimpleNoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SimpleNoteDatabase::class.java,
                    "simple_note_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .fallbackToDestructiveMigration() // For development only
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
