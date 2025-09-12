package ir.sharif.simplenote.data.model

data class NoteResponse(
    val id: Int,
    val title: String,
    val description: String,
    val created_at: String,
    val updated_at: String,
    val creator_name: String?,
    val creator_username: String
)
