package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "dailyActivity",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DailyActivity(
    @PrimaryKey(autoGenerate = true)
    val activityID: Int = 0,

    @ColumnInfo(name = "userID")
    val userID: Int,

    @ColumnInfo(name = "date")
    val date: LocalDateTime,  // Assuming you are using 'java.time.LocalDate'

    @ColumnInfo(name = "steps")
    val steps: Int? = 0,

    @ColumnInfo(name = "walkingDistance")
    val walkingDistance: Double? = 0.0,  // Using 'Double' for DECIMAL type

    @ColumnInfo(name = "exerciseDuration")
    val exerciseDuration: String? = "",  // Assuming TIME format as String "HH:MM:SS"

    @ColumnInfo(name = "heartRate")
    val heartRate: Int? = 0,

    @ColumnInfo(name = "floorsClimbed")
    val floorsClimbed: Int? = 0,

    @ColumnInfo(name = "runningDistance")
    val runningDistance: Double? = 0.0,

    @ColumnInfo(name = "spO2")
    val spO2: Double? = 0.0,

    @ColumnInfo(name = "energyExpenditure")
    val energyExpenditure: Double? = 0.0,

    @ColumnInfo(name = "vitalCapacity")
    val vitalCapacity: Double? = 0.0
)
