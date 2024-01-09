package com.example.healthapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val userId: Int,
    val height: Float,
    val weight: Float,
    val age: Int,
    val gender: String
)
