package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.MedicationRecordDAO
import com.example.healthapp.data.entity.MedicationRecord
import com.example.healthapp.data.mysql.RetrofitClient
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

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

    // 获取用药记录的数量
    fun getMedicationRecordsCountByUserId(userId: Int): Flow<List<MedicationRecord>> {
        return medicationRecordDAO.getMedicationRecordsCountByUserId(userId)
    }

    suspend fun getMedicationRecordsCountByUserIdN(userId: Int): List<MedicationRecord> {
        return medicationRecordDAO.getMedicationRecordsCountByUserIdN(userId)
    }

    suspend fun getMedicationRecordsAndStore(userId: Int) {
        val response = RetrofitClient.apiService.getMedicationRecordsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { medicationRecords ->
                medicationRecords.forEach { networkMedicationRecord ->
                    val localHealthIndicator =
                        medicationRecordDAO.getMedicationRecordsByUserIdAndDate(
                            userId = networkMedicationRecord.userID,
                            date = networkMedicationRecord.date ?: LocalDateTime.now()
                        )

                    if (localHealthIndicator != null) {
                        // 如果本地数据库已有该条记录，则更新
                        medicationRecordDAO.updateMedicationRecord(networkMedicationRecord)
                    } else {
                        // 如果本地数据库没有该条记录，则插入新记录
                        medicationRecordDAO.insertMedicationRecord(networkMedicationRecord)
                    }
                }
            }
        } else {
        }
    }

}


