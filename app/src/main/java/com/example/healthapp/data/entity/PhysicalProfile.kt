package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "physicalProfile",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PhysicalProfile(
    @PrimaryKey
    val userID: Int,
    @ColumnInfo(name = "height")
    val height: Double, // DECIMAL in SQL maps to Double in Kotlin
    @ColumnInfo(name = "weight")
    val weight: Double,
    @ColumnInfo(name = "bmi")
    val bmi: Double,
    @ColumnInfo(name = "myopiaDegree")
    val myopiaDegree: Double?,
    @ColumnInfo(name = "wearsGlasses")
    val wearsGlasses: Boolean,
    @ColumnInfo(name = "bloodType")
    val bloodType: String, // Could use TypeConverter for ENUM-like behavior
    @ColumnInfo(name = "shoeSize")
    val shoeSize: Double
)


