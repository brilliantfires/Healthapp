package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.HealthIndicatorDAO
import com.example.healthapp.data.entity.HealthIndicator
import com.example.healthapp.data.mysql.RetrofitClient
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class HealthIndicatorRepository(private val healthIndicatorDAO: HealthIndicatorDAO) {
    val allHealthIndicators: Flow<List<HealthIndicator>> = healthIndicatorDAO.getAllHealthMetrics()

    suspend fun insertHealthIndicator(healthIndicator: HealthIndicator) {
        healthIndicatorDAO.insert(healthIndicator)
    }

    suspend fun updateHealthIndicator(healthIndicator: HealthIndicator) {
        healthIndicatorDAO.updateHealthMetric(healthIndicator)
    }

    suspend fun deleteHealthIndicator(healthIndicator: HealthIndicator) {
        healthIndicatorDAO.deleteHealthMetric(healthIndicator)
    }

    fun getHealthIndicatorById(indicatorID: Int): Flow<HealthIndicator> {
        return healthIndicatorDAO.getHealthMetricById(indicatorID)
    }

    fun getHealthMetricByUserId(userId: Int): Flow<List<HealthIndicator>> {
        return healthIndicatorDAO.getHealthMetricByUserId(userId)
    }

    suspend fun getHealthMetricByUserIdN(userId: Int): List<HealthIndicator> {
        return healthIndicatorDAO.getHealthMetricByUserIdN(userId)
    }

    suspend fun getHealthIndicatorsAndStore(userId: Int) {
        val response = RetrofitClient.apiService.getHealthIndicatorsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { healthIndicators ->
                healthIndicators.forEach { networkHealthIndicator ->
                    val localHealthIndicator = healthIndicatorDAO.getHealthIndicatorByUserIdAndDate(
                        userId = networkHealthIndicator.userID,
                        date = networkHealthIndicator.date ?: LocalDateTime.now()
                    )

                    if (localHealthIndicator != null) {
                        // 如果本地数据库已有该条记录，则更新
                        healthIndicatorDAO.updateHealthMetric(networkHealthIndicator)
                    } else {
                        // 如果本地数据库没有该条记录，则插入新记录
                        healthIndicatorDAO.insert(networkHealthIndicator)
                    }
                }
            }
        } else {
        }
    }
}


