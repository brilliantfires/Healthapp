package com.example.healthapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admins")
data class Admin(
    @PrimaryKey(autoGenerate = true)
    val adminId: Int = 0,
    val username: String,
    val passwordHash: String
)
