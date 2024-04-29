package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.NutritionRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface NutritionRecordDAO {
    // 插入一条新的营养记录
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutritionRecord(nutritionRecord: NutritionRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(nutritionRecords: List<NutritionRecord>)

    // 根据用户ID查询该用户的所有营养记录
    @Query("SELECT * FROM nutritionRecord WHERE userID = :userID")
    fun getNutritionRecordsByUserId(userID: Int): Flow<List<NutritionRecord>>

    @Query("SELECT * FROM nutritionRecord WHERE userID = :userID")
    suspend fun getNutritionRecordsByUserIdN(userID: Int): List<NutritionRecord>

    // 更新已有的营养记录
    @Update
    fun updateNutritionRecord(nutritionRecord: NutritionRecord)

    // 删除指定的营养记录
    @Delete
    fun deleteNutritionRecord(nutritionRecord: NutritionRecord)

    // 查询在特定日期范围内的营养记录
    @Query("SELECT * FROM nutritionRecord WHERE userID = :userID AND date BETWEEN :startDate AND :endDate")
    fun getNutritionRecordsByDateRange(
        userID: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<NutritionRecord>>

    // 查询特定用户和餐饮类型的营养记录
    @Query("SELECT * FROM nutritionRecord WHERE userID = :userID AND mealType = :mealType")
    fun getNutritionRecordsByMealType(userID: Int, mealType: String): Flow<List<NutritionRecord>>

    @Query("SELECT * FROM nutritionRecord WHERE userID = :userID AND date = :date")
    suspend fun getNutritionRecordByUserIdAndDate(
        userID: Int,
        date: LocalDateTime
    ): NutritionRecord?

    @Query("SELECT * FROM nutritionRecord")
    suspend fun getAllNutritionRecords(): List<NutritionRecord>
}
