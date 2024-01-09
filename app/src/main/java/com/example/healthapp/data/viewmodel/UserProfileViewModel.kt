package com.example.healthapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.UserProfile
import com.example.healthapp.data.repository.UserProfileRepository
import kotlinx.coroutines.launch

class UserProfileViewModel(private val userProfileRepository: UserProfileRepository) : ViewModel() {

    fun getUserProfileById(userId: Int): LiveData<UserProfile> = userProfileRepository.getUserProfileById(userId).asLiveData()

    val allUserProfiles: LiveData<List<UserProfile>> = userProfileRepository.getAllUserProfiles().asLiveData()

    fun insertUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            userProfileRepository.insertUserProfile(userProfile)
        }
    }

    fun updateUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            userProfileRepository.updateUserProfile(userProfile)
        }
    }

    fun deleteUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            userProfileRepository.deleteUserProfile(userProfile)
        }
    }
}
class UserProfileViewModelFactory(private val userProfileRepository: UserProfileRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserProfileViewModel(userProfileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
