package com.example.healthapp.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthapp.data.entity.User
import com.example.healthapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val allUsers: Flow<List<User>> = userRepository.allUsers

    fun insertUser(user: User) = viewModelScope.launch {
        userRepository.insertUser(user)
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        userRepository.deleteUser(user)
    }

    fun getUserById(userId: Int): Flow<User> {
        return userRepository.getUserById(userId)
    }

    private val theUser = MutableLiveData<User?>()
    val user = theUser
    fun getUserByIdN(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getUserByIdN(userId)
            theUser.postValue(user)
        }
    }

    // 从远程数据库读取数据更新到SQLite
    fun fetchUserAndStore(userId: Int) {
        viewModelScope.launch {
            userRepository.fetchUserAndStore(userId)
        }
    }

    // 用 MutableLiveData 来表示登录是否成功
    val loginSuccess = MutableLiveData<Boolean>()
    val theLoginUser = MutableLiveData<User?>()

    // 登录函数，现在只更新布尔状态
    fun loginUser(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.loginUser(username, password)
            // 直接更新 LiveData 的值
            loginSuccess.postValue(user != null)
            theLoginUser.postValue(user)
        }
    }

    // 用来存放出错的原因是什么
    val errorMessage = MutableLiveData<String>()
    fun errorResult(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.errorResult(email)
            if (result == null) {
                errorMessage.postValue("账户不存在！")
            } else {
                errorMessage.postValue("密码错误！")
            }
        }
    }

    val registrationStatus = MutableLiveData<Boolean>()

    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.registerUser(username, email, password)
            registrationStatus.postValue(result)
        }
    }

    val resetPasswordStatus = MutableLiveData<Boolean>()
    fun resetPassword(username: String, email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.resetPassword(username, email, password)
            resetPasswordStatus.postValue(result)
        }
    }

    val currentUserId = MutableLiveData<Int>()

    fun saveUserId(userId: Int) {
        userRepository.saveUserId(userId)
        currentUserId.value = userId  // 更新 LiveData，以便界面可以响应变化
    }

    fun loadUserId() {
        val userId = userRepository.getUserId()
        currentUserId.value = userId  // 更新 LiveData
    }
}

class UserViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}