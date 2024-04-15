package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.PhysicalProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface PhysicalProfileDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(physicalProfile: PhysicalProfile)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(physicalProfiles: List<PhysicalProfile>)
    @Update
    suspend fun updateUserProfile(physicalProfile: PhysicalProfile)

    @Delete
    suspend fun deleteUserProfile(physicalProfile: PhysicalProfile)

    //上面用表名user_profile
    @Query("SELECT * FROM physicalProfile WHERE userID = :userID")
    //这里函数用的是实体的类名UserProfile
    fun getUserProfileById(userID: Int): Flow<PhysicalProfile>

    @Query("SELECT * FROM physicalProfile")
    fun getAllUserProfiles(): Flow<List<PhysicalProfile>>

}