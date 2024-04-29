//package com.example.healthapp
//
//import androidx.room.Room
//import androidx.test.platform.app.InstrumentationRegistry
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.healthapp.data.HealthAppDatabase
//import com.example.healthapp.data.dao.UserDAO
//import com.example.healthapp.data.entity.User
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.runBlocking
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.Assert.*
//import java.time.LocalDateTime
//
//@RunWith(AndroidJUnit4::class)
//class HealthAppDatabaseTest {
//
//    private lateinit var database: HealthAppDatabase
//    private lateinit var userDao: UserDAO
//    // 初始化其他 DAOs
//
//    @Before
//    fun createDb() {
//        val context = InstrumentationRegistry.getInstrumentation().targetContext
//        database = Room.inMemoryDatabaseBuilder(context, HealthAppDatabase::class.java)
//            .allowMainThreadQueries() // 允许在主线程上查询，仅用于测试
//            .build()
//        userDao = database.userDao()
//        // 初始化其他 DAOs
//    }
//
//    @After
//    fun closeDb() {
//        database.close()
//    }
//
//    @Test
//    fun insertAndGetUser() = runBlocking {
//        // 创建用户
//        val user = User(
//            username = "test01",
//            passwordHash = "123456",
//            email = "test@example.com",
//            dateCreated = LocalDateTime.now(),
//
//        )
//        userDao.insertUser(user) // 插入用户
//
//        // 获取用户
//        val retrievedUser = userDao.getUserById(user.userId).first()
//
//        // 验证所有用户字段
//        assertNotNull(retrievedUser) // 确保检索到的用户不为 null
//        assertNotEquals(0, retrievedUser?.userId) // 确保 userId 已经生成
//        assertEquals(user.username, retrievedUser?.username)
//        assertEquals(user.passwordHash, retrievedUser?.passwordHash)
//        assertEquals(user.email, retrievedUser?.email)
//        assertEquals(user.createDate, retrievedUser?.createDate)
//    }
//
//
//}
