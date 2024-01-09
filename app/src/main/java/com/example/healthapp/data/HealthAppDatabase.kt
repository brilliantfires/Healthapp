package com.example.healthapp.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.healthapp.data.dao.ActivityLogDAO
import com.example.healthapp.data.dao.AdminDAO
import com.example.healthapp.data.dao.HealthMetricDAO
import com.example.healthapp.data.dao.SleepLogDAO
import com.example.healthapp.data.dao.UserDAO
import com.example.healthapp.data.entity.ActivityLog
import com.example.healthapp.data.entity.Admin
import com.example.healthapp.data.entity.HealthMetric
import com.example.healthapp.data.entity.SleepLog
import com.example.healthapp.data.entity.User
import com.example.healthapp.data.entity.UserProfile
import com.example.healthapp.data.dao.UserProfileDAO

//将 exportSchema 设置为 false，不会保留架构版本记录的备份
@Database(
    entities = [User::class, UserProfile::class, HealthMetric::class, ActivityLog::class, SleepLog::class, Admin::class],
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
    abstract fun admin(): AdminDAO

    companion object {
        //INSTANCE 变量将在数据库创建后保留对数据库的引用。
        //这有助于保持在任意时间点都只有一个打开的数据库实例，因为这种资源的创建和维护成本极高。
        //volatile 变量的值绝不会缓存，所有读写操作都将在主内存中完成。
        //这有助于确保 INSTANCE 的值始终是最新的值，并且对所有执行线程都相同。
        //也就是说，一个线程对 INSTANCE 所做的更改会立即对所有其他线程可见。
        @Volatile
        private var INSTANCE: HealthAppDatabase? = null

        fun getDatabase(context: Context): HealthAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthAppDatabase::class.java,
                    "health_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
