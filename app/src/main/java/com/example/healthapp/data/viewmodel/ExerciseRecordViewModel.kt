package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.ExerciseRecord
import com.example.healthapp.data.repository.ExerciseRecordRepository
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
}

class ExerciseRecordModelFactory(private val exerciseRecordRepository: ExerciseRecordRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseRecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExerciseRecordViewModel(exerciseRecordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

