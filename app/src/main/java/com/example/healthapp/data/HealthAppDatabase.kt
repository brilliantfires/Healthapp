package com.example.healthapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.healthapp.data.dao.ActivityLogDAO
import com.example.healthapp.data.dao.AdminDAO
import com.example.healthapp.data.dao.HealthMetricDAO
import com.example.healthapp.data.dao.SleepLogDAO
import com.example.healthapp.data.dao.UserDAO
import com.example.healthapp.data.dao.UserProfileDAO
import com.example.healthapp.data.dao.WellnessTaskDAO
import com.example.healthapp.data.entity.ActivityLog
import com.example.healthapp.data.entity.Admin
import com.example.healthapp.data.entity.HealthMetric
import com.example.healthapp.data.entity.SleepLog
import com.example.healthapp.data.entity.User
import com.example.healthapp.data.entity.UserProfile
import com.example.healthapp.data.entity.WellnessTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//将 exportSchema 设置为 false，不会保留架构版本记录的备份
@Database(
    entities = [
        User::class,
        UserProfile::class,
        HealthMetric::class,
        ActivityLog::class,
        SleepLog::class,
        Admin::class,
        WellnessTask::class],
    version = 1,
    exportSchema = false
)
//定义的类是抽象类，因为 Room 会为您创建实现。
//@Database 注解
//@Database 是一个注解，用于标识某个类作为 Room 数据库的主要访问点。
//在 Room 中，数据库被表示为一个继承自 RoomDatabase 的抽象类。
//这个类定义了数据库中的表（实体）和用于访问这些表的数据访问对象（DAOs）。
//exportSchema：此参数确定是否将数据库的'架构'信息导出到特定的文件夹。
// 设置为 false 表示不导出这些信息。这对于跟踪和版本控制数据库的架构变化有用，但在某些情况下可能不需要。
abstract class HealthAppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun userProfileDao(): UserProfileDAO
    abstract fun healthMetricDao(): HealthMetricDAO
    abstract fun activityLogDao(): ActivityLogDAO
    abstract fun sleepLogDao(): SleepLogDAO
    abstract fun adminDAO(): AdminDAO
    abstract fun wellnessTaskDAO(): WellnessTaskDAO


    companion object {
        //INSTANCE 变量将在数据库创建后保留对数据库的引用。
        //这有助于保持在任意时间点都只有一个打开的数据库实例，因为这种资源的创建和维护成本极高。
        //volatile 变量的值绝不会缓存，所有读写操作都将在主内存中完成。
        //这有助于确保 INSTANCE 的值始终是最新的值，并且对所有执行线程都相同。
        //也就是说，一个线程对 INSTANCE 所做的更改会立即对所有其他线程可见。
        @Volatile
        private var INSTANCE: HealthAppDatabase? = null

        // 为什么要更新 getDatabase？ 因为需要在实例化数据库之后，向数据库中写入必要的数据
        fun getDatabase(context: Context): HealthAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthAppDatabase::class.java,
                    "health_database"
                )
                    .addCallback(roomDatabaseCallback) // 添加回调
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // 创建一个 RoomDatabase.Callback 实例，在数据库创建时调用
        private val roomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // 在协程中执行数据库填充
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { database ->
                        database.healthMetricDao().insertAll(provideInitialHealthMetric())
                        database.activityLogDao().insertAll(provideInitialActivityLogs())
                        database.adminDAO().insertAll(provideInitialAdmins())
                        database.sleepLogDao().insertAll(provideInitialSleepLogs())
                        database.userDao().insertAll(provideInitialUsers())
                        database.userProfileDao().insertAll(provideInitialUserProfiles())
                        database.wellnessTaskDAO().insertAll(provideInitialWellnessTask())
                    }
                }
            }
        }

        private fun provideInitialHealthMetric(): List<HealthMetric> {
            return listOf(
                // 示例数据
                HealthMetric(userId = 1, date = "2024-01-01", type = "HeartRate", value = "72"),
                HealthMetric(
                    userId = 2,
                    date = "2024-01-02",
                    type = "BloodPressure",
                    value = "120/80"
                ),
                HealthMetric(userId = 3, date = "2024-01-01", type = "Steps", value = "10000"),
                HealthMetric(userId = 4, date = "2024-01-03", type = "SleepHours", value = "8"),
                HealthMetric(
                    userId = 5,
                    date = "2024-01-02",
                    type = "CaloriesBurned",
                    value = "500"
                ),
                HealthMetric(userId = 6, date = "2024-01-04", type = "BloodGlucose", value = "5.6"),
                HealthMetric(
                    userId = 7,
                    date = "2024-01-05",
                    type = "BodyTemperature",
                    value = "36.6"
                ),
                HealthMetric(
                    userId = 8,
                    date = "2024-01-03",
                    type = "OxygenSaturation",
                    value = "98%"
                )
            )
        }

        private fun provideInitialActivityLogs(): List<ActivityLog> {
            return listOf(
                ActivityLog(
                    userId = 1,
                    date = "2024-01-01",
                    activityType = "Running",
                    steps = 1000,
                    floors = 10,
                    distance = 1.5f,
                    calories = 100
                ),
                ActivityLog(
                    userId = 2,
                    date = "2024-01-02",
                    activityType = "Walking",
                    steps = 3000,
                    floors = 5,
                    distance = 2.0f,
                    calories = 80
                ),
                ActivityLog(
                    userId = 1,
                    date = "2024-01-03",
                    activityType = "Cycling",
                    steps = 0,
                    floors = 0,
                    distance = 10.0f,
                    calories = 300
                ),
                ActivityLog(
                    userId = 3,
                    date = "2024-01-01",
                    activityType = "Swimming",
                    steps = 0,
                    floors = 0,
                    distance = 0.5f,
                    calories = 150
                ),
                ActivityLog(
                    userId = 2,
                    date = "2024-01-04",
                    activityType = "Yoga",
                    steps = 500,
                    floors = 0,
                    distance = 0f,
                    calories = 50
                ),
                ActivityLog(
                    userId = 1,
                    date = "2024-01-05",
                    activityType = "Hiking",
                    steps = 4000,
                    floors = 20,
                    distance = 5.0f,
                    calories = 400
                ),
                ActivityLog(
                    userId = 3,
                    date = "2024-01-02",
                    activityType = "Weight Training",
                    steps = 300,
                    floors = 0,
                    distance = 0f,
                    calories = 200
                )
            )
        }

        private fun provideInitialAdmins(): List<Admin> {
            return listOf(
                Admin(adminId = 1, username = "test01", passwordHash = "hashValue001"),
                Admin(adminId = 2, username = "adminUser02", passwordHash = "hashValue002"),
                Admin(adminId = 3, username = "superAdmin03", passwordHash = "hashValue003"),
                Admin(adminId = 4, username = "systemOperator04", passwordHash = "hashValue004"),
                Admin(adminId = 5, username = "networkAdmin05", passwordHash = "hashValue005"),
                Admin(adminId = 6, username = "userManagement06", passwordHash = "hashValue006"),
                Admin(adminId = 7, username = "databaseAdmin07", passwordHash = "hashValue007")
            )
        }

        private fun provideInitialSleepLogs(): List<SleepLog> {
            return listOf(
                SleepLog(
                    sleepId = 1,
                    userId = 1,
                    date = "2024-01-01",
                    duration = 480,
                    quality = "Good"
                ),
                SleepLog(
                    sleepId = 2,
                    userId = 2,
                    date = "2024-01-02",
                    duration = 420,
                    quality = "Average"
                ),
                SleepLog(
                    sleepId = 3,
                    userId = 1,
                    date = "2024-01-03",
                    duration = 360,
                    quality = "Poor"
                ),
                SleepLog(
                    sleepId = 4,
                    userId = 3,
                    date = "2024-01-04",
                    duration = 540,
                    quality = "Excellent"
                ),
                SleepLog(
                    sleepId = 5,
                    userId = 2,
                    date = "2024-01-05",
                    duration = 400,
                    quality = "Good"
                ),
                SleepLog(
                    sleepId = 6,
                    userId = 3,
                    date = "2024-01-01",
                    duration = 450,
                    quality = "Average"
                ),
                SleepLog(
                    sleepId = 7,
                    userId = 1,
                    date = "2024-01-02",
                    duration = 500,
                    quality = null
                )
            )
        }

        private fun provideInitialUsers(): List<User> {
            return listOf(
                User(
                    userId = 1,
                    username = "user01",
                    passwordHash = "hashValue001",
                    email = "user01@example.com",
                    createDate = "2023-12-01"
                ),
                User(
                    userId = 2,
                    username = "user02",
                    passwordHash = "hashValue002",
                    email = "user02@example.com",
                    createDate = "2023-12-10"
                ),
                User(
                    userId = 3,
                    username = "user03",
                    passwordHash = "hashValue003",
                    email = "user03@example.com",
                    createDate = "2023-12-15"
                ),
                User(
                    userId = 4,
                    username = "user04",
                    passwordHash = "hashValue004",
                    email = "user04@example.com",
                    createDate = "2024-01-01"
                ),
                User(
                    userId = 5,
                    username = "user05",
                    passwordHash = "hashValue005",
                    email = "user05@example.com",
                    createDate = "2024-01-05"
                ),
                User(
                    userId = 6,
                    username = "user06",
                    passwordHash = "hashValue006",
                    email = "user06@example.com",
                    createDate = "2024-01-10"
                )
            )
        }

        private fun provideInitialUserProfiles(): List<UserProfile> {
            return listOf(
                UserProfile(userId = 1, height = 175.5f, weight = 70.0f, age = 28, gender = "Male"),
                UserProfile(
                    userId = 2,
                    height = 162.0f,
                    weight = 55.0f,
                    age = 25,
                    gender = "Female"
                ),
                UserProfile(userId = 3, height = 180.0f, weight = 80.0f, age = 30, gender = "Male"),
                UserProfile(
                    userId = 4,
                    height = 158.0f,
                    weight = 48.0f,
                    age = 22,
                    gender = "Female"
                ),
                UserProfile(
                    userId = 5,
                    height = 170.0f,
                    weight = 65.0f,
                    age = 26,
                    gender = "Non-binary"
                )
            )
        }

        private fun provideInitialWellnessTask(): List<WellnessTask> {
            return listOf(
                WellnessTask(taskId = 1, taskLabel = "Morning Yoga", taskChecked = false),
                WellnessTask(taskId = 2, taskLabel = "10,000 Steps Walk", taskChecked = true),
                WellnessTask(taskId = 3, taskLabel = "Healthy Meal Plan", taskChecked = false),
                WellnessTask(taskId = 4, taskLabel = "8 Hours Sleep", taskChecked = true),
                WellnessTask(taskId = 5, taskLabel = "Meditation Session", taskChecked = false),
                WellnessTask(
                    taskId = 6,
                    taskLabel = "Hydration - Drink 2L Water",
                    taskChecked = true
                )
            )
        }
        //原 getDatabase 方法，这里只是用于初始化，而不用向数据库中插入相应的数据
        /*fun getDatabase(context: Context): HealthAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthAppDatabase::class.java,
                    "health_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }*/

    }
}


