package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.HealthIndicatorDAO
import com.example.healthapp.data.entity.HealthIndicator
import kotlinx.coroutines.flow.Flow

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
}


