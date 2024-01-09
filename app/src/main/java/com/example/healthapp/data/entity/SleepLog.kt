package com.example.healthapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sleep_logs")
data class SleepLog(
    @PrimaryKey(autoGenerate = true)
    val sleepId: Int = 0,
    val userId: Int,
    val date: String,
    val duration: Int,
    val quality: String?
)
