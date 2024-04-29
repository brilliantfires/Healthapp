package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.PhysicalProfileDAO
import com.example.healthapp.data.entity.PhysicalProfile
import com.example.healthapp.data.mysql.ApiService
import kotlinx.coroutines.flow.Flow

class PhysicalProfileRepository(
    private val apiService: ApiService,
    private val physicalProfileDAO: PhysicalProfileDAO
) {

    val allPhysicalProfiles: Flow<List<PhysicalProfile>> = physicalProfileDAO.getAllUserProfiles()
    suspend fun getAllPhysicalProfiles(): List<PhysicalProfile> {
        return physicalProfileDAO.getAllUserProfilesN()
    }

    suspend fun insertPhysicalProfile(physicalProfile: PhysicalProfile) {
        physicalProfileDAO.insert(physicalProfile)
    }

    suspend fun updatePhysicalProfile(physicalProfile: PhysicalProfile) {
        val phy = physicalProfileDAO.getUserProfileByIdN(physicalProfile.userID)
        if (phy != null)
            physicalProfileDAO.updateUserProfile(physicalProfile)
        else physicalProfileDAO.insert(physicalProfile)
    }

    suspend fun deletePhysicalProfile(physicalProfile: PhysicalProfile) {
        physicalProfileDAO.deleteUserProfile(physicalProfile)
    }

    fun getPhysicalProfileById(userId: Int): Flow<PhysicalProfile> {
        return physicalProfileDAO.getUserProfileById(userId)
    }

    suspend fun getPhysicalProfileByIdN(userId: Int): PhysicalProfile? {
        return physicalProfileDAO.getUserProfileByIdN(userId)
    }

    // 进行数据同步操作
    suspend fun getPhysicalProfileAndStore(userId: Int) {
        val response = apiService.getPhysicalProfileById(userId)
        if (response.isSuccessful) {
            response.body()?.let { networkPhysicalProfile ->
                physicalProfileDAO.updateUserProfile(networkPhysicalProfile)
            }
        }
    }
}


