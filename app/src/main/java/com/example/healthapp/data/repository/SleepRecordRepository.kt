package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.SleepRecordDAO
import com.example.healthapp.data.entity.SleepRecord
import com.example.healthapp.data.mysql.ApiService
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class SleepRecordRepository(
    private val apiService: ApiService,
    private val sleepRecordDAO: SleepRecordDAO
) {
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

    // 根据输入的日期获取对应的日期之前的数据
    fun getSleepRecordsFrom(startTime: LocalDateTime, userId: Int): Flow<List<SleepRecord>> {
        return sleepRecordDAO.getSleepRecordsFrom(startTime, userId)
    }

    // 根据useId来获取所有的睡眠数据
    fun getAllSleepRecordsByUserId(userId: Int): Flow<List<SleepRecord>> {
        return sleepRecordDAO.getAllSleepRecordsByUserId(userId)
    }

    suspend fun getAllSleepRecordsByUserIdN(userId: Int): List<SleepRecord> {
        return sleepRecordDAO.getAllSleepRecordsByUserIdN(userId)
    }

    // 使用网络协议Retrofit来读取MySQL中的数据，进行同步操作
    // 同步的逻辑是，判断userid相同，如果该日期已经存在就update数据，如果该日期未存在数据，就插入数据
    // 在更新数据的时候，其中有sleepId，需要防止sleepId冲突
    suspend fun getSleepRecordAndStore(userId: Int) {
        val response = apiService.getSleepRecordsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { sleepRecords ->
                sleepRecords.forEach { networkSleepRecord ->
                    val localSleepRecord = sleepRecordDAO.getSleepRecordByUserIdAndDate(
                        userId = networkSleepRecord.userID,
                        date = networkSleepRecord.date
                    )

                    if (localSleepRecord != null) {
                        // 更新现有记录
                        sleepRecordDAO.updateSleepLog(networkSleepRecord)
                    } else {
                        // 插入新记录
                        sleepRecordDAO.insert(networkSleepRecord)
                    }
                }
            }
        }
    }

}

