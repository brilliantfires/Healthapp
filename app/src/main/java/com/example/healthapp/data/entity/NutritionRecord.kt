package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "nutritionRecord",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NutritionRecord(
    @PrimaryKey(autoGenerate = true)
    val nutritionID: Int = 0,
    @ColumnInfo(name = "userID")
    val userID: Int,
    @ColumnInfo(name = "date")
    val date: LocalDateTime?,
    @ColumnInfo(name = "mealType")
    val mealType: String,
    @ColumnInfo(name = "calories")
    val calories: Double,
    @ColumnInfo(name = "protein")
    val protein: Double,
    @ColumnInfo(name = "fat")
    val fat: Double,
    @ColumnInfo(name = "carbohydrates")
    val carbohydrates: Double
)
