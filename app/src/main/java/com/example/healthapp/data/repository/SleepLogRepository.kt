package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.SleepLogDAO
import com.example.healthapp.data.entity.SleepLog
import kotlinx.coroutines.flow.Flow

class SleepLogRepository(private val sleepLogDao: SleepLogDAO) {

    fun getSleepLogById(sleepID: Int): Flow<SleepLog> = sleepLogDao.getSleepLogById(sleepID)

    fun getAllSleepLogs(): Flow<List<SleepLog>> = sleepLogDao.getAllSleepLogs()

    suspend fun insertSleepLog(sleepLog: SleepLog) {
        sleepLogDao.insert(sleepLog)
    }

    suspend fun updateSleepLog(sleepLog: SleepLog) {
        sleepLogDao.updateSleepLog(sleepLog)
    }

    suspend fun deleteSleepLog(sleepLog: SleepLog) {
        sleepLogDao.deleteSleepLog(sleepLog)
    }
}
