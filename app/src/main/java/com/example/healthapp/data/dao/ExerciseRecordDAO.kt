package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.ExerciseRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface ExerciseRecordDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exerciseRecord: ExerciseRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exerciseRecords: List<ExerciseRecord>)

    @Update
    suspend fun updateExerciseRecord(exerciseRecord: ExerciseRecord)

    @Delete
    suspend fun deleteExerciseRecord(exerciseRecord: ExerciseRecord)

    @Query("SELECT * FROM exerciseRecord WHERE exerciseID = :exerciseID")
    fun getExerciseRecordById(exerciseID: Int): Flow<ExerciseRecord>

    @Query("SELECT * FROM exerciseRecord")
    fun getAllExerciseRecords(): Flow<List<ExerciseRecord>>

    @Query("SELECT * FROM exerciseRecord")
    suspend fun getAllExerciseRecordsN(): List<ExerciseRecord>

    // 根据userId来获取锻炼记录
    @Query("SELECT * FROM exerciseRecord WHERE userID = :userId")
    fun getExerciseRecordByUserId(userId: Int): Flow<List<ExerciseRecord>>

    @Query("SELECT * FROM exerciseRecord WHERE userID = :userId")
    suspend fun getExerciseRecordByUserIdN(userId: Int): List<ExerciseRecord>

    @Query("SELECT * FROM exerciseRecord WHERE userID=:userId AND date=:date")
    suspend fun getExerciseRecordByUserIdAndDate(userId: Int, date: LocalDateTime): ExerciseRecord?
}