package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "sleepRecord",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SleepRecord(
    @PrimaryKey(autoGenerate = true)
    val sleepID: Int = 0,
    @ColumnInfo(name = "userID")
    val userID: Int,
    @ColumnInfo(name = "date")
    val date: LocalDateTime, // Use a TypeConverter to handle Date
    @ColumnInfo(name = "totalDuration")
    val totalDuration: String? = "0:0:0", // TIME represented as String or Int, requires TypeConverter for complex representation
    @ColumnInfo(name = "deepSleep")
    val deepSleep: String? = "0:0:0",
    @ColumnInfo(name = "lightSleep")
    val lightSleep: String? = "0:0:0",
    @ColumnInfo(name = "remSleep")
    val remSleep: String? = "0:0:0",
    @ColumnInfo(name = "awakeDuration")
    val awakeDuration: String? = "0:0:0"
)

