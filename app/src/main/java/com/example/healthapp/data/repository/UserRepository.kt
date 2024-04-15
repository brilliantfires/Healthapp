package com.example.healthapp.data.repository

//Repository（数据仓库）是一个用于管理数据来源的类。
//它的主要作用是为应用的其余部分提供数据访问的抽象层。
//在Android应用中，Repository通常从本地数据库（如Room）、远程服务器、或两者获取数据。
import com.example.healthapp.data.dao.UserDAO
import com.example.healthapp.data.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDAO: UserDAO) {

    val allUsers: Flow<List<User>> = userDAO.getAllUsers()

    suspend fun insertUser(user: User) {
        userDAO.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDAO.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDAO.deleteUser(user)
    }

    fun getUserById(userId: Int): Flow<User> {
        return userDAO.getUserById(userId)
    }
}

