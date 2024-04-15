package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.MedicationRecordDAO
import com.example.healthapp.data.entity.MedicationRecord
import kotlinx.coroutines.flow.Flow

class MedicationRecordRepository(private val medicationRecordDAO: MedicationRecordDAO) {
    val allMedicationRecords: Flow<List<MedicationRecord>> =
        medicationRecordDAO.getAllMedicationRecords()

    suspend fun insertMedicationRecord(medicationRecord: MedicationRecord) {
        medicationRecordDAO.insertMedicationRecord(medicationRecord)
    }

    suspend fun updateMedicationRecord(medicationRecord: MedicationRecord) {
        medicationRecordDAO.updateMedicationRecord(medicationRecord)
    }

    suspend fun deleteMedicationRecord(medicationRecord: MedicationRecord) {
        medicationRecordDAO.deleteMedicationRecord(medicationRecord)
    }

    fun getMedicationRecordById(medicationID: Int): Flow<MedicationRecord> {
        return medicationRecordDAO.getMedicationRecordById(medicationID)
    }
}


