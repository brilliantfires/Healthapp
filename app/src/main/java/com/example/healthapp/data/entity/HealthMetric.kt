package com.example.healthapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "health_metrics")
data class HealthMetric(
    @PrimaryKey(autoGenerate = true)
    val metricId: Int = 0,
    val userId: Int,
    val date: String,
    val type: String,
    val value: String
)
