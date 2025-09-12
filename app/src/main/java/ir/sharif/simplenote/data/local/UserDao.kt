package ir.sharif.simplenote.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM user_info WHERE id = 1")
    suspend fun getUserInfo(): UserEntity?
    
    @Query("SELECT * FROM user_info WHERE id = 1")
    fun getUserInfoFlow(): Flow<UserEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userEntity: UserEntity)
    
    @Update
    suspend fun updateUserInfo(userEntity: UserEntity)
    
    @Query("DELETE FROM user_info WHERE id = 1")
    suspend fun deleteUserInfo()
    
    @Query("UPDATE user_info SET isSynced = :isSynced WHERE id = 1")
    suspend fun updateSyncStatus(isSynced: Boolean)
}
