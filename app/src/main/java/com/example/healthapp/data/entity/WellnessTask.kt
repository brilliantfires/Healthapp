package com.example.healthapp.data.entity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wellnessTask")
data class WellnessTask(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val taskLabel: String,
    val taskChecked: Boolean = false
)