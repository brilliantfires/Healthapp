package com.example.healthapp.data.repository

//Repository（数据仓库）是一个用于管理数据来源的类。
//它的主要作用是为应用的其余部分提供数据访问的抽象层。
//在Android应用中，Repository通常从本地数据库（如Room）、远程服务器、或两者获取数据。
import android.content.SharedPreferences
import com.example.healthapp.data.dao.UserDAO
import com.example.healthapp.data.entity.User
import com.example.healthapp.data.mysql.ApiService
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class UserRepository(
    private val apiService: ApiService,
    private val userDAO: UserDAO,
    private val sharedPreferences: SharedPreferences
) {

    val allUsers: Flow<List<User>> = userDAO.getAllUsers()

    suspend fun getAllUser(): List<User> {
        return userDAO.getAllUsersN()
    }

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

    suspend fun getUserByIdN(userId: Int): User? {
        return userDAO.getUserByIdN(userId)
    }

    // 从远程数据库读取数据更新到SQLite
    suspend fun fetchUserAndStore(userId: Int) {
        val response = apiService.getUserById(userId)
        if (response.isSuccessful) {
            response.body()?.let {
                updateUser(it)
            }
        } else {
            // Handle API error
        }
    }

    // 登录验证
    suspend fun loginUser(username: String, password: String): User? {
        val hashedPassword = hashPassword(password) // 假设已实现密码哈希
        val response = apiService.loginUser(username, hashedPassword) // 假设 API 支持此调用
        if (response.isSuccessful) {
            response.body()?.let {
                if (userDAO.getUserByIdN(it.userId) == null)
                    userDAO.insertUser(it) // 将用户信息存入本地数据库
                else
                    userDAO.updateUser(it)
                return it
            }
        }
        return null
    }

    private fun hashPassword(password: String): String {
        // 这里应用哈希算法处理密码，比如 SHA-256
        return password // 临时示例，请替换为真实的哈希处理代码
    }

    // 错误检测
    suspend fun errorResult(email: String): User? {
        val response = apiService.getUserByEmail(email)
        if (response.isSuccessful) {
            response.body()?.let {
                return it
            }
        } else {
            if (response.code() == 404) {
                return null
            }
        }
        return null
    }

    // 用户注册功能
    suspend fun registerUser(username: String, email: String, password: String): Boolean {
        val newUser = User(
            username = username,
            email = email,
            phoneNumber = "",
            passwordHash = password,
            profilePicture = "/images/profiles/user1",
            role = "USER",
            bloodType = "A",
            gender = "MALE",
            dateOfBirth = LocalDateTime.of(
                1970,
                1,
                1,
                0,
                0
            ),
            dateCreated = LocalDateTime.now(),
            lastLogin = LocalDateTime.now(),
            isWheelchairUser = false,
            skinType = "TYPE_II",
            heartRateAffectingDrugs = "null"
        )

        val response = apiService.createUser(newUser)
        if (response.isSuccessful) {
            response.body()?.let {
                userDAO.insertUser(newUser)  // Save the user received from the backend into the local database
                return true
            }
        }
        return false
    }

    // 用户密码找回功能 这里返回1代表设置成功，返回0代表不存在该账户
    suspend fun resetPassword(username: String, email: String, newPassword: String): Boolean {
        val response = apiService.updateUser(email, newPassword)
        if (!response.isSuccessful) {
            return false
        } else {
            val user = userDAO.getUserByEmail(email)
            userDAO.updateUser(user.copy(passwordHash = newPassword))
            return true
        }
    }

    fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt("USER_ID_KEY", userId).apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt("USER_ID_KEY", -1)  // 默认值为-1，表示未知或未登录
    }
}

