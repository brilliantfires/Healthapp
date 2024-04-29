package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.ExerciseRecordDAO
import com.example.healthapp.data.entity.ExerciseRecord
import com.example.healthapp.data.mysql.RetrofitClient.apiService
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class ExerciseRecordRepository(private val exerciseRecordDao: ExerciseRecordDAO) {

    fun getExerciseRecordById(logID: Int): Flow<ExerciseRecord> =
        exerciseRecordDao.getExerciseRecordById(logID)

    val allExerciseRecords: Flow<List<ExerciseRecord>> = exerciseRecordDao.getAllExerciseRecords()

    suspend fun insertExerciseRecord(exerciseRecord: ExerciseRecord) {
        exerciseRecordDao.insert(exerciseRecord)
    }

    suspend fun updateExerciseRecord(exerciseRecord: ExerciseRecord) {
        exerciseRecordDao.updateExerciseRecord(exerciseRecord)
    }

    suspend fun deleteExerciseRecord(exerciseRecord: ExerciseRecord) {
        exerciseRecordDao.deleteExerciseRecord(exerciseRecord)
    }

    // 根据userId获取锻炼数据
    fun getExerciseRecordByUserId(userId: Int): Flow<List<ExerciseRecord>> {
        return exerciseRecordDao.getExerciseRecordByUserId(userId)
    }

    suspend fun getExerciseRecordByUserIdN(userId: Int): List<ExerciseRecord> {
        return exerciseRecordDao.getExerciseRecordByUserIdN(userId)
    }

    suspend fun getExerciseRecordAndStore(userId: Int) {
        val response = apiService.getExerciseRecordsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { exerciseRecords ->
                exerciseRecords.forEach { networkExerciseRecord ->
                    // 处理网络数据中的null值
                    val safeNetworkRecord = networkExerciseRecord.copy(
                        date = networkExerciseRecord.date ?: LocalDateTime.now(), // 提供默认日期
                    )
                    val localExerciseRecord = exerciseRecordDao.getExerciseRecordByUserIdAndDate(
                        userId = safeNetworkRecord.userID,
                        date = safeNetworkRecord.date ?: LocalDateTime.now()
                    )

                    if (localExerciseRecord != null) {
                        // 更新现有记录
                        exerciseRecordDao.updateExerciseRecord(safeNetworkRecord)
                    } else {
                        // 插入新记录
                        exerciseRecordDao.insert(safeNetworkRecord)
                    }
                }
            }
        }
    }


}
