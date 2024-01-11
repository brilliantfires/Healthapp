package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.WellnessTaskDAO
import com.example.healthapp.data.entity.WellnessTask
import kotlinx.coroutines.flow.Flow

class WellnessTaskRepository(private val wellnessTaskDAO: WellnessTaskDAO) {
    val allTasks: Flow<List<WellnessTask>> = wellnessTaskDAO.getAllTasks()

    suspend fun insertTask(wellnessTask: WellnessTask) {
        wellnessTaskDAO.insertTask(wellnessTask)
    }

    suspend fun updateTask(wellnessTask: WellnessTask) {
        wellnessTaskDAO.updateTask(wellnessTask)
    }

    suspend fun deleteTask(wellnessTask: WellnessTask) {
        wellnessTaskDAO.deleteTask(wellnessTask)
    }

    fun getTaskById(taskId: Int): Flow<WellnessTask> = wellnessTaskDAO.getTaskById(taskId)
}