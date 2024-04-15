package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "medicationRecord",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicationRecord(
    @PrimaryKey(autoGenerate = true)
    val medicationID: Int = 0,
    @ColumnInfo(name = "userID")
    val userID: Int,
    @ColumnInfo(name = "date")
    val date: LocalDateTime?,
    @ColumnInfo(name = "drugName")
    val drugName: String,
    @ColumnInfo(name = "dosage")
    val dosage: String,
    @ColumnInfo(name = "frequency")
    val frequency: String,
    @ColumnInfo(name = "purpose")
    val purpose: String?
)
