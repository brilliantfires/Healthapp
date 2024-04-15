package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.SleepRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface SleepRecordDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sleepRecord: SleepRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sleepRecords: List<SleepRecord>)

    @Update
    suspend fun updateSleepLog(sleepRecord: SleepRecord)

    @Delete
    suspend fun deleteSleepLog(sleepRecord: SleepRecord)

    @Query("SELECT * FROM sleepRecord WHERE sleepID = :sleepID")
    fun getSleepLogById(sleepID: Int): Flow<SleepRecord>

    @Query("SELECT * FROM sleepRecord ORDER BY date DESC")
    fun getAllSleepLogs(): Flow<List<SleepRecord>>

    @Query("SELECT * FROM sleepRecord WHERE userID=:uerId AND date=:dateTime")
    fun getSleepRecordByIdAndDate(uerId: Int, dateTime: LocalDateTime): Flow<SleepRecord>

    @Query("SELECT * FROM sleepRecord WHERE userID=:userId ORDER BY date DESC")
    fun getSleepRecordByUserId(userId: Int): Flow<List<SleepRecord>>

    // 选择最近7条数据
    @Query("SELECT * FROM sleepRecord WHERE userID=:userId ORDER BY date DESC LIMIT 7")
    fun getLatestSevenSleepRecord(userId: Int): Flow<List<SleepRecord>>

}