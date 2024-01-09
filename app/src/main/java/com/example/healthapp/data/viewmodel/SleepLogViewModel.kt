package com.example.healthapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.SleepLog
import com.example.healthapp.data.repository.SleepLogRepository
import kotlinx.coroutines.launch

class SleepLogViewModel(private val sleepLogRepository: SleepLogRepository) : ViewModel() {

    fun getSleepLogById(sleepID: Int): LiveData<SleepLog> = sleepLogRepository.getSleepLogById(sleepID).asLiveData()

    val allSleepLogs: LiveData<List<SleepLog>> = sleepLogRepository.getAllSleepLogs().asLiveData()

    fun insertSleepLog(sleepLog: SleepLog) {
        viewModelScope.launch {
            sleepLogRepository.insertSleepLog(sleepLog)
        }
    }

    fun updateSleepLog(sleepLog: SleepLog) {
        viewModelScope.launch {
            sleepLogRepository.updateSleepLog(sleepLog)
        }
    }

    fun deleteSleepLog(sleepLog: SleepLog) {
        viewModelScope.launch {
            sleepLogRepository.deleteSleepLog(sleepLog)
        }
    }
}
class SleepLogViewModelFactory(private val sleepLogRepository: SleepLogRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SleepLogViewModel(sleepLogRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
