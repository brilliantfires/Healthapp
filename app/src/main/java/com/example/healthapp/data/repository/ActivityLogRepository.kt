package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.ActivityLogDAO
import com.example.healthapp.data.entity.ActivityLog
import kotlinx.coroutines.flow.Flow

class ActivityLogRepository(private val activityLogDao: ActivityLogDAO) {

    fun getActivityLogById(logID: Int): Flow<ActivityLog> = activityLogDao.getActivityLogById(logID)

    fun getAllActivityLogs(): Flow<List<ActivityLog>> = activityLogDao.getAllActivityLogs()

    suspend fun insertActivityLog(activityLog: ActivityLog) {
        activityLogDao.insert(activityLog)
    }

    suspend fun updateActivityLog(activityLog: ActivityLog) {
        activityLogDao.updateActivityLog(activityLog)
    }

    suspend fun deleteActivityLog(activityLog: ActivityLog) {
        activityLogDao.deleteActivityLog(activityLog)
    }
}
