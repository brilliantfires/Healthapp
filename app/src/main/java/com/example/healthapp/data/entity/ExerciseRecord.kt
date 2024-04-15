package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "exerciseRecord",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseRecord(
    @PrimaryKey(autoGenerate = true)
    val exerciseID: Int = 0,
    @ColumnInfo(name = "userID")
    val userID: Int,
    @ColumnInfo(name = "exerciseType")
    val exerciseType: String,
    @ColumnInfo(name = "duration")
    val duration: String, // TIME is best represented as String or Int (minutes/seconds), requires TypeConverter if complex representation is needed
    @ColumnInfo(name = "date")
    val date: LocalDateTime?, // Use a TypeConverter to handle Date
    @ColumnInfo(name = "distance")
    val distance: Double, // DECIMAL maps to Double
    @ColumnInfo(name = "energyExpenditure")
    val energyExpenditure: Double
)

