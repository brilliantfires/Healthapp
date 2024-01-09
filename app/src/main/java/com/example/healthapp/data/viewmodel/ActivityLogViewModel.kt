package com.example.healthapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.ActivityLog
import com.example.healthapp.data.repository.ActivityLogRepository
import kotlinx.coroutines.launch

class ActivityLogViewModel(private val activityLogRepository: ActivityLogRepository) : ViewModel() {

    fun getActivityLogById(logID: Int): LiveData<ActivityLog> =
        activityLogRepository.getActivityLogById(logID).asLiveData()

    val allActivityLogs: LiveData<List<ActivityLog>> =
        activityLogRepository.getAllActivityLogs().asLiveData()

    fun insertActivityLog(activityLog: ActivityLog) {
        viewModelScope.launch {
            activityLogRepository.insertActivityLog(activityLog)
        }
    }

    fun updateActivityLog(activityLog: ActivityLog) {
        viewModelScope.launch {
            activityLogRepository.updateActivityLog(activityLog)
        }
    }

    fun deleteActivityLog(activityLog: ActivityLog) {
        viewModelScope.launch {
            activityLogRepository.deleteActivityLog(activityLog)
        }
    }
}

class ActivityLogViewModelFactory(private val activityLogRepository: ActivityLogRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivityLogViewModel(activityLogRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
