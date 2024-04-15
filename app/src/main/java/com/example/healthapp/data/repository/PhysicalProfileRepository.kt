package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.PhysicalProfileDAO
import com.example.healthapp.data.entity.PhysicalProfile
import kotlinx.coroutines.flow.Flow

class PhysicalProfileRepository(private val physicalProfileDAO: PhysicalProfileDAO) {

    val allPhysicalProfiles: Flow<List<PhysicalProfile>> = physicalProfileDAO.getAllUserProfiles()

    suspend fun insertPhysicalProfile(physicalProfile: PhysicalProfile) {
        physicalProfileDAO.insert(physicalProfile)
    }

    suspend fun updatePhysicalProfile(physicalProfile: PhysicalProfile) {
        physicalProfileDAO.updateUserProfile(physicalProfile)
    }

    suspend fun deletePhysicalProfile(physicalProfile: PhysicalProfile) {
        physicalProfileDAO.deleteUserProfile(physicalProfile)
    }

    fun getPhysicalProfileById(userId: Int): Flow<PhysicalProfile> {
        return physicalProfileDAO.getUserProfileById(userId)
    }
}

