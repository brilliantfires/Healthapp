package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.DailyActivity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface DailyActivityDAO {
    // suspend挂起函数是一种特殊类型的函数，它可以在执行长时间操作时暂停（挂起）其执行，而不会阻塞线程。
    // 添加参数 OnConflict 并为其赋值 OnConflictStrategy.IGNORE。参数 OnConflict 用于告知 Room 在发生冲突时应该执行的操作。
    // OnConflictStrategy.IGNORE 策略会忽略主键已存在于数据库中的新商品。
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDailyActivity(dailyActivity: DailyActivity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dailyActivities: List<DailyActivity>)

    @Update
    suspend fun updateDailyActivity(dailyActivity: DailyActivity)

    @Delete
    suspend fun deleteDailyActivity(dailyActivity: DailyActivity)

    @Query("SELECT * FROM dailyActivity WHERE userID = :userId ORDER BY date DESC")
    fun getActivitiesByUserId(userId: Int): Flow<List<DailyActivity>>

    @Query("SELECT * FROM dailyActivity WHERE activityID = :activityId")
    fun getDailyActivityById(activityId: Int): Flow<DailyActivity>

    @Query("SELECT * FROM dailyActivity")
    fun getAllDailyActivities(): Flow<List<DailyActivity>>

    @Query("SELECT * FROM dailyActivity WHERE userID=:userId AND date=:date")
    fun getDailyActivityByIdAndDate(userId: Int, date: LocalDateTime): Flow<DailyActivity>

    // 查找最近7条数据，而不是最近7天的数据
    @Query("SELECT * FROM dailyActivity WHERE userId = :userId ORDER BY date DESC LIMIT 7")
    fun getLastSevenActivities(userId: Int): Flow<List<DailyActivity>>

    // 该方法可以通过设置相应的日期，来实现获取对应的日期范围的数据，比如最近一年和最近1个月的数据
    @Query("SELECT * FROM dailyActivity WHERE userId = :userId AND date >= :startDate ORDER BY date")
    fun getActivitiesFrom(startDate: LocalDateTime, userId: Int): Flow<List<DailyActivity>>

}