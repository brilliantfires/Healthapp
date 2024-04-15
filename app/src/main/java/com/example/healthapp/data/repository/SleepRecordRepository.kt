package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.SleepRecordDAO
import com.example.healthapp.data.entity.SleepRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class SleepRecordRepository(private val sleepRecordDAO: SleepRecordDAO) {
    val allSleepRecords: Flow<List<SleepRecord>> = sleepRecordDAO.getAllSleepLogs()

    suspend fun insertSleepRecord(sleepRecord: SleepRecord) {
        sleepRecordDAO.insert(sleepRecord)
    }

    suspend fun updateSleepRecord(sleepRecord: SleepRecord) {
        sleepRecordDAO.updateSleepLog(sleepRecord)
    }

    suspend fun deleteSleepRecord(sleepRecord: SleepRecord) {
        sleepRecordDAO.deleteSleepLog(sleepRecord)
    }

    fun getSleepRecordById(sleepID: Int): Flow<SleepRecord> {
        return sleepRecordDAO.getSleepLogById(sleepID)
    }

    fun getSleepRecordByIdAndDate(userId: Int, dateTime: LocalDateTime): Flow<SleepRecord> {
        return sleepRecordDAO.getSleepRecordByIdAndDate(userId, dateTime)
    }

    fun getSleepRecordByUserId(userId: Int): Flow<List<SleepRecord>> {
        return sleepRecordDAO.getSleepRecordByUserId(userId)
    }

    // 获取最近7条数据
    fun getLatestSevenSleepRecord(userId: Int): Flow<List<SleepRecord>> {
        return sleepRecordDAO.getLatestSevenSleepRecord(userId)
    }
}

