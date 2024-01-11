package com.example.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.entity.ActivityLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(activityLog: ActivityLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(activityLogs:List<ActivityLog> )

    @Update
    suspend fun updateActivityLog(activityLog: ActivityLog)

    @Delete
    suspend fun deleteActivityLog(activityLog: ActivityLog)

    @Query("SELECT * FROM activity_logs WHERE logId = :logID")
    fun getActivityLogById(logID:Int): Flow<ActivityLog>

    @Query("SELECT * FROM activity_logs")
    fun getAllActivityLogs(): Flow<List<ActivityLog>>
}