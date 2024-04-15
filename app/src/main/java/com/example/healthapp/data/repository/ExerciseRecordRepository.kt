package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.ExerciseRecordDAO
import com.example.healthapp.data.entity.ExerciseRecord
import kotlinx.coroutines.flow.Flow

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
}
