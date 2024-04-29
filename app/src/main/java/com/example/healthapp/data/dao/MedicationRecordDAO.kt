package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.MedicationRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface MedicationRecordDAO {
    // suspend挂起函数是一种特殊类型的函数，它可以在执行长时间操作时暂停（挂起）其执行，而不会阻塞线程。
    // 添加参数 OnConflict 并为其赋值 OnConflictStrategy.IGNORE。参数 OnConflict 用于告知 Room 在发生冲突时应该执行的操作。
    // OnConflictStrategy.IGNORE 策略会忽略主键已存在于数据库中的新商品。
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicationRecord(medicationRecord: MedicationRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicationRecords: List<MedicationRecord>)

    @Update
    suspend fun updateMedicationRecord(medicationRecord: MedicationRecord)

    @Delete
    suspend fun deleteMedicationRecord(medicationRecord: MedicationRecord)

    @Query("SELECT * FROM medicationRecord WHERE medicationID = :medicationID")
    fun getMedicationRecordById(medicationID: Int): Flow<MedicationRecord>

    @Query("SELECT * FROM medicationRecord")
    fun getAllMedicationRecords(): Flow<List<MedicationRecord>>

    @Query("SELECT * FROM medicationRecord")
    suspend fun getAllMedicationRecordsN(): List<MedicationRecord>

    // 根据userId来获取所有的用药记录数量
    @Query("SELECT * FROM medicationRecord WHERE userID=:userId")
    fun getMedicationRecordsCountByUserId(userId: Int): Flow<List<MedicationRecord>>

    @Query("SELECT * FROM medicationRecord WHERE userID=:userId")
    suspend fun getMedicationRecordsCountByUserIdN(userId: Int): List<MedicationRecord>

    @Query("SELECT * FROM medicationRecord WHERE userID=:userId AND date=:date")
    suspend fun getMedicationRecordsByUserIdAndDate(
        userId: Int,
        date: LocalDateTime
    ): MedicationRecord?
}