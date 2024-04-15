package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.NutritionRecord
import com.example.healthapp.data.repository.NutritionRecordRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class NutritionRecordViewModel(private val repository: NutritionRecordRepository) : ViewModel() {

    fun insertNutritionRecord(nutritionRecord: NutritionRecord) = viewModelScope.launch {
        repository.insertNutritionRecord(nutritionRecord)
    }

    fun updateNutritionRecord(nutritionRecord: NutritionRecord) = viewModelScope.launch {
        repository.updateNutritionRecord(nutritionRecord)
    }

    fun deleteNutritionRecord(nutritionRecord: NutritionRecord) = viewModelScope.launch {
        repository.deleteNutritionRecord(nutritionRecord)
    }

    fun getNutritionRecordsByUserId(userId: Int) = repository.getNutritionRecordsByUserId(userId)

    fun getNutritionRecordsByDateRange(
        userId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ) =
        repository.getNutritionRecordsByDateRange(userId, startDate, endDate)
}

class NutritionRecordViewModelFactory(private val nutritionRecordRepository: NutritionRecordRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NutritionRecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NutritionRecordViewModel(nutritionRecordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}