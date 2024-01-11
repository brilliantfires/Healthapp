package com.example.healthapp

import android.app.Application
import com.example.healthapp.data.HealthAppDatabase
import com.example.healthapp.data.repository.WellnessTaskRepository

class HealthApplication : Application() {

    // 延迟初始化 Repository
    val repository: WellnessTaskRepository by lazy {
        // 初始化数据库
        val database = HealthAppDatabase.getDatabase(this)
        // 从数据库获取 DAO，并用它来初始化 Repository
        WellnessTaskRepository(database.wellnessTaskDAO())
    }

    override fun onCreate() {
        super.onCreate()
        // 其他初始化代码（如果有的话）
    }
}