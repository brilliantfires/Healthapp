package com.example.healthapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey(autoGenerate = true)
    val logId: Int = 0,
    val userId: Int,
    val date: String,
    val activityType: String,
    //这里加上？是表示可以为null，可以安全的处理为null而不会出现应用崩溃
    val steps: Int?,
    val floors: Int?,
    val distance: Float?,
    val calories: Int?
)
