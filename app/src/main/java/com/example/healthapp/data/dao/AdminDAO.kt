package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.Admin
import kotlinx.coroutines.flow.Flow

@Dao
interface AdminDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(admin: Admin)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(admins: List<Admin>)

    @Update
    suspend fun updateAdmin(admin: Admin)

    @Delete
    suspend fun deleteAdmin(admin: Admin)

    @Query("SELECT * FROM admins WHERE adminId = :adminID")
    fun getAdminById(adminID: Int): Flow<Admin>

    @Query("SELECT * FROM admins")
    fun getAllAdmins(): Flow<List<Admin>>
}