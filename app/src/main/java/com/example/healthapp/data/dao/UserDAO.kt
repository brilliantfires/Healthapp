package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.User
import kotlinx.coroutines.flow.Flow

//接口
@Dao
interface UserDAO {
    // suspend挂起函数是一种特殊类型的函数，它可以在执行长时间操作时暂停（挂起）其执行，而不会阻塞线程。
    // 添加参数 OnConflict 并为其赋值 OnConflictStrategy.IGNORE。参数 OnConflict 用于告知 Room 在发生冲突时应该执行的操作。
    // OnConflictStrategy.IGNORE 策略会忽略主键已存在于数据库中的新商品。
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM Users WHERE userId = :userID")
    fun getUserById(userID: Int): Flow<User>

    @Query("SELECT * FROM Users WHERE userId = :userID")
    fun getUserByIdN(userID: Int): User?

    @Query("SELECT * FROM Users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM Users")
    suspend fun getAllUsersN(): List<User>

    @Query("SELECT * FROM Users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User
}