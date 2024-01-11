package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userProfile: UserProfile)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(userProfiles: List<UserProfile>)
    @Update
    suspend fun updateUserProfile(userProfile: UserProfile)

    @Delete
    suspend fun deleteUserProfile(userProfile: UserProfile)

    //上面用表名user_profile
    @Query("SELECT * FROM user_profile WHERE userId = :userID")
    //这里函数用的是实体的类名UserProfile
    fun getUserProfileById(userID: Int): Flow<UserProfile>

    @Query("SELECT * FROM user_profile")
    fun getAllUserProfiles(): Flow<List<UserProfile>>

}