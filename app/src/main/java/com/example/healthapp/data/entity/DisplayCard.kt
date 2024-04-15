package com.example.healthapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "displayCards")
data class DisplayCard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "cardName") val cardName: String,
    @ColumnInfo(name = "isDisplayed") val isDisplayed: Boolean
)
