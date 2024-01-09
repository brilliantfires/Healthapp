package com.example.healthapp.data.repository

import com.example.healthapp.data.dao.AdminDAO
import com.example.healthapp.data.entity.Admin
import kotlinx.coroutines.flow.Flow

class AdminRepository(private val adminDao: AdminDAO) {

    fun getAdminById(adminID: Int): Flow<Admin> = adminDao.getAdminById(adminID)

    fun getAllAdmins(): Flow<List<Admin>> = adminDao.getAllAdmins()

    suspend fun insertAdmin(admin: Admin) {
        adminDao.insert(admin)
    }

    suspend fun updateAdmin(admin: Admin) {
        adminDao.updateAdmin(admin)
    }

    suspend fun deleteAdmin(admin: Admin) {
        adminDao.deleteAdmin(admin)
    }
}
