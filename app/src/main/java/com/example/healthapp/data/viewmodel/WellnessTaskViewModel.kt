package com.example.healthapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.WellnessTask
import com.example.healthapp.data.repository.WellnessTaskRepository
import kotlinx.coroutines.launch

class WellnessTaskViewModel(private val repository: WellnessTaskRepository) : ViewModel() {

    // 使用 LiveData 包装 Flow，以便在 UI 中观察数据变化
    val allTasks: LiveData<List<WellnessTask>> = repository.allTasks.asLiveData()

    // 重载 insertTask 函数以接受不带 ID 的 WellnessTask
    fun insertTask(wellnessTask: WellnessTask) {
        viewModelScope.launch {
            try {
                repository.insertTask(wellnessTask)
                // 可以在这里更新一个 LiveData 以通知 UI 保存成功
            } catch (e: Exception) {
                // 处理错误
            }
        }
    }

    // 更新任务
    fun updateTask(wellnessTask: WellnessTask) {
        viewModelScope.launch {
            repository.updateTask(wellnessTask)
        }
    }

    // 删除任务
    fun deleteTask(wellnessTask: WellnessTask) {
        viewModelScope.launch {
            repository.deleteTask(wellnessTask)
        }
    }
}

class WellnessTaskViewModelFactory(private val repository: WellnessTaskRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WellnessTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WellnessTaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

