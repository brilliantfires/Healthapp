package com.example.healthapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.PhysicalProfile
import com.example.healthapp.data.repository.PhysicalProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhysicalProfileViewModel(private val repository: PhysicalProfileRepository) : ViewModel() {

    val allPhysicalProfiles: Flow<List<PhysicalProfile>> = repository.allPhysicalProfiles

    fun insertPhysicalProfile(physicalProfile: PhysicalProfile) = viewModelScope.launch {
        repository.insertPhysicalProfile(physicalProfile)
    }

    fun updatePhysicalProfile(physicalProfile: PhysicalProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePhysicalProfile(physicalProfile)
        }
    }

    fun deletePhysicalProfile(physicalProfile: PhysicalProfile) = viewModelScope.launch {
        repository.deletePhysicalProfile(physicalProfile)
    }

    fun getPhysicalProfileById(userId: Int): Flow<PhysicalProfile> {
        return repository.getPhysicalProfileById(userId)
    }

    private val _physicalProfile = MutableStateFlow<PhysicalProfile?>(null)
    val physicalProfile: StateFlow<PhysicalProfile?> = _physicalProfile.asStateFlow()

    fun loadPhysicalProfileOnce(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = repository.getPhysicalProfileByIdN(userId)
            _physicalProfile.value = profile
        }
    }

    fun getPhysicalProfileByUserIdAndStore(userId: Int) {
        viewModelScope.launch {
            repository.getPhysicalProfileAndStore(userId)
        }
    }
}

class PhysicalProfileViewModelFactory(private val physicalProfileRepository: PhysicalProfileRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhysicalProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhysicalProfileViewModel(physicalProfileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
