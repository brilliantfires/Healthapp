package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.NutritionRecordDAO
import com.example.healthapp.data.entity.NutritionRecord
import com.example.healthapp.data.mysql.RetrofitClient.apiService
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class NutritionRecordRepository(private val nutritionRecordDAO: NutritionRecordDAO) {
    fun getNutritionRecordsByUserId(userId: Int): Flow<List<NutritionRecord>> {
        return nutritionRecordDAO.getNutritionRecordsByUserId(userId)
    }

    suspend fun getNutritionRecordsByUserIdN(userId: Int): List<NutritionRecord> {
        return nutritionRecordDAO.getNutritionRecordsByUserIdN(userId)
    }

    suspend fun insertNutritionRecord(nutritionRecord: NutritionRecord) {
        nutritionRecordDAO.insertNutritionRecord(nutritionRecord)
    }

    suspend fun updateNutritionRecord(nutritionRecord: NutritionRecord) {
        nutritionRecordDAO.updateNutritionRecord(nutritionRecord)
    }

    suspend fun deleteNutritionRecord(nutritionRecord: NutritionRecord) {
        nutritionRecordDAO.deleteNutritionRecord(nutritionRecord)
    }

    fun getNutritionRecordsByDateRange(
        userId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<NutritionRecord>> {
        return nutritionRecordDAO.getNutritionRecordsByDateRange(userId, startDate, endDate)
    }

    // 从MySQL更新数据
    suspend fun getNutritionRecordsAndStore(userId: Int) {
        val response = apiService.getNutritionRecordsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { nutritionRecords ->
                nutritionRecords.forEach { networkNutritionRecord ->
                    nutritionRecordDAO.insertNutritionRecord(networkNutritionRecord)
                }
            }
        }
    }

}
