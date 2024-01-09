package com.example.healthapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.Admin
import com.example.healthapp.data.repository.AdminRepository
import kotlinx.coroutines.launch

class AdminViewModel(private val adminRepository: AdminRepository) : ViewModel() {

    fun getAdminById(adminID: Int): LiveData<Admin> = adminRepository.getAdminById(adminID).asLiveData()

    val allAdmins: LiveData<List<Admin>> = adminRepository.getAllAdmins().asLiveData()

    fun insertAdmin(admin: Admin) {
        viewModelScope.launch {
            adminRepository.insertAdmin(admin)
        }
    }

    fun updateAdmin(admin: Admin) {
        viewModelScope.launch {
            adminRepository.updateAdmin(admin)
        }
    }

    fun deleteAdmin(admin: Admin) {
        viewModelScope.launch {
            adminRepository.deleteAdmin(admin)
        }
    }
}
class AdminViewModelFactory(private val adminRepository: AdminRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminViewModel(adminRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
