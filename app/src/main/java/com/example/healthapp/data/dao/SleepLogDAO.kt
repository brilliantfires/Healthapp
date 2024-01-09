package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.HealthMetric
import com.example.healthapp.data.entity.SleepLog
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepLogDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sleepLog: SleepLog)

    @Update
    suspend fun updateSleepLog(sleepLog: SleepLog)

    @Delete
    suspend fun deleteSleepLog(sleepLog: SleepLog)

    @Query("SELECT * FROM sleep_logs WHERE sleepId = :sleepID")
    fun getSleepLogById(sleepID: Int): Flow<SleepLog>

    @Query("SELECT * FROM sleep_logs")
    fun getAllSleepLogs(): Flow<List<SleepLog>>
}