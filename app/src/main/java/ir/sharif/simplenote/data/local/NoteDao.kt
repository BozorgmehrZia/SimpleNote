package ir.sharif.simplenote.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    
    @Query("SELECT * FROM notes WHERE isDeleted = 0 ORDER BY lastModified DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE userId = :userId AND isDeleted = 0 ORDER BY lastModified DESC")
    fun getNotesByUser(userId: String): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE id = :id AND isDeleted = 0")
    suspend fun getNoteById(id: String): NoteEntity?
    
    @Query("SELECT * FROM notes WHERE serverId = :serverId AND isDeleted = 0")
    suspend fun getNoteByServerId(serverId: Int): NoteEntity?
    
    @Query("SELECT * FROM notes WHERE userId = :userId AND (isSynced = 0 OR isDeleted = 1)")
    suspend fun getNotesNeedingSyncForUser(userId: String): List<NoteEntity>
    
    @Query("SELECT * FROM notes WHERE isSynced = 0 OR isDeleted = 1")
    suspend fun getNotesNeedingSync(): List<NoteEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)
    
    @Update
    suspend fun updateNote(note: NoteEntity)
    
    @Query("UPDATE notes SET isDeleted = 1, isSynced = 0 WHERE id = :id")
    suspend fun markNoteAsDeleted(id: String)
    
    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNotePermanently(id: String)
    
    @Query("DELETE FROM notes")
    suspend fun clearAllNotes()
}
