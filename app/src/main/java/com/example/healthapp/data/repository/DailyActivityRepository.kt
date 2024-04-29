package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.DailyActivityDAO
import com.example.healthapp.data.entity.DailyActivity
import com.example.healthapp.data.mysql.RetrofitClient.apiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
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

    fun getAllActivitiesByUserId(userId: Int): Flow<List<DailyActivity>> {
        return dailyActivityDAO.getAllActivitiesByUserId(userId)
    }

    suspend fun getAllActivitiesByUserIdN(userId: Int): List<DailyActivity> {
        return dailyActivityDAO.getAllActivitiesByUserIdN(userId)
    }

    suspend fun getDailyActivitiesAndStore(userId: Int) {
        val response = apiService.getDailyActivitiesByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { dailyActivities ->
                dailyActivities.forEach { networkDailyActivity ->
                    val localDailyActivity = dailyActivityDAO.getDailyActivityByIdAndDate(
                        userId = networkDailyActivity.userID,
                        date = networkDailyActivity.date ?: LocalDateTime.now()
                    ).firstOrNull() // 这里使用 firstOrNull 获取第一个匹配的记录或null

                    if (localDailyActivity != null) {
                        // 更新现有记录
                        dailyActivityDAO.updateDailyActivity(networkDailyActivity)
                    } else {
                        // 插入新记录
                        dailyActivityDAO.insertDailyActivity(networkDailyActivity)
                    }
                }
            }
        } else {
            // 处理错误情况，例如记录错误日志、显示错误信息等
        }
    }
}