package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.DailyActivityDAO
import com.example.healthapp.data.entity.DailyActivity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class DailyActivityRepository(private val dailyActivityDAO: DailyActivityDAO) {

    val allDailyActivities: Flow<List<DailyActivity>> = dailyActivityDAO.getAllDailyActivities()

    suspend fun insertDailyActivity(dailyActivity: DailyActivity) {
        dailyActivityDAO.insertDailyActivity(dailyActivity)
    }

    suspend fun updateDailyActivity(dailyActivity: DailyActivity) {
        dailyActivityDAO.updateDailyActivity(dailyActivity)
    }

    suspend fun deleteDailyActivity(dailyActivity: DailyActivity) {
        dailyActivityDAO.deleteDailyActivity(dailyActivity)
    }

    fun getActivitiesByUserId(userId: Int): Flow<List<DailyActivity>> {
        return dailyActivityDAO.getActivitiesByUserId(userId)
    }

    fun getDailyActivityById(activityId: Int): Flow<DailyActivity> {
        return dailyActivityDAO.getDailyActivityById(activityId)
    }

    fun getDailyActivityByIdAndDate(userId: Int, date: LocalDateTime): Flow<DailyActivity> {
        return dailyActivityDAO.getDailyActivityByIdAndDate(userId, date)
    }

    // 查找最近7条数据
    fun getLastSevenActivities(userId: Int): Flow<List<DailyActivity>> {
        return dailyActivityDAO.getLastSevenActivities(userId)
    }

    // 该方法可以通过设置相应的日期，来实现获取对应的日期范围的数据，比如最近一年和最近1个月的数据
    fun getActivitiesFrom(startDate: LocalDateTime, userId: Int): Flow<List<DailyActivity>> {
        return dailyActivityDAO.getActivitiesFrom(startDate, userId)
    }
}