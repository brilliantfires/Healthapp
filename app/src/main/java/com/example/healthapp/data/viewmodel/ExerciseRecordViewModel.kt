package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.ExerciseRecord
import com.example.healthapp.data.repository.ExerciseRecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExerciseRecordViewModel(private val repository: ExerciseRecordRepository) : ViewModel() {

    val allExerciseRecords = repository.allExerciseRecords

    fun insertExerciseRecord(exerciseRecord: ExerciseRecord) = viewModelScope.launch {
        repository.insertExerciseRecord(exerciseRecord)
    }

    fun updateExerciseRecord(exerciseRecord: ExerciseRecord) = viewModelScope.launch {
        repository.updateExerciseRecord(exerciseRecord)
    }

    fun deleteExerciseRecord(exerciseRecord: ExerciseRecord) = viewModelScope.launch {
        repository.deleteExerciseRecord(exerciseRecord)
    }

    fun getExerciseRecordById(exerciseID: Int) = repository.getExerciseRecordById(exerciseID)

    // 根据userId获取所有锻炼数据
    fun getExerciseRecordByUserId(userId: Int): Flow<List<ExerciseRecord>> {
        return repository.getExerciseRecordByUserId(userId)
    }

    private val _exerciseRecords = MutableStateFlow<List<ExerciseRecord>>(emptyList())
    val exerciseRecords: StateFlow<List<ExerciseRecord>> = _exerciseRecords.asStateFlow()

    fun loadExerciseRecordsOnce(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val records = repository.getExerciseRecordByUserIdN(userId)
            _exerciseRecords.value = records
        }
    }

    fun getExerciseRecordsByUserIdAndStore(userId: Int) {
        viewModelScope.launch {
            repository.getExerciseRecordAndStore(userId)
        }
    }
}

class ExerciseRecordModelFactory(private val exerciseRecordRepository: ExerciseRecordRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseRecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseRecordViewModel(exerciseRecordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

