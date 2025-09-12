package ir.sharif.simplenote.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.sharif.simplenote.data.model.UserInfo

@Entity(tableName = "user_info")
data class UserEntity(
    @PrimaryKey
    val id: Int = 1, // Single user per device, so we use a fixed ID
    val email: String,
    val name: String,
    val username: String,
    val isSynced: Boolean = false, // Whether this user info is synced with server
    val lastUpdated: Long = System.currentTimeMillis() // When this data was last updated
) {
    fun toUserInfo(): UserInfo {
        return UserInfo(
            email = email,
            name = name,
            username = username
        )
    }
    
    companion object {
        fun fromUserInfo(userInfo: UserInfo, isSynced: Boolean = false): UserEntity {
            return UserEntity(
                id = 1,
                email = userInfo.email,
                name = userInfo.name,
                username = userInfo.username,
                isSynced = isSynced,
                lastUpdated = System.currentTimeMillis()
            )
        }
    }
}
