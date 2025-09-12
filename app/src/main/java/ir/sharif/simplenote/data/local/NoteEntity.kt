package ir.sharif.simplenote.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.sharif.simplenote.model.Note

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val lastModified: Long,
    val serverId: Int? = null, // ID from Django backend
    val isSynced: Boolean = false, // Whether this note is synced with server
    val needsSync: Boolean = false, // Whether this note needs to be synced
    val isDeleted: Boolean = false // Whether this note is marked for deletion
) {
    fun toNote(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            lastModified = lastModified
        )
    }
    
    companion object {
        fun fromNote(note: Note, serverId: Int? = null, isSynced: Boolean = false): NoteEntity {
            return NoteEntity(
                id = note.id,
                title = note.title,
                content = note.content,
                lastModified = note.lastModified,
                serverId = serverId,
                isSynced = isSynced,
                needsSync = serverId == null, // New notes need sync
                isDeleted = false
            )
        }
    }
}
