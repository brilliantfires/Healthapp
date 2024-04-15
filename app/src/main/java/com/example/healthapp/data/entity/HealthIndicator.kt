package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

// 健康指标记录表
@Entity(
    tableName = "healthIndicators",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HealthIndicator(
    @PrimaryKey(autoGenerate = true)
    val indicatorID: Int = 0,
    @ColumnInfo(name = "userID")
    val userID: Int,
    @ColumnInfo(name = "date")
    val date: LocalDateTime?,
    @ColumnInfo(name = "bloodPressure")
    val bloodPressure: String,
    @ColumnInfo(name = "cholesterol")
    val cholesterol: Double,
    @ColumnInfo(name = "glucoseLevel")
    val glucoseLevel: Double,
    @ColumnInfo(name = "otherIndicators")
    val otherIndicators: String?
)

