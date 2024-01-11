package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.HealthMetric
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthMetricDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(healthMetric: HealthMetric)

    @Update
    suspend fun updateHealthMetric(healthMetric: HealthMetric)

    @Delete
    suspend fun deleteHealthMetric(healthMetric: HealthMetric)

    @Query("SELECT * FROM health_metrics WHERE metricId = :metricID")
    fun getHealthMetricById(metricID: Int): Flow<HealthMetric>

    @Query("SELECT * FROM health_metrics")
    fun getAllHealthMetrics(): Flow<List<HealthMetric>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(metrics: List<HealthMetric>)
}