package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.HealthIndicator
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthIndicatorDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(healthIndicator: HealthIndicator)

    @Update
    suspend fun updateHealthMetric(healthIndicator: HealthIndicator)

    @Delete
    suspend fun deleteHealthMetric(healthIndicator: HealthIndicator)

    @Query("SELECT * FROM healthIndicators WHERE indicatorID = :indicatorID")
    fun getHealthMetricById(indicatorID: Int): Flow<HealthIndicator>

    @Query("SELECT * FROM healthIndicators")
    fun getAllHealthMetrics(): Flow<List<HealthIndicator>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(metrics: List<HealthIndicator>)
}