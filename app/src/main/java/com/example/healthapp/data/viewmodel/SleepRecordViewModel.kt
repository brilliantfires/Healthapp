package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.SleepRecord
import com.example.healthapp.data.repository.SleepRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class SleepRecordViewModel(private val repository: SleepRecordRepository) : ViewModel() {

    val allSleepRecords: Flow<List<SleepRecord>> = repository.allSleepRecords

    fun insertSleepRecord(sleepRecord: SleepRecord) = viewModelScope.launch {
        repository.insertSleepRecord(sleepRecord)
    }

    fun updateSleepRecord(sleepRecord: SleepRecord) = viewModelScope.launch {
        repository.updateSleepRecord(sleepRecord)
    }

    fun deleteSleepRecord(sleepRecord: SleepRecord) = viewModelScope.launch {
        repository.deleteSleepRecord(sleepRecord)
    }

    fun getSleepRecordById(id: Int): Flow<SleepRecord> {
        return repository.getSleepRecordById(id)
    }

    fun addOrUpdateTotalDuration(userId: Int, date: LocalDateTime, totalDuration: String) {
        viewModelScope.launch {
            val sleepRecord = repository.getSleepRecordByIdAndDate(userId, date).firstOrNull()

            if (sleepRecord != null) {
                // 如果存在记录，更新消耗能量
                val updatedSleepRecord = sleepRecord.copy(totalDuration = totalDuration)
                repository.updateSleepRecord(updatedSleepRecord)
            } else {
                // 如果不存在，创建新的记录并插入
                val newSleepRecord = SleepRecord(
                    userID = userId,
                    date = date,
                    totalDuration = totalDuration
                )
                repository.insertSleepRecord(newSleepRecord)
            }
        }
    }

    /*fun getSleepRecordByUserId(userId: Int): Flow<List<SleepRecord>> {
        viewModelScope.launch {
            repository.getSleepRecordByUserId(userId)
        }
    }*/

    // 添加一个函数，用于获取特定用户ID最近一天的步数信息
    fun getLatestSleepRecordByUserId(userId: Int): Flow<SleepRecord?> {
        // 使用map转换Flow<List<DailyActivity>>为Flow<DailyActivity?>，只取最近的一条记录
        return repository.getSleepRecordByUserId(userId).map { activities ->
            activities.firstOrNull()
        }
    }

    // 获取最近7条数据
    fun getLatestSevenSleepRecord(userId: Int): Flow<List<SleepRecord>> {
        return repository.getLatestSevenSleepRecord(userId)
    }
}

class SleepRecordViewModelFactory(private val sleepRecordRepository: SleepRecordRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepRecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return SleepRecordViewModel(sleepRecordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
