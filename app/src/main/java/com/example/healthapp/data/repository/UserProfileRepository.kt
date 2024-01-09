package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.UserProfileDAO
import com.example.healthapp.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

class UserProfileRepository(private val userProfileDao: UserProfileDAO) {

    fun getUserProfileById(userId: Int): Flow<UserProfile> = userProfileDao.getUserProfileById(userId)

    fun getAllUserProfiles(): Flow<List<UserProfile>> = userProfileDao.getAllUserProfiles()

    suspend fun insertUserProfile(userProfile: UserProfile) {
        userProfileDao.insert(userProfile)
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.updateUserProfile(userProfile)
    }

    suspend fun deleteUserProfile(userProfile: UserProfile) {
        userProfileDao.deleteUserProfile(userProfile)
    }
}
