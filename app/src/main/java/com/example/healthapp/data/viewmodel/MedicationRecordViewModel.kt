package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.MedicationRecord
import com.example.healthapp.data.repository.MedicationRecordRepository
import kotlinx.coroutines.launch

class MedicationRecordViewModel(private val repository: MedicationRecordRepository) : ViewModel() {

    val allMedicationRecords = repository.allMedicationRecords

    fun insertMedicationRecord(medicationRecord: MedicationRecord) = viewModelScope.launch {
        repository.insertMedicationRecord(medicationRecord)
    }

    fun updateMedicationRecord(medicationRecord: MedicationRecord) = viewModelScope.launch {
        repository.updateMedicationRecord(medicationRecord)
    }

    fun deleteMedicationRecord(medicationRecord: MedicationRecord) = viewModelScope.launch {
        repository.deleteMedicationRecord(medicationRecord)
    }

    fun getMedicationRecordById(medicationID: Int) =
        repository.getMedicationRecordById(medicationID)
}


class MedicationRecordViewModelFactory(private val medicationRecordRepository: MedicationRecordRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicationRecordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicationRecordViewModel(medicationRecordRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
