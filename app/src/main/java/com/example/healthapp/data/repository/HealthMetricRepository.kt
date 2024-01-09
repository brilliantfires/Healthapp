package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.HealthMetricDAO
import com.example.healthapp.data.entity.HealthMetric
import kotlinx.coroutines.flow.Flow

class HealthMetricRepository(private val healthMetricDao: HealthMetricDAO) {

    fun getHealthMetricById(metricID: Int): Flow<HealthMetric> = healthMetricDao.getHealthMetricById(metricID)

    fun getAllHealthMetrics(): Flow<List<HealthMetric>> = healthMetricDao.getAllHealthMetrics()

    suspend fun insertHealthMetric(healthMetric: HealthMetric) {
        healthMetricDao.insert(healthMetric)
    }

    suspend fun updateHealthMetric(healthMetric: HealthMetric) {
        healthMetricDao.updateHealthMetric(healthMetric)
    }

    suspend fun deleteHealthMetric(healthMetric: HealthMetric) {
        healthMetricDao.deleteHealthMetric(healthMetric)
    }
}
