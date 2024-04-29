package com.example.healthapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.healthapp.data.dao.ArticleMediaDAO
import com.example.healthapp.data.dao.ArticleTagRelationDAO
import com.example.healthapp.data.dao.ArticleTagsDAO
import com.example.healthapp.data.dao.ArticlesDAO
import com.example.healthapp.data.dao.AuthorsDAO
import com.example.healthapp.data.dao.DailyActivityDAO
import com.example.healthapp.data.dao.DisplayCardDao
import com.example.healthapp.data.dao.ExerciseRecordDAO
import com.example.healthapp.data.dao.HealthIndicatorDAO
import com.example.healthapp.data.dao.MedicationRecordDAO
import com.example.healthapp.data.dao.NutritionRecordDAO
import com.example.healthapp.data.dao.PhysicalProfileDAO
import com.example.healthapp.data.dao.SleepRecordDAO
import com.example.healthapp.data.dao.UserDAO
import com.example.healthapp.data.entity.Article
import com.example.healthapp.data.entity.ArticleMedia
import com.example.healthapp.data.entity.ArticleTag
import com.example.healthapp.data.entity.ArticleTagRelation
import com.example.healthapp.data.entity.Author
import com.example.healthapp.data.entity.DailyActivity
import com.example.healthapp.data.entity.DisplayCard
import com.example.healthapp.data.entity.ExerciseRecord
import com.example.healthapp.data.entity.HealthIndicator
import com.example.healthapp.data.entity.MedicationRecord
import com.example.healthapp.data.entity.NutritionRecord
import com.example.healthapp.data.entity.PhysicalProfile
import com.example.healthapp.data.entity.SleepRecord
import com.example.healthapp.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

//将 exportSchema 设置为 false，不会保留架构版本记录的备份
@Database(
    entities = [
        User::class,
        PhysicalProfile::class,
        HealthIndicator::class,
        ExerciseRecord::class,
        SleepRecord::class,
        NutritionRecord::class,
        MedicationRecord::class,
        Author::class,
        ArticleTagRelation::class,
        ArticleTag::class,
        ArticleMedia::class,
        Article::class,
        DailyActivity::class,
        DisplayCard::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
//定义的类是抽象类，因为 Room 会为您创建实现。
//@Database 注解
//@Database 是一个注解，用于标识某个类作为 Room 数据库的主要访问点。
//在 Room 中，数据库被表示为一个继承自 RoomDatabase 的抽象类。
//这个类定义了数据库中的表（实体）和用于访问这些表的数据访问对象（DAOs）。
//exportSchema：此参数确定是否将数据库的'架构'信息导出到特定的文件夹。
// 设置为 false 表示不导出这些信息。这对于跟踪和版本控制数据库的架构变化有用，但在某些情况下可能不需要。
abstract class HealthAppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun physicalProfileDao(): PhysicalProfileDAO
    abstract fun healthIndicationDao(): HealthIndicatorDAO
    abstract fun exerciseRecordDao(): ExerciseRecordDAO
    abstract fun sleepRecordDao(): SleepRecordDAO
    abstract fun nutritionRecordDao(): NutritionRecordDAO
    abstract fun authorDao(): AuthorsDAO
    abstract fun medicationRecordDao(): MedicationRecordDAO
    abstract fun articleTagsDao(): ArticleTagsDAO
    abstract fun articleTagRelationDao(): ArticleTagRelationDAO
    abstract fun articlesDao(): ArticlesDAO
    abstract fun articleMediaDAO(): ArticleMediaDAO
    abstract fun dailyActivityDAO(): DailyActivityDAO
    abstract fun displayCardDAO(): DisplayCardDao


    companion object {
        // INSTANCE 变量将在数据库创建后保留对数据库的引用。
        // 这有助于保持在任意时间点都只有一个打开的数据库实例，因为这种资源的创建和维护成本极高。
        // volatile 变量的值不会缓存，所有读写操作都将在主内存中完成。
        // 这有助于确保 INSTANCE 的值始终是最新的值，并且对所有执行线程都相同。
        // 也就是说，一个线程对 INSTANCE 所做的更改会立即对所有其他线程可见。
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
                        database.userDao().insertAll(provideInitialUsers())
                        database.sleepRecordDao().insertAll(provideInitialSleepRecords())
                        database.physicalProfileDao().insertAll(provideInitialPhysicalProfiles())
                        database.nutritionRecordDao().insertAll(provideInitialNutritionRecords())
                        database.medicationRecordDao().insertAll(provideInitialMedicationRecords())
                        database.healthIndicationDao().insertAll(provideInitialHealthIndicators())
                        database.exerciseRecordDao().insertAll(provideInitialExerciseRecords())
                        database.authorDao().insertAll(provideInitialAuthors())
                        database.articleTagsDao().insertAll(provideInitialArticleTags())
                        database.articlesDao().insertAll(provideInitialArticles())
                        database.articleMediaDAO().insertAll(provideInitialArticleMedia())
                        database.articleTagRelationDao()
                            .insertAll(provideInitialArticleTagRelations())
                        database.dailyActivityDAO().insertAll(provideInitialDailyActivities())
                        database.displayCardDAO().insertAll(provideInitialDisplayCards())
                    }
                }


            }
        }

        private fun provideInitialUsers(): List<User> {
            return listOf(
                User(
                    userId = 1,
                    username = "Android",
                    email = "user1@example.com",
                    phoneNumber = "13429891024",
                    passwordHash = "hash1",
                    profilePicture = "/images/profiles/user1",
                    role = "ADMIN",
                    bloodType = "A",
                    gender = "MALE",
                    dateOfBirth = LocalDateTime.of(
                        1990,
                        5,
                        15,
                        0,
                        0
                    ), // Convert to appropriate Date format
                    dateCreated = LocalDateTime.of(2024, 1, 30, 0, 0),
                    lastLogin = LocalDateTime.now(),
                    isWheelchairUser = false,
                    skinType = "TYPE_II",
                    heartRateAffectingDrugs = "null"
                ),
                User(
                    username = "User2",
                    email = "user2@example.com",
                    phoneNumber = "13512345678",
                    passwordHash = "hash2",
                    profilePicture = "/images/profiles/user2",
                    role = "ADMIN",
                    bloodType = "A",
                    gender = "FEMALE",
                    dateOfBirth = LocalDateTime.of(
                        2002,
                        5,
                        15,
                        0,
                        0
                    ), // Convert to appropriate Date format
                    dateCreated = LocalDateTime.of(2023, 5, 27, 0, 0),
                    lastLogin = LocalDateTime.now(),
                    isWheelchairUser = true,
                    skinType = null,
                    heartRateAffectingDrugs = "DrugA, DrugB"
                ), User(
                    username = "User3",
                    email = "user3@example.com",
                    phoneNumber = "13723456789",
                    passwordHash = "hash3",
                    profilePicture = "/images/profiles/user3",
                    role = "USER",
                    bloodType = "A",
                    gender = "MALE",
                    dateOfBirth = LocalDateTime.of(
                        1991, 6, 6, 9, 59
                    ),
                    dateCreated = LocalDateTime.of(
                        2023, 5, 16, 3, 17
                    ),
                    lastLogin = LocalDateTime.of(
                        2024, 4, 7, 3, 17
                    ),
                    isWheelchairUser = true,
                    skinType = "TYPE_V",
                    heartRateAffectingDrugs = "DrugA, DrugB"
                ), User(
                    username = "User4",
                    email = "user4@example.com",
                    phoneNumber = "15034567890",
                    passwordHash = "hash4",
                    profilePicture = "/images/profiles/user4",
                    role = "ADMIN",
                    bloodType = "O",
                    gender = "FEMALE",
                    dateOfBirth = LocalDateTime.of(1986, 6, 17, 3, 45),
                    dateCreated = LocalDateTime.of(2023, 11, 27, 3, 17),
                    lastLogin = LocalDateTime.of(2024, 4, 7, 3, 17),
                    isWheelchairUser = false,
                    skinType = "TYPE_I",
                    heartRateAffectingDrugs = "DrugC"
                ), User(
                    username = "User5",
                    email = "user5@example.com",
                    phoneNumber = "18756789012",
                    passwordHash = "hash5",
                    profilePicture = "/images/profiles/user5",
                    role = "USER",
                    bloodType = "O",
                    gender = "MALE",
                    dateOfBirth = LocalDateTime.of(2001, 5, 8, 18, 24),
                    dateCreated = LocalDateTime.of(2024, 4, 4, 3, 17),
                    lastLogin = LocalDateTime.of(2024, 4, 7, 3, 17),
                    isWheelchairUser = true,
                    skinType = "TYPE_I",
                    heartRateAffectingDrugs = "DrugD"
                ), User(
                    username = "User6",
                    email = "user6@example.com",
                    phoneNumber = "13378901234",
                    passwordHash = "hash6",
                    profilePicture = "/images/profiles/user6",
                    role = "ADMIN",
                    bloodType = "O",
                    gender = "FEMALE",
                    dateOfBirth = LocalDateTime.of(1989, 8, 5, 16, 5),
                    dateCreated = LocalDateTime.of(2024, 1, 15, 0, 17),
                    lastLogin = LocalDateTime.of(2024, 4, 7, 3, 17),
                    isWheelchairUser = true,
                    skinType = null,
                    heartRateAffectingDrugs = "DrugB, DrugD"
                ), User(
                    username = "User7",
                    email = "user7@example.com",
                    phoneNumber = "13889012345",
                    passwordHash = "hash7",
                    profilePicture = "/images/profiles/user7",
                    role = "MODERATOR",
                    bloodType = "A",
                    gender = "FEMALE",
                    dateOfBirth = LocalDateTime.of(1977, 10, 19, 15, 22),
                    dateCreated = LocalDateTime.of(2023, 4, 17, 3, 17),
                    lastLogin = LocalDateTime.of(2024, 4, 7, 3, 17),
                    isWheelchairUser = true,
                    skinType = "TYPE_V",
                    heartRateAffectingDrugs = "DrugC"
                ), User(
                    username = "User8",
                    email = "user8@example.com",
                    phoneNumber = "15290123456",
                    passwordHash = "hash8",
                    profilePicture = "/images/profiles/user8",
                    role = "USER",
                    bloodType = "AB",
                    gender = "FEMALE",
                    dateOfBirth = LocalDateTime.of(1981, 5, 2, 13, 20),
                    dateCreated = LocalDateTime.of(2023, 4, 29, 3, 17),
                    lastLogin = LocalDateTime.of(2024, 4, 7, 3, 17),
                    isWheelchairUser = false,
                    skinType = "TYPE_V",
                    heartRateAffectingDrugs = "DrugA, DrugB"
                ), User(
                    username = "User9",
                    email = "user9@example.com",
                    phoneNumber = "15801234567",
                    passwordHash = "hash9",
                    profilePicture = "/images/profiles/user9",
                    role = "MODERATOR",
                    bloodType = "B",
                    gender = "FEMALE",
                    dateOfBirth = LocalDateTime.of(2001, 8, 3, 2, 5),
                    dateCreated = LocalDateTime.of(2023, 12, 28, 3, 17),
                    lastLogin = LocalDateTime.of(2024, 4, 7, 3, 17),
                    isWheelchairUser = false,
                    skinType = null,
                    heartRateAffectingDrugs = "DrugB, DrugD"
                ), User(
                    username = "User10",
                    email = "user10@example.com",
                    phoneNumber = "13865033214",
                    passwordHash = "hash10",
                    profilePicture = "/images/profiles/user10",
                    role = "USER",
                    bloodType = "A",
                    gender = "MALE",
                    dateOfBirth = LocalDateTime.of(2004, 1, 1, 22, 43),
                    dateCreated = LocalDateTime.of(2023, 9, 7, 3, 17),
                    lastLogin = LocalDateTime.of(2024, 4, 7, 3, 17),
                    isWheelchairUser = false,
                    skinType = "TYPE_II",
                    heartRateAffectingDrugs = "DrugD"
                )
            )
        }

        private fun provideInitialSleepRecords(): List<SleepRecord> {
            return listOf(
                SleepRecord(
                    sleepID = 1,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 6, 0, 0),
                    totalDuration = "07:30:00",
                    deepSleep = "02:15:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:30:00",
                    awakeDuration = "00:45:00"
                ),
                SleepRecord(
                    sleepID = 2,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 5, 0, 0),
                    totalDuration = "08:00:00",
                    deepSleep = "02:00:00",
                    lightSleep = "04:00:00",
                    remSleep = "01:20:00",
                    awakeDuration = "00:40:00"
                ),
                SleepRecord(
                    sleepID = 3,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 4, 0, 0),
                    totalDuration = "06:45:00",
                    deepSleep = "02:05:00",
                    lightSleep = "02:40:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:50:00"
                ),
                SleepRecord(
                    sleepID = 4,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 3, 0, 0),
                    totalDuration = "07:20:00",
                    deepSleep = "01:50:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:25:00",
                    awakeDuration = "00:50:00"
                ),
                SleepRecord(
                    sleepID = 5,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 2, 0, 0),
                    totalDuration = "07:50:00",
                    deepSleep = "02:30:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:15:00",
                    awakeDuration = "00:55:00"
                ),
                SleepRecord(
                    sleepID = 6,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 1, 0, 0),
                    totalDuration = "08:15:00",
                    deepSleep = "02:40:00",
                    lightSleep = "03:45:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:40:00"
                ),
                SleepRecord(
                    sleepID = 7,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 31, 0, 0),
                    totalDuration = "06:30:00",
                    deepSleep = "01:45:00",
                    lightSleep = "02:50:00",
                    remSleep = "01:05:00",
                    awakeDuration = "00:50:00"
                ),
                SleepRecord(
                    sleepID = 8,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 30, 0, 0),
                    totalDuration = "07:55:00",
                    deepSleep = "02:25:00",
                    lightSleep = "03:30:00",
                    remSleep = "01:20:00",
                    awakeDuration = "00:40:00"
                ),
                SleepRecord(
                    sleepID = 9,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 29, 0, 0),
                    totalDuration = "06:40:00",
                    deepSleep = "02:10:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 10,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 28, 0, 0),
                    totalDuration = "07:10:00",
                    deepSleep = "02:20:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:05:00",
                    awakeDuration = "00:30:00"
                ), SleepRecord(
                    sleepID = 11,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 27, 0, 0),
                    totalDuration = "06:55:00",
                    deepSleep = "02:25:00",
                    lightSleep = "03:10:00",
                    remSleep = "00:50:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 12,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 26, 0, 0),
                    totalDuration = "07:35:00",
                    deepSleep = "02:40:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:35:00"
                ),
                SleepRecord(
                    sleepID = 13,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 25, 0, 0),
                    totalDuration = "06:50:00",
                    deepSleep = "02:10:00",
                    lightSleep = "03:05:00",
                    remSleep = "01:05:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 14,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 24, 0, 0),
                    totalDuration = "08:00:00",
                    deepSleep = "02:55:00",
                    lightSleep = "03:30:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:35:00"
                ),
                SleepRecord(
                    sleepID = 15,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 23, 0, 0),
                    totalDuration = "07:20:00",
                    deepSleep = "02:30:00",
                    lightSleep = "03:25:00",
                    remSleep = "01:05:00",
                    awakeDuration = "00:20:00"
                ),
                SleepRecord(
                    sleepID = 16,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 22, 0, 0),
                    totalDuration = "07:45:00",
                    deepSleep = "02:35:00",
                    lightSleep = "03:40:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 17,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 21, 0, 0),
                    totalDuration = "06:30:00",
                    deepSleep = "02:00:00",
                    lightSleep = "02:50:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 18,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 20, 0, 0),
                    totalDuration = "07:10:00",
                    deepSleep = "02:20:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 19,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 19, 0, 0),
                    totalDuration = "08:15:00",
                    deepSleep = "03:00:00",
                    lightSleep = "03:45:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 20,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 18, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "02:15:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 21,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 17, 0, 0),
                    totalDuration = "06:45:00",
                    deepSleep = "02:05:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 22,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 16, 0, 0),
                    totalDuration = "07:30:00",
                    deepSleep = "02:45:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 23,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 15, 0, 0),
                    totalDuration = "07:20:00",
                    deepSleep = "02:30:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 24,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 14, 0, 0),
                    totalDuration = "06:50:00",
                    deepSleep = "02:10:00",
                    lightSleep = "02:40:00",
                    remSleep = "01:00:00",
                    awakeDuration = "01:00:00"
                ),
                SleepRecord(
                    sleepID = 25,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 13, 0, 0),
                    totalDuration = "08:00:00",
                    deepSleep = "03:00:00",
                    lightSleep = "03:30:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 26,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 12, 0, 0),
                    totalDuration = "07:35:00",
                    deepSleep = "02:40:00",
                    lightSleep = "03:25:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 27,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 11, 0, 0),
                    totalDuration = "06:45:00",
                    deepSleep = "02:15:00",
                    lightSleep = "02:50:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 28,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 10, 0, 0),
                    totalDuration = "07:20:00",
                    deepSleep = "02:35:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 29,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 9, 0, 0),
                    totalDuration = "08:10:00",
                    deepSleep = "03:05:00",
                    lightSleep = "03:35:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 30,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 8, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "02:20:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 31,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 7, 0, 0),
                    totalDuration = "06:55:00",
                    deepSleep = "02:25:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 32,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 6, 0, 0),
                    totalDuration = "07:40:00",
                    deepSleep = "02:50:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 33,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 5, 0, 0),
                    totalDuration = "07:15:00",
                    deepSleep = "02:35:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 34,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 4, 0, 0),
                    totalDuration = "06:30:00",
                    deepSleep = "02:00:00",
                    lightSleep = "02:50:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 35,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 3, 0, 0),
                    totalDuration = "07:50:00",
                    deepSleep = "03:00:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 36,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 2, 0, 0),
                    totalDuration = "07:25:00",
                    deepSleep = "02:45:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 37,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 1, 0, 0),
                    totalDuration = "06:50:00",
                    deepSleep = "02:10:00",
                    lightSleep = "02:40:00",
                    remSleep = "01:30:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 38,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 29, 0, 0),
                    totalDuration = "08:05:00",
                    deepSleep = "03:15:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 39,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 28, 0, 0),
                    totalDuration = "07:15:00",
                    deepSleep = "02:30:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 40,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 27, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "02:20:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 41,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 26, 0, 0),
                    totalDuration = "06:45:00",
                    deepSleep = "02:05:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 42,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 25, 0, 0),
                    totalDuration = "07:30:00",
                    deepSleep = "02:45:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 43,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 24, 0, 0),
                    totalDuration = "07:20:00",
                    deepSleep = "02:30:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 44,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 23, 0, 0),
                    totalDuration = "06:50:00",
                    deepSleep = "02:10:00",
                    lightSleep = "02:40:00",
                    remSleep = "01:30:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 45,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 22, 0, 0),
                    totalDuration = "08:00:00",
                    deepSleep = "03:00:00",
                    lightSleep = "03:30:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 46,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 21, 0, 0),
                    totalDuration = "07:35:00",
                    deepSleep = "02:40:00",
                    lightSleep = "03:25:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 47,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 20, 0, 0),
                    totalDuration = "06:45:00",
                    deepSleep = "02:15:00",
                    lightSleep = "02:50:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 48,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 19, 0, 0),
                    totalDuration = "07:20:00",
                    deepSleep = "02:35:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 49,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 18, 0, 0),
                    totalDuration = "08:10:00",
                    deepSleep = "03:05:00",
                    lightSleep = "03:35:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 50,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 17, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "02:20:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 51,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 16, 0, 0),
                    totalDuration = "06:55:00",
                    deepSleep = "02:25:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 52,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 15, 0, 0),
                    totalDuration = "07:40:00",
                    deepSleep = "02:50:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 53,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 14, 0, 0),
                    totalDuration = "07:15:00",
                    deepSleep = "02:35:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 54,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 13, 0, 0),
                    totalDuration = "06:30:00",
                    deepSleep = "02:00:00",
                    lightSleep = "02:50:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 55,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 12, 0, 0),
                    totalDuration = "07:50:00",
                    deepSleep = "03:00:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 56,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 11, 0, 0),
                    totalDuration = "07:25:00",
                    deepSleep = "02:45:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 57,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 10, 0, 0),
                    totalDuration = "06:50:00",
                    deepSleep = "02:10:00",
                    lightSleep = "02:40:00",
                    remSleep = "01:30:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 58,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 9, 0, 0),
                    totalDuration = "08:05:00",
                    deepSleep = "03:15:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 59,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 8, 0, 0),
                    totalDuration = "07:15:00",
                    deepSleep = "02:30:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 60,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 7, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "02:20:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 61,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 6, 0, 0),
                    totalDuration = "06:45:00",
                    deepSleep = "02:05:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 62,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 5, 0, 0),
                    totalDuration = "07:30:00",
                    deepSleep = "02:45:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 63,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 4, 0, 0),
                    totalDuration = "07:20:00",
                    deepSleep = "02:30:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 64,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 3, 0, 0),
                    totalDuration = "06:50:00",
                    deepSleep = "02:10:00",
                    lightSleep = "02:40:00",
                    remSleep = "01:30:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 65,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 2, 0, 0),
                    totalDuration = "08:00:00",
                    deepSleep = "03:00:00",
                    lightSleep = "03:30:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 66,
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 1, 0, 0),
                    totalDuration = "07:35:00",
                    deepSleep = "02:40:00",
                    lightSleep = "03:25:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 67,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 31, 0, 0),
                    totalDuration = "06:45:00",
                    deepSleep = "02:15:00",
                    lightSleep = "02:50:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 68,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 30, 0, 0),
                    totalDuration = "07:20:00",
                    deepSleep = "02:35:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 69,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 29, 0, 0),
                    totalDuration = "08:10:00",
                    deepSleep = "03:05:00",
                    lightSleep = "03:35:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 70,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 28, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "02:20:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 71,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 27, 0, 0),
                    totalDuration = "06:55:00",
                    deepSleep = "02:25:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 72,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 26, 0, 0),
                    totalDuration = "07:40:00",
                    deepSleep = "02:50:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 73,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 25, 0, 0),
                    totalDuration = "07:15:00",
                    deepSleep = "02:35:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 74,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 24, 0, 0),
                    totalDuration = "06:30:00",
                    deepSleep = "02:00:00",
                    lightSleep = "02:50:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 75,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 23, 0, 0),
                    totalDuration = "07:50:00",
                    deepSleep = "03:00:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 76,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 22, 0, 0),
                    totalDuration = "07:25:00",
                    deepSleep = "02:45:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 77,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 21, 0, 0),
                    totalDuration = "06:50:00",
                    deepSleep = "02:10:00",
                    lightSleep = "02:40:00",
                    remSleep = "01:30:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 78,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 20, 0, 0),
                    totalDuration = "08:05:00",
                    deepSleep = "03:15:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 79,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 19, 0, 0),
                    totalDuration = "07:15:00",
                    deepSleep = "02:30:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 80,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 18, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "02:20:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 81,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 17, 0, 0),
                    totalDuration = "06:45:00",
                    deepSleep = "02:05:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 82,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 16, 0, 0),
                    totalDuration = "07:30:00",
                    deepSleep = "02:45:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 83,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 15, 0, 0),
                    totalDuration = "07:20:00",
                    deepSleep = "02:30:00",
                    lightSleep = "03:20:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 84,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 14, 0, 0),
                    totalDuration = "06:50:00",
                    deepSleep = "02:10:00",
                    lightSleep = "02:40:00",
                    remSleep = "01:30:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 85,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 13, 0, 0),
                    totalDuration = "08:00:00",
                    deepSleep = "03:00:00",
                    lightSleep = "03:30:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ), SleepRecord(
                    sleepID = 86,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 12, 0, 0),
                    totalDuration = "07:35:00",
                    deepSleep = "02:40:00",
                    lightSleep = "03:25:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 87,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 11, 0, 0),
                    totalDuration = "06:45:00",
                    deepSleep = "02:15:00",
                    lightSleep = "02:50:00",
                    remSleep = "01:10:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 88,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 10, 0, 0),
                    totalDuration = "07:20:00",
                    deepSleep = "02:35:00",
                    lightSleep = "03:15:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 89,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 9, 0, 0),
                    totalDuration = "08:10:00",
                    deepSleep = "03:05:00",
                    lightSleep = "03:35:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                ),
                SleepRecord(
                    sleepID = 90,
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 8, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "02:20:00",
                    lightSleep = "03:10:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:30:00"
                )
            )
        }

        private fun provideInitialPhysicalProfiles(): List<PhysicalProfile> {
            return listOf(
                PhysicalProfile(

                    userID = 1,
                    height = 187.0,
                    weight = 52.02,
                    bmi = 14.88,
                    myopiaDegree = null,
                    wearsGlasses = false,
                    bloodType = "B",
                    shoeSize = 43.9
                ),
                PhysicalProfile(

                    userID = 2,
                    height = 191.0,
                    weight = 86.48,
                    bmi = 23.71,
                    myopiaDegree = -0.88,
                    wearsGlasses = true,
                    bloodType = "A",
                    shoeSize = 45.6
                ),
                PhysicalProfile(

                    userID = 3,
                    height = 171.0,
                    weight = 76.47,
                    bmi = 26.15,
                    myopiaDegree = 0.0, // Since myopiaDegree was -0.0, adjusted to 0.0 for clarity
                    wearsGlasses = false,
                    bloodType = "O",
                    shoeSize = 40.2
                ),
                PhysicalProfile(

                    userID = 4,
                    height = 162.0,
                    weight = 79.08,
                    bmi = 30.13,
                    myopiaDegree = null,
                    wearsGlasses = false,
                    bloodType = "AB",
                    shoeSize = 38.6
                ),
                PhysicalProfile(

                    userID = 5,
                    height = 195.0,
                    weight = 95.73,
                    bmi = 25.18,
                    myopiaDegree = -4.65,
                    wearsGlasses = true,
                    bloodType = "A",
                    shoeSize = 44.9
                ),
                PhysicalProfile(

                    userID = 6,
                    height = 161.0,
                    weight = 86.23,
                    bmi = 33.27,
                    myopiaDegree = null,
                    wearsGlasses = false,
                    bloodType = "A",
                    shoeSize = 39.8
                ),
                PhysicalProfile(

                    userID = 7,
                    height = 194.0,
                    weight = 53.79,
                    bmi = 14.29,
                    myopiaDegree = null,
                    wearsGlasses = false,
                    bloodType = "AB",
                    shoeSize = 38.0
                ),
                PhysicalProfile(

                    userID = 8,
                    height = 166.0,
                    weight = 81.96,
                    bmi = 29.74,
                    myopiaDegree = null,
                    wearsGlasses = false,
                    bloodType = "O",
                    shoeSize = 46.0
                ),
                PhysicalProfile(

                    userID = 9,
                    height = 199.0,
                    weight = 54.9,
                    bmi = 13.86,
                    myopiaDegree = null,
                    wearsGlasses = false,
                    bloodType = "O",
                    shoeSize = 41.7
                ),
                PhysicalProfile(

                    userID = 10,
                    height = 179.0,
                    weight = 73.94,
                    bmi = 23.08,
                    myopiaDegree = -2.72,
                    wearsGlasses = true,
                    bloodType = "O",
                    shoeSize = 39.9
                )
            )
        }

        private fun provideInitialNutritionRecords(): List<NutritionRecord> {
            return listOf(
                NutritionRecord(
                    nutritionID = 1,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 6, 0, 0),
                    mealType = "Snack",
                    calories = 863.91,
                    protein = 24.57,
                    fat = 2.21,
                    carbohydrates = 65.69
                ),
                NutritionRecord(
                    nutritionID = 2,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 5, 0, 0),
                    mealType = "Snack",
                    calories = 569.29,
                    protein = 27.37,
                    fat = 16.78,
                    carbohydrates = 72.27
                ),
                NutritionRecord(
                    nutritionID = 3,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 4, 0, 0),
                    mealType = "Breakfast",
                    calories = 830.55,
                    protein = 27.46,
                    fat = 14.57,
                    carbohydrates = 78.66
                ),
                NutritionRecord(
                    nutritionID = 4,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 3, 0, 0),
                    mealType = "Breakfast",
                    calories = 505.25,
                    protein = 24.96,
                    fat = 8.96,
                    carbohydrates = 75.64
                ),
                NutritionRecord(
                    nutritionID = 5,
                    userID = 4,
                    date = LocalDateTime.of(2024, 4, 2, 0, 0),
                    mealType = "Lunch",
                    calories = 421.04,
                    protein = 14.49,
                    fat = 9.33,
                    carbohydrates = 87.33
                ),
                NutritionRecord(
                    nutritionID = 6,
                    userID = 8,
                    date = LocalDateTime.of(2024, 4, 1, 0, 0),
                    mealType = "Breakfast",
                    calories = 447.89,
                    protein = 29.76,
                    fat = 0.92,
                    carbohydrates = 87.74
                ),
                NutritionRecord(
                    nutritionID = 7,
                    userID = 9,
                    date = LocalDateTime.of(2024, 3, 31, 0, 0),
                    mealType = "Snack",
                    calories = 299.28,
                    protein = 18.26,
                    fat = 1.03,
                    carbohydrates = 50.82
                ),
                NutritionRecord(
                    nutritionID = 8,
                    userID = 4,
                    date = LocalDateTime.of(2024, 3, 30, 0, 0),
                    mealType = "Breakfast",
                    calories = 661.82,
                    protein = 17.2,
                    fat = 16.8,
                    carbohydrates = 74.69
                ),
                NutritionRecord(
                    nutritionID = 9,
                    userID = 2,
                    date = LocalDateTime.of(2024, 3, 29, 0, 0),
                    mealType = "Dinner",
                    calories = 545.58,
                    protein = 22.89,
                    fat = 7.37,
                    carbohydrates = 98.63
                ),
                NutritionRecord(
                    nutritionID = 10,
                    userID = 4,
                    date = LocalDateTime.of(2024, 3, 28, 0, 0),
                    mealType = "Lunch",
                    calories = 562.81,
                    protein = 10.74,
                    fat = 1.3,
                    carbohydrates = 51.9
                )

            )
        }

        private fun provideInitialMedicationRecords(): List<MedicationRecord> {
            return listOf(
                MedicationRecord(
                    medicationID = 1,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 6, 0, 0),
                    drugName = "Ibuprofen",
                    dosage = "200mg",
                    frequency = "Three Times Daily",
                    purpose = "Antibiotic"
                ),
                MedicationRecord(
                    medicationID = 2,
                    userID = 4,
                    date = LocalDateTime.of(2024, 4, 5, 0, 0),
                    drugName = "Acetaminophen",
                    dosage = "500mg",
                    frequency = "Three Times Daily",
                    purpose = "Cholesterol Management"
                ),
                MedicationRecord(
                    medicationID = 3,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 4, 0, 0),
                    drugName = "Ciprofloxacin",
                    dosage = "500mg",
                    frequency = "Once Daily",
                    purpose = "Antibiotic"
                ),
                MedicationRecord(
                    medicationID = 4,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 3, 0, 0),
                    drugName = "Amoxicillin",
                    dosage = "250mg",
                    frequency = "Once Daily",
                    purpose = null
                ),
                MedicationRecord(
                    medicationID = 5,
                    userID = 7,
                    date = LocalDateTime.of(2024, 4, 2, 0, 0),
                    drugName = "Ciprofloxacin",
                    dosage = "200mg",
                    frequency = "Once Daily",
                    purpose = null
                ),
                MedicationRecord(
                    medicationID = 6,
                    userID = 2,
                    date = LocalDateTime.of(2024, 4, 1, 0, 0),
                    drugName = "Acetaminophen",
                    dosage = "500mg",
                    frequency = "Twice Daily",
                    purpose = "Fever Reduction"
                ),
                MedicationRecord(
                    medicationID = 7,
                    userID = 9,
                    date = LocalDateTime.of(2024, 3, 31, 0, 0),
                    drugName = "Amoxicillin",
                    dosage = "200mg",
                    frequency = "Once Daily",
                    purpose = "Cholesterol Management"
                ),
                MedicationRecord(
                    medicationID = 8,
                    userID = 5,
                    date = LocalDateTime.of(2024, 3, 30, 0, 0),
                    drugName = "Ciprofloxacin",
                    dosage = "200mg",
                    frequency = "Twice Daily",
                    purpose = "Antibiotic"
                ),
                MedicationRecord(
                    medicationID = 9,
                    userID = 2,
                    date = LocalDateTime.of(2024, 3, 29, 0, 0),
                    drugName = "Ciprofloxacin",
                    dosage = "200mg",
                    frequency = "Three Times Daily",
                    purpose = "Cholesterol Management"
                ),
                MedicationRecord(
                    medicationID = 10,
                    userID = 4,
                    date = LocalDateTime.of(2024, 3, 28, 0, 0),
                    drugName = "Amoxicillin",
                    dosage = "250mg",
                    frequency = "Three Times Daily",
                    purpose = "Cholesterol Management"
                )

            )
        }

        private fun provideInitialHealthIndicators(): List<HealthIndicator> {
            return listOf(
                HealthIndicator(
                    indicatorID = 1,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 6, 0, 0),
                    bloodPressure = "115/89",
                    cholesterol = 199.15,
                    glucoseLevel = 127.43,
                    otherIndicators = "Oxygen Saturation: 95-100%"
                ),
                HealthIndicator(
                    indicatorID = 2,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 5, 0, 0),
                    bloodPressure = "136/85",
                    cholesterol = 112.57,
                    glucoseLevel = 80.27,
                    otherIndicators = null
                ),
                HealthIndicator(
                    indicatorID = 3,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 4, 0, 0),
                    bloodPressure = "113/88",
                    cholesterol = 199.08,
                    glucoseLevel = 81.88,
                    otherIndicators = null
                ),
                HealthIndicator(
                    indicatorID = 4,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 3, 0, 0),
                    bloodPressure = "111/81",
                    cholesterol = 210.34,
                    glucoseLevel = 118.16,
                    otherIndicators = null
                ),
                HealthIndicator(
                    indicatorID = 5,
                    userID = 4,
                    date = LocalDateTime.of(2024, 4, 2, 0, 0),
                    bloodPressure = "130/82",
                    cholesterol = 233.52,
                    glucoseLevel = 121.09,
                    otherIndicators = "Oxygen Saturation: 95-100%"
                ),
                HealthIndicator(
                    indicatorID = 6,
                    userID = 3,
                    date = LocalDateTime.of(2024, 4, 1, 0, 0),
                    bloodPressure = "134/73",
                    cholesterol = 218.46,
                    glucoseLevel = 113.87,
                    otherIndicators = "Heart Rate: 60-100 bpm"
                ),
                HealthIndicator(
                    indicatorID = 7,
                    userID = 10,
                    date = LocalDateTime.of(2024, 3, 31, 0, 0),
                    bloodPressure = "132/86",
                    cholesterol = 157.9,
                    glucoseLevel = 124.39,
                    otherIndicators = null
                ),
                HealthIndicator(
                    indicatorID = 8,
                    userID = 6,
                    date = LocalDateTime.of(2024, 3, 30, 0, 0),
                    bloodPressure = "116/77",
                    cholesterol = 188.1,
                    glucoseLevel = 84.09,
                    otherIndicators = "Heart Rate: 60-100 bpm"
                ),
                HealthIndicator(
                    indicatorID = 9,
                    userID = 10,
                    date = LocalDateTime.of(2024, 3, 29, 0, 0),
                    bloodPressure = "120/87",
                    cholesterol = 152.29,
                    glucoseLevel = 97.35,
                    otherIndicators = null
                ),
                HealthIndicator(
                    indicatorID = 10,
                    userID = 6,
                    date = LocalDateTime.of(2024, 3, 28, 0, 0),
                    bloodPressure = "123/85",
                    cholesterol = 145.03,
                    glucoseLevel = 102.79,
                    otherIndicators = null
                )

            )
        }

        private fun provideInitialExerciseRecords(): List<ExerciseRecord> {
            return listOf(
                ExerciseRecord(
                    exerciseID = 1,
                    userID = 6,
                    exerciseType = "Cycling",
                    duration = "90 minutes",
                    date = LocalDateTime.of(2024, 4, 6, 0, 0),
                    distance = 3.77,
                    energyExpenditure = 377.0
                ),
                ExerciseRecord(
                    exerciseID = 2,
                    userID = 7,
                    exerciseType = "Swimming",
                    duration = "45 minutes",
                    date = LocalDateTime.of(2024, 4, 5, 0, 0),
                    distance = 5.20,
                    energyExpenditure = 100.0
                ),
                ExerciseRecord(
                    exerciseID = 3,
                    userID = 1,
                    exerciseType = "Yoga",
                    duration = "90 minutes",
                    date = LocalDateTime.of(2024, 4, 4, 0, 0),
                    distance = 2.56,
                    energyExpenditure = 200.0
                ),
                ExerciseRecord(
                    exerciseID = 4,
                    userID = 7,
                    exerciseType = "Weight Training",
                    duration = "60 minutes",
                    date = LocalDateTime.of(2024, 4, 3, 0, 0),
                    distance = 1.145,
                    energyExpenditure = 150.0
                ),
                ExerciseRecord(
                    exerciseID = 5,
                    userID = 8,
                    exerciseType = "Weight Training",
                    duration = "90 minutes",
                    date = LocalDateTime.of(2024, 4, 2, 0, 0),
                    distance = 2.50,
                    energyExpenditure = 200.0
                ),
                ExerciseRecord(
                    exerciseID = 6,
                    userID = 7,
                    exerciseType = "Yoga",
                    duration = "45 minutes",
                    date = LocalDateTime.of(2024, 4, 1, 0, 0),
                    distance = 3.80,
                    energyExpenditure = 100.0
                ),
                ExerciseRecord(
                    exerciseID = 7,
                    userID = 3,
                    exerciseType = "Yoga",
                    duration = "45 minutes",
                    date = LocalDateTime.of(2024, 3, 31, 0, 0),
                    distance = 3.14,
                    energyExpenditure = 100.0
                ),
                ExerciseRecord(
                    exerciseID = 8,
                    userID = 9,
                    exerciseType = "Running",
                    duration = "90 minutes",
                    date = LocalDateTime.of(2024, 3, 30, 0, 0),
                    distance = 7.54,
                    energyExpenditure = 754.0
                ),
                ExerciseRecord(
                    exerciseID = 9,
                    userID = 2,
                    exerciseType = "Yoga",
                    duration = "30 minutes",
                    date = LocalDateTime.of(2024, 3, 29, 0, 0),
                    distance = 4.25,
                    energyExpenditure = 50.0
                ),
                ExerciseRecord(
                    exerciseID = 10,
                    userID = 6,
                    exerciseType = "Cycling",
                    duration = "45 minutes",
                    date = LocalDateTime.of(2024, 3, 28, 0, 0),
                    distance = 7.5,
                    energyExpenditure = 750.0
                )

            )
        }

        private fun provideInitialAuthors(): List<Author> {
            return listOf(
                Author(
                    authorID = 1,
                    name = "Author Name 1",
                    bio = "Bio of Author 1. An interesting fact.",
                    profilePicture = "/images/authors/author1"
                ),
                Author(
                    authorID = 2,
                    name = "Author Name 2",
                    bio = "Bio of Author 2. An interesting fact.",
                    profilePicture = "/images/authors/author2"
                ),
                Author(
                    authorID = 3,
                    name = "Author Name 3",
                    bio = "Bio of Author 3. An interesting fact.",
                    profilePicture = "/images/authors/author3"
                ),
                Author(
                    authorID = 4,
                    name = "Author Name 4",
                    bio = "Bio of Author 4. An interesting fact.",
                    profilePicture = "/images/authors/author4"
                ),
                Author(
                    authorID = 5,
                    name = "Author Name 5",
                    bio = "Bio of Author 5. An interesting fact.",
                    profilePicture = "/images/authors/author5"
                ),
                Author(
                    authorID = 6,
                    name = "Author Name 6",
                    bio = "Bio of Author 6. An interesting fact.",
                    profilePicture = "/images/authors/author6"
                ),
                Author(
                    authorID = 7,
                    name = "Author Name 7",
                    bio = "Bio of Author 7. An interesting fact.",
                    profilePicture = "/images/authors/author7"
                ),
                Author(
                    authorID = 8,
                    name = "Author Name 8",
                    bio = "Bio of Author 8. An interesting fact.",
                    profilePicture = "/images/authors/author8"
                ),
                Author(
                    authorID = 9,
                    name = "Author Name 9",
                    bio = "Bio of Author 9. An interesting fact.",
                    profilePicture = "/images/authors/author9"
                ),
                Author(
                    authorID = 10,
                    name = "Author Name 10",
                    bio = "Bio of Author 10. An interesting fact.",
                    profilePicture = "/images/authors/author10"
                )
            )
        }

        private fun provideInitialArticleTags(): List<ArticleTag> {
            return listOf(
                ArticleTag(tagID = 1, tagName = "健康基础知识"),
                ArticleTag(tagID = 2, tagName = "饮食与营养"),
                ArticleTag(tagID = 3, tagName = "运动与健身"),
                ArticleTag(tagID = 4, tagName = "心理健康"),
                ArticleTag(tagID = 5, tagName = "疾病预防与管理"),
            )
        }

        private fun provideInitialArticles(): List<Article> {
            return listOf(
                Article(
                    articleID = 1,
                    title = "早睡的好处",
                    category = "健康基础知识",
                    content = "1.有益心脏：晚上熬夜其实就是在折磨你的身体，会让你的血压和胆固醇升高，这些都会给你心脏带来健康风险。如果你不想情况更糟，就抓紧去睡个好觉。\n" +
                            "\n" +
                            "2.减缓压力：夜晚一个安稳的睡眠，会帮助你释放出日常生活中所产生的身心上的压力，使身心得到放松。不充足的睡眠会导致心脏病发作或其它疾病。\n" +
                            "\n" +
                            "3.提高记忆力：早睡帮助你很好地理清思维，早起之后你的思路会变得更加清晰。\n" +
                            "\n" +
                            "4.免受疾病困扰：晚上熬夜只会让你的血压和胆固醇含量升高，只会给自己的身体带来压力。这样的生活节奏会让你感染癌症或者其它心脏类疾病。所以要早睡让你的血压保持在正常水平。\n" +
                            "\n" +
                            "5.有时间吃早餐：早餐对一天的开始非常重要，而大多时候我们因为起晚了而不吃早餐。为了能吃上早餐所以早晨要早起（早睡才能早起），10点之前吃完早餐非常重要。\n" +
                            "\n" +
                            "6.有乐观的行动：晚上连续睡眠7个小时非常有必要。不充足的睡眠会导致你创造性思维能力下降，让你不得不与压力和注意力作斗争，所有这些压力的因素都会对你的行为产生不良影响。\n" +
                            "\n" +
                            "7.有助新陈代谢：我们的身体需要排出一些我们不需要的毒素，睡得太晚会影响排毒的这个过程。我们需要按时吃饭和睡觉才能保持体力。\n" +
                            "\n" +
                            "8.减少得癌症的几率：要保证在黑暗中睡觉。晚上睡觉有光的话会减少褪黑激素的含量，而抗黑变激素能够促进睡眠，让我们不容易得癌症。所以晚上睡觉要保证房间黑暗并且早睡，这样能促进褪黑激素的产生。\n" +
                            "\n" +
                            "9.养精蓄锐：早睡能让你睡眠更充足，早上更加有精神。能让你精力充足表现更好。灵活的头脑也会助于集中精力。\n" +
                            "\n" +
                            "10.保持体型：睡太晚会导致你的机体收到干扰。一天睡眠不足7小时会导致肥胖。睡眠不足产生的激素会影响食欲，使体重大幅减少。",
                    wordCount = 372,
                    authorID = 1,
                    publishDate = LocalDateTime.of(2024, 4, 5, 0, 0),
                    lastUpdated = LocalDateTime.of(2024, 4, 5, 0, 0)
                ),
                Article(
                    articleID = 2,
                    title = "合理安排三餐",
                    category = "健康基础知识",
                    content = "吃的目的就是把维持人体正常生理功能的食物摄入体内，以满足人体生长发育、益智健脑、抗衰防病的需要。要做到这一点，最重要的是安排好一日三餐。\n" +
                            "\n" +
                            "1.早餐要吃好：清晨是一天的开端，人们经过一夜的休息后体内食物已被消化吸收完毕，而上午有事思维最活跃、体能消耗最多的时候，整个身体迫切需要得到补充。因此早餐应吃好，多吃些高热量、高蛋白的食物，进食量为全天量的30%-40%为宜。\n" +
                            "\n" +
                            "2.午餐要吃饱：午餐是一日中最主要的一餐，经过半天的“激战”后，体能消耗大，下午还要继续学习和工作，身体需要大量的能量供给，故午餐应吃饱，进食量为全天量的40%-50%。\n" +
                            "\n" +
                            "3.晚餐要吃少：晚餐后人们的活动量大为减少，若吃的太多会给身体带来许多的危害，因此晚餐要少吃，进食量为全天量的20%-30%为宜。",
                    wordCount = 248,
                    authorID = 2,
                    publishDate = LocalDateTime.of(2024, 4, 5, 0, 0),
                    lastUpdated = LocalDateTime.of(2024, 4, 5, 0, 0)
                ),
                Article(
                    articleID = 3,
                    title = "Article Title 3",
                    category = "饮食与营养",
                    content = "Content of Article 3. Covering the latest.",
                    wordCount = 750,
                    authorID = 3,
                    publishDate = LocalDateTime.of(2024, 4, 5, 0, 0),
                    lastUpdated = LocalDateTime.of(2024, 4, 5, 0, 0)
                ),
                Article(
                    articleID = 4,
                    title = "Article Title 4",
                    category = "饮食与营养",
                    content = "Content of Article 4. In-depth reporting.",
                    wordCount = 1250,
                    authorID = 4,
                    publishDate = LocalDateTime.of(2024, 4, 5, 0, 0),
                    lastUpdated = LocalDateTime.of(2024, 4, 5, 0, 0)
                ),
                Article(
                    articleID = 5,
                    title = "Article Title 5",
                    category = "运动与健身",
                    content = "Content of Article 5. Reviewing the new thing.",
                    wordCount = 600,
                    authorID = 5,
                    publishDate = LocalDateTime.of(2024, 4, 5, 0, 0),
                    lastUpdated = LocalDateTime.of(2024, 4, 5, 0, 0)
                ),
                Article(
                    articleID = 6,
                    title = "Article Title 6",
                    category = "运动与健身",
                    content = "Content of Article 6. How-to and tutorials.",
                    wordCount = 950,
                    authorID = 6,
                    publishDate = LocalDateTime.of(2024, 4, 5, 0, 0),
                    lastUpdated = LocalDateTime.of(2024, 4, 5, 0, 0)
                ),
                Article(
                    articleID = 7,
                    title = "Article Title 7",
                    category = "心理健康",
                    content = "Content of Article 7. Expressing views.",
                    wordCount = 800,
                    authorID = 7,
                    publishDate = LocalDateTime.of(2024, 4, 5, 0, 0),
                    lastUpdated = LocalDateTime.of(2024, 4, 5, 0, 0)
                ),
                Article(
                    articleID = 8,
                    title = "Article Title 8",
                    category = "心理健康",
                    content = "Content of Article 8. Latest updates.",
                    wordCount = 680,
                    authorID = 8,
                    publishDate = LocalDateTime.of(2024, 4, 5, 0, 0),
                    lastUpdated = LocalDateTime.of(2024, 4, 5, 0, 0)
                ),
                Article(
                    articleID = 9,
                    title = "Article Title 9",
                    category = "疾病预防与管理",
                    content = "Content of Article 9. Investigative piece.",
                    wordCount = 1100,
                    authorID = 9,
                    publishDate = LocalDateTime.of(2024, 4, 5, 0, 0),
                    lastUpdated = LocalDateTime.of(2024, 4, 5, 0, 0)
                ),
                Article(
                    articleID = 10,
                    title = "Article Title 10",
                    category = "疾病预防与管理",
                    content = "Content of Article 10. Critical look at events.",
                    wordCount = 500,
                    authorID = 10,
                    publishDate = LocalDateTime.of(2024, 4, 5, 0, 0),
                    lastUpdated = LocalDateTime.of(2024, 4, 5, 0, 0)
                )
            )
        }

        private fun provideInitialArticleMedia(): List<ArticleMedia> {
            return listOf(
                ArticleMedia(
                    mediaID = 1,
                    articleID = 1,
                    mediaType = "Image",
                    filePath = "ab1_inversions",
                    description = "Media description for Article 1"
                ),
                ArticleMedia(
                    mediaID = 2,
                    articleID = 2,
                    mediaType = "Image",
                    filePath = "ab2_quick_yoga",
                    description = "Media description for Article 2"
                ),
                ArticleMedia(
                    mediaID = 3,
                    articleID = 3,
                    mediaType = "Image",
                    filePath = "ab3_stretching",
                    description = "Media description for Article 3"
                ),
                ArticleMedia(
                    mediaID = 4,
                    articleID = 4,
                    mediaType = "Image",
                    filePath = "ab4_tabata",
                    description = "Media description for Article 4"
                ),
                ArticleMedia(
                    mediaID = 5,
                    articleID = 5,
                    mediaType = "Image",
                    filePath = "ab5_hiit",
                    description = "Media description for Article 5"
                ),
                ArticleMedia(
                    mediaID = 6,
                    articleID = 6,
                    mediaType = "Image",
                    filePath = "ab6_pre_natal_yoga",
                    description = "Media description for Article 6"
                ),
                ArticleMedia(
                    mediaID = 7,
                    articleID = 7,
                    mediaType = "Image",
                    filePath = "fc1_short_mantras",
                    description = "Media description for Article 7"
                ),
                ArticleMedia(
                    mediaID = 8,
                    articleID = 8,
                    mediaType = "Image",
                    filePath = "fc3_stress_and_anxiety",
                    description = "Media description for Article 8"
                ),
                ArticleMedia(
                    mediaID = 9,
                    articleID = 9,
                    mediaType = "Image",
                    filePath = "fc4_self_massage",
                    description = "Media description for Article 9"
                ),
                ArticleMedia(
                    mediaID = 10,
                    articleID = 10,
                    mediaType = "Image",
                    filePath = "fc5_overwhelmed",
                    description = "Media description for Article 10"
                )
            )
        }

        private fun provideInitialArticleTagRelations(): List<ArticleTagRelation> {
            return listOf(
                ArticleTagRelation(articleID = 1, tagID = 1, true),
                ArticleTagRelation(articleID = 2, tagID = 1, true),
                ArticleTagRelation(articleID = 3, tagID = 2, true),
                ArticleTagRelation(articleID = 4, tagID = 2, true),
                ArticleTagRelation(articleID = 5, tagID = 3, true),
                ArticleTagRelation(articleID = 6, tagID = 3, true),
                ArticleTagRelation(articleID = 7, tagID = 4, true),
                ArticleTagRelation(articleID = 8, tagID = 4, true),
                ArticleTagRelation(articleID = 9, tagID = 5, true),
                ArticleTagRelation(articleID = 10, tagID = 5, true)
            )
        }

        private fun provideInitialDailyActivities(): List<DailyActivity> {
            return listOf(
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 7, 0, 0),
                    steps = 15257,
                    walkingDistance = 12.21,
                    exerciseDuration = "01:06:14",
                    heartRate = 90,
                    floorsClimbed = 8,
                    runningDistance = 3.34,
                    spO2 = 98.66,
                    energyExpenditure = 543.57,
                    vitalCapacity = 4.29
                ),
                DailyActivity(
                    userID = 2,
                    date = LocalDateTime.of(2024, 4, 6, 0, 0),
                    steps = 19169,
                    walkingDistance = 15.34,
                    exerciseDuration = "01:24:44",
                    heartRate = 71,
                    floorsClimbed = 5,
                    runningDistance = 3.32,
                    spO2 = 92.45,
                    energyExpenditure = 390.65,
                    vitalCapacity = 2.03
                ),
                DailyActivity(
                    userID = 3,
                    date = LocalDateTime.of(2024, 4, 5, 0, 0),
                    steps = 7842,
                    walkingDistance = 6.27,
                    exerciseDuration = "01:33:52",
                    heartRate = 83,
                    floorsClimbed = 25,
                    runningDistance = 9.11,
                    spO2 = 93.49,
                    energyExpenditure = 600.94,
                    vitalCapacity = 4.86
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 4, 0, 0),
                    steps = 6706,
                    walkingDistance = 5.36,
                    exerciseDuration = "00:10:34",
                    heartRate = 93,
                    floorsClimbed = 22,
                    runningDistance = 1.18,
                    spO2 = 95.59,
                    energyExpenditure = 516.21,
                    vitalCapacity = 4.57
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 3, 0, 0),
                    steps = 7672,
                    walkingDistance = 6.14,
                    exerciseDuration = "00:28:03",
                    heartRate = 68,
                    floorsClimbed = 32,
                    runningDistance = 9.5,
                    spO2 = 96.37,
                    energyExpenditure = 232.37,
                    vitalCapacity = 2.79
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 2, 0, 0),
                    steps = 12559,
                    walkingDistance = 10.05,
                    exerciseDuration = "01:45:47",
                    heartRate = 71,
                    floorsClimbed = 28,
                    runningDistance = 7.81,
                    spO2 = 92.48,
                    energyExpenditure = 364.67,
                    vitalCapacity = 2.71
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 1, 0, 0),
                    steps = 11501,
                    walkingDistance = 9.2,
                    exerciseDuration = "00:30:25",
                    heartRate = 72,
                    floorsClimbed = 22,
                    runningDistance = 3.75,
                    spO2 = 93.7,
                    energyExpenditure = 462.45,
                    vitalCapacity = 2.36
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 31, 0, 0),
                    steps = 5423,
                    walkingDistance = 4.34,
                    exerciseDuration = "00:46:01",
                    heartRate = 96,
                    floorsClimbed = 15,
                    runningDistance = 4.6,
                    spO2 = 96.87,
                    energyExpenditure = 267.79,
                    vitalCapacity = 4.92
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 30, 0, 0),
                    steps = 16813,
                    walkingDistance = 13.45,
                    exerciseDuration = "00:13:53",
                    heartRate = 89,
                    floorsClimbed = 27,
                    runningDistance = 1.66,
                    spO2 = 99.26,
                    energyExpenditure = 843.14,
                    vitalCapacity = 3.57
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 29, 0, 0),
                    steps = 3021,
                    walkingDistance = 2.42,
                    exerciseDuration = "00:41:16",
                    heartRate = 92,
                    floorsClimbed = 37,
                    runningDistance = 7.69,
                    spO2 = 95.32,
                    energyExpenditure = 277.3,
                    vitalCapacity = 4.97
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 28, 0, 0),
                    steps = 8223,
                    walkingDistance = 6.58,
                    exerciseDuration = "01:42:04",
                    heartRate = 94,
                    floorsClimbed = 2,
                    runningDistance = 1.05,
                    spO2 = 98.15,
                    energyExpenditure = 390.7,
                    vitalCapacity = 4.83
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 27, 0, 0),
                    steps = 17095,
                    walkingDistance = 13.68,
                    exerciseDuration = "01:10:17",
                    heartRate = 69,
                    floorsClimbed = 3,
                    runningDistance = 3.92,
                    spO2 = 98.6,
                    energyExpenditure = 978.99,
                    vitalCapacity = 2.09
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 26, 0, 0),
                    steps = 15605,
                    walkingDistance = 12.48,
                    exerciseDuration = "01:09:54",
                    heartRate = 72,
                    floorsClimbed = 27,
                    runningDistance = 3.25,
                    spO2 = 95.53,
                    energyExpenditure = 819.82,
                    vitalCapacity = 3.92
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 25, 0, 0),
                    steps = 2162,
                    walkingDistance = 1.73,
                    exerciseDuration = "00:57:28",
                    heartRate = 64,
                    floorsClimbed = 32,
                    runningDistance = 9.83,
                    spO2 = 98.93,
                    energyExpenditure = 318.54,
                    vitalCapacity = 3.78
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 24, 0, 0),
                    steps = 3628,
                    walkingDistance = 2.9,
                    exerciseDuration = "00:01:17",
                    heartRate = 99,
                    floorsClimbed = 25,
                    runningDistance = 7.23,
                    spO2 = 92.37,
                    energyExpenditure = 857.28,
                    vitalCapacity = 3.37
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 23, 0, 0),
                    steps = 10716,
                    walkingDistance = 8.57,
                    exerciseDuration = "01:42:44",
                    heartRate = 89,
                    floorsClimbed = 24,
                    runningDistance = 3.67,
                    spO2 = 98.06,
                    energyExpenditure = 818.38,
                    vitalCapacity = 2.52
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 22, 0, 0),
                    steps = 11291,
                    walkingDistance = 9.03,
                    exerciseDuration = "01:21:24",
                    heartRate = 87,
                    floorsClimbed = 8,
                    runningDistance = 7.56,
                    spO2 = 99.45,
                    energyExpenditure = 926.84,
                    vitalCapacity = 4.1
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 21, 0, 0),
                    steps = 4397,
                    walkingDistance = 3.52,
                    exerciseDuration = "01:11:24",
                    heartRate = 87,
                    floorsClimbed = 10,
                    runningDistance = 4.67,
                    spO2 = 93.01,
                    energyExpenditure = 688.62,
                    vitalCapacity = 3.76
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 20, 0, 0),
                    steps = 11078,
                    walkingDistance = 8.86,
                    exerciseDuration = "01:44:24",
                    heartRate = 94,
                    floorsClimbed = 6,
                    runningDistance = 9.43,
                    spO2 = 95.69,
                    energyExpenditure = 877.33,
                    vitalCapacity = 4.06
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 19, 0, 0),
                    steps = 17948,
                    walkingDistance = 14.36,
                    exerciseDuration = "00:24:13",
                    heartRate = 85,
                    floorsClimbed = 26,
                    runningDistance = 5.15,
                    spO2 = 92.4,
                    energyExpenditure = 461.97,
                    vitalCapacity = 3.73
                ), DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 18, 0, 0),
                    steps = 8965,
                    walkingDistance = 7.16,
                    exerciseDuration = "01:25:39",
                    heartRate = 78,
                    floorsClimbed = 12,
                    runningDistance = 2.44,
                    spO2 = 97.22,
                    energyExpenditure = 529.41,
                    vitalCapacity = 3.25
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 17, 0, 0),
                    steps = 13457,
                    walkingDistance = 10.75,
                    exerciseDuration = "00:56:18",
                    heartRate = 88,
                    floorsClimbed = 9,
                    runningDistance = 5.87,
                    spO2 = 98.99,
                    energyExpenditure = 682.77,
                    vitalCapacity = 4.14
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 16, 0, 0),
                    steps = 10532,
                    walkingDistance = 8.42,
                    exerciseDuration = "01:07:53",
                    heartRate = 95,
                    floorsClimbed = 7,
                    runningDistance = 6.19,
                    spO2 = 97.56,
                    energyExpenditure = 750.12,
                    vitalCapacity = 3.89
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 15, 0, 0),
                    steps = 6985,
                    walkingDistance = 5.59,
                    exerciseDuration = "00:38:21",
                    heartRate = 74,
                    floorsClimbed = 10,
                    runningDistance = 3.28,
                    spO2 = 94.83,
                    energyExpenditure = 415.66,
                    vitalCapacity = 2.47
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 14, 0, 0),
                    steps = 14729,
                    walkingDistance = 11.78,
                    exerciseDuration = "00:49:57",
                    heartRate = 91,
                    floorsClimbed = 18,
                    runningDistance = 2.54,
                    spO2 = 96.14,
                    energyExpenditure = 813.55,
                    vitalCapacity = 4.12
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 13, 0, 0),
                    steps = 16382,
                    walkingDistance = 13.1,
                    exerciseDuration = "01:53:42",
                    heartRate = 83,
                    floorsClimbed = 14,
                    runningDistance = 7.98,
                    spO2 = 95.37,
                    energyExpenditure = 939.48,
                    vitalCapacity = 4.78
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 12, 0, 0),
                    steps = 12234,
                    walkingDistance = 9.79,
                    exerciseDuration = "01:35:44",
                    heartRate = 80,
                    floorsClimbed = 5,
                    runningDistance = 4.32,
                    spO2 = 93.82,
                    energyExpenditure = 512.29,
                    vitalCapacity = 3.06
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 11, 0, 0),
                    steps = 8579,
                    walkingDistance = 6.86,
                    exerciseDuration = "00:41:29",
                    heartRate = 85,
                    floorsClimbed = 11,
                    runningDistance = 1.73,
                    spO2 = 94.95,
                    energyExpenditure = 481.34,
                    vitalCapacity = 3.29
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 10, 0, 0),
                    steps = 6420,
                    walkingDistance = 5.13,
                    exerciseDuration = "00:59:10",
                    heartRate = 90,
                    floorsClimbed = 16,
                    runningDistance = 6.55,
                    spO2 = 96.61,
                    energyExpenditure = 289.42,
                    vitalCapacity = 2.95
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 9, 0, 0),
                    steps = 11956,
                    walkingDistance = 9.56,
                    exerciseDuration = "01:15:22",
                    heartRate = 77,
                    floorsClimbed = 4,
                    runningDistance = 3.47,
                    spO2 = 97.44,
                    energyExpenditure = 613.23,
                    vitalCapacity = 3.81
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 8, 0, 0),
                    steps = 13742,
                    walkingDistance = 10.99,
                    exerciseDuration = "01:02:37",
                    heartRate = 92,
                    floorsClimbed = 17,
                    runningDistance = 2.89,
                    spO2 = 98.71,
                    energyExpenditure = 714.19,
                    vitalCapacity = 4.44
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 7, 0, 0),
                    steps = 9587,
                    walkingDistance = 7.67,
                    exerciseDuration = "00:53:48",
                    heartRate = 86,
                    floorsClimbed = 13,
                    runningDistance = 4.36,
                    spO2 = 95.28,
                    energyExpenditure = 589.12,
                    vitalCapacity = 3.57
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 6, 0, 0),
                    steps = 11345,
                    walkingDistance = 9.08,
                    exerciseDuration = "01:46:13",
                    heartRate = 79,
                    floorsClimbed = 6,
                    runningDistance = 7.12,
                    spO2 = 93.96,
                    energyExpenditure = 657.80,
                    vitalCapacity = 3.98
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 5, 0, 0),
                    steps = 12478,
                    walkingDistance = 9.98,
                    exerciseDuration = "00:38:56",
                    heartRate = 84,
                    floorsClimbed = 19,
                    runningDistance = 5.63,
                    spO2 = 97.34,
                    energyExpenditure = 722.49,
                    vitalCapacity = 4.29
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 4, 0, 0),
                    steps = 14652,
                    walkingDistance = 11.72,
                    exerciseDuration = "01:12:31",
                    heartRate = 96,
                    floorsClimbed = 8,
                    runningDistance = 3.96,
                    spO2 = 98.88,
                    energyExpenditure = 841.57,
                    vitalCapacity = 3.95
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 3, 0, 0),
                    steps = 13094,
                    walkingDistance = 10.48,
                    exerciseDuration = "00:50:25",
                    heartRate = 73,
                    floorsClimbed = 21,
                    runningDistance = 4.19,
                    spO2 = 99.02,
                    energyExpenditure = 659.32,
                    vitalCapacity = 4.01
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 2, 0, 0),
                    steps = 11267,
                    walkingDistance = 9.01,
                    exerciseDuration = "01:33:18",
                    heartRate = 81,
                    floorsClimbed = 23,
                    runningDistance = 8.44,
                    spO2 = 94.77,
                    energyExpenditure = 703.15,
                    vitalCapacity = 4.67
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 1, 0, 0),
                    steps = 9763,
                    walkingDistance = 7.81,
                    exerciseDuration = "01:08:22",
                    heartRate = 88,
                    floorsClimbed = 15,
                    runningDistance = 2.71,
                    spO2 = 95.84,
                    energyExpenditure = 528.37,
                    vitalCapacity = 3.12
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 28, 0, 0),
                    steps = 6548,
                    walkingDistance = 5.24,
                    exerciseDuration = "00:47:39",
                    heartRate = 65,
                    floorsClimbed = 7,
                    runningDistance = 1.58,
                    spO2 = 97.65,
                    energyExpenditure = 310.42,
                    vitalCapacity = 2.84
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 27, 0, 0),
                    steps = 8437,
                    walkingDistance = 6.75,
                    exerciseDuration = "01:00:17",
                    heartRate = 93,
                    floorsClimbed = 12,
                    runningDistance = 5.01,
                    spO2 = 96.53,
                    energyExpenditure = 467.51,
                    vitalCapacity = 3.68
                ), DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 26, 0, 0),
                    steps = 9376,
                    walkingDistance = 7.5,
                    exerciseDuration = "01:17:33",
                    heartRate = 90,
                    floorsClimbed = 10,
                    runningDistance = 2.96,
                    spO2 = 97.88,
                    energyExpenditure = 495.20,
                    vitalCapacity = 3.41
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 25, 0, 0),
                    steps = 14352,
                    walkingDistance = 11.48,
                    exerciseDuration = "01:02:07",
                    heartRate = 87,
                    floorsClimbed = 13,
                    runningDistance = 4.39,
                    spO2 = 96.42,
                    energyExpenditure = 824.66,
                    vitalCapacity = 4.02
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 24, 0, 0),
                    steps = 7884,
                    walkingDistance = 6.31,
                    exerciseDuration = "00:51:22",
                    heartRate = 82,
                    floorsClimbed = 11,
                    runningDistance = 1.89,
                    spO2 = 95.67,
                    energyExpenditure = 357.94,
                    vitalCapacity = 2.76
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 23, 0, 0),
                    steps = 11689,
                    walkingDistance = 9.35,
                    exerciseDuration = "01:28:45",
                    heartRate = 91,
                    floorsClimbed = 17,
                    runningDistance = 3.23,
                    spO2 = 94.52,
                    energyExpenditure = 632.18,
                    vitalCapacity = 3.90
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 22, 0, 0),
                    steps = 10457,
                    walkingDistance = 8.37,
                    exerciseDuration = "01:15:58",
                    heartRate = 78,
                    floorsClimbed = 5,
                    runningDistance = 7.11,
                    spO2 = 97.01,
                    energyExpenditure = 712.47,
                    vitalCapacity = 4.15
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 21, 0, 0),
                    steps = 8543,
                    walkingDistance = 6.83,
                    exerciseDuration = "00:42:31",
                    heartRate = 85,
                    floorsClimbed = 9,
                    runningDistance = 2.45,
                    spO2 = 96.59,
                    energyExpenditure = 468.32,
                    vitalCapacity = 3.12
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 20, 0, 0),
                    steps = 9786,
                    walkingDistance = 7.83,
                    exerciseDuration = "01:33:29",
                    heartRate = 76,
                    floorsClimbed = 12,
                    runningDistance = 3.97,
                    spO2 = 98.72,
                    energyExpenditure = 599.21,
                    vitalCapacity = 3.98
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 19, 0, 0),
                    steps = 14285,
                    walkingDistance = 11.43,
                    exerciseDuration = "01:12:10",
                    heartRate = 89,
                    floorsClimbed = 18,
                    runningDistance = 5.52,
                    spO2 = 95.38,
                    energyExpenditure = 833.19,
                    vitalCapacity = 4.21
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 18, 0, 0),
                    steps = 6732,
                    walkingDistance = 5.38,
                    exerciseDuration = "00:34:27",
                    heartRate = 84,
                    floorsClimbed = 14,
                    runningDistance = 2.13,
                    spO2 = 96.24,
                    energyExpenditure = 324.87,
                    vitalCapacity = 2.68
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 17, 0, 0),
                    steps = 11584,
                    walkingDistance = 9.27,
                    exerciseDuration = "01:41:33",
                    heartRate = 92,
                    floorsClimbed = 6,
                    runningDistance = 4.36,
                    spO2 = 97.49,
                    energyExpenditure = 745.39,
                    vitalCapacity = 3.83
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 16, 0, 0),
                    steps = 8274,
                    walkingDistance = 6.62,
                    exerciseDuration = "00:53:11",
                    heartRate = 77,
                    floorsClimbed = 10,
                    runningDistance = 3.14,
                    spO2 = 94.85,
                    energyExpenditure = 489.30,
                    vitalCapacity = 3.16
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 15, 0, 0),
                    steps = 9563,
                    walkingDistance = 7.65,
                    exerciseDuration = "01:07:42",
                    heartRate = 88,
                    floorsClimbed = 8,
                    runningDistance = 2.76,
                    spO2 = 95.73,
                    energyExpenditure = 577.94,
                    vitalCapacity = 3.45
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 14, 0, 0),
                    steps = 13928,
                    walkingDistance = 11.14,
                    exerciseDuration = "00:58:19",
                    heartRate = 90,
                    floorsClimbed = 17,
                    runningDistance = 6.02,
                    spO2 = 98.46,
                    energyExpenditure = 815.67,
                    vitalCapacity = 4.33
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 13, 0, 0),
                    steps = 6819,
                    walkingDistance = 5.45,
                    exerciseDuration = "01:24:56",
                    heartRate = 81,
                    floorsClimbed = 5,
                    runningDistance = 1.96,
                    spO2 = 93.67,
                    energyExpenditure = 366.82,
                    vitalCapacity = 2.92
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 12, 0, 0),
                    steps = 12248,
                    walkingDistance = 9.8,
                    exerciseDuration = "01:12:03",
                    heartRate = 95,
                    floorsClimbed = 13,
                    runningDistance = 4.81,
                    spO2 = 97.14,
                    energyExpenditure = 793.10,
                    vitalCapacity = 4.06
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 11, 0, 0),
                    steps = 13457,
                    walkingDistance = 10.76,
                    exerciseDuration = "00:46:18",
                    heartRate = 86,
                    floorsClimbed = 19,
                    runningDistance = 3.87,
                    spO2 = 96.99,
                    energyExpenditure = 828.77,
                    vitalCapacity = 4.14
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 10, 0, 0),
                    steps = 10532,
                    walkingDistance = 8.42,
                    exerciseDuration = "01:07:53",
                    heartRate = 95,
                    floorsClimbed = 7,
                    runningDistance = 6.19,
                    spO2 = 97.56,
                    energyExpenditure = 750.12,
                    vitalCapacity = 3.89
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 9, 0, 0),
                    steps = 6985,
                    walkingDistance = 5.59,
                    exerciseDuration = "00:38:21",
                    heartRate = 74,
                    floorsClimbed = 10,
                    runningDistance = 3.28,
                    spO2 = 94.83,
                    energyExpenditure = 415.66,
                    vitalCapacity = 2.47
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 8, 0, 0),
                    steps = 14729,
                    walkingDistance = 11.78,
                    exerciseDuration = "00:49:57",
                    heartRate = 91,
                    floorsClimbed = 18,
                    runningDistance = 2.54,
                    spO2 = 96.14,
                    energyExpenditure = 813.55,
                    vitalCapacity = 4.12
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 7, 0, 0),
                    steps = 16382,
                    walkingDistance = 13.1,
                    exerciseDuration = "01:53:42",
                    heartRate = 83,
                    floorsClimbed = 14,
                    runningDistance = 7.98,
                    spO2 = 95.37,
                    energyExpenditure = 939.48,
                    vitalCapacity = 4.78
                ), DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 6, 0, 0),
                    steps = 11582,
                    walkingDistance = 9.26,
                    exerciseDuration = "01:35:14",
                    heartRate = 80,
                    floorsClimbed = 11,
                    runningDistance = 2.77,
                    spO2 = 97.34,
                    energyExpenditure = 612.39,
                    vitalCapacity = 3.92
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 5, 0, 0),
                    steps = 8427,
                    walkingDistance = 6.74,
                    exerciseDuration = "00:44:59",
                    heartRate = 78,
                    floorsClimbed = 14,
                    runningDistance = 4.43,
                    spO2 = 95.88,
                    energyExpenditure = 489.52,
                    vitalCapacity = 3.14
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 4, 0, 0),
                    steps = 9032,
                    walkingDistance = 7.22,
                    exerciseDuration = "01:18:22",
                    heartRate = 82,
                    floorsClimbed = 12,
                    runningDistance = 1.97,
                    spO2 = 98.44,
                    energyExpenditure = 575.03,
                    vitalCapacity = 3.68
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 3, 0, 0),
                    steps = 7605,
                    walkingDistance = 6.08,
                    exerciseDuration = "00:55:43",
                    heartRate = 85,
                    floorsClimbed = 16,
                    runningDistance = 2.19,
                    spO2 = 94.99,
                    energyExpenditure = 391.27,
                    vitalCapacity = 2.83
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 2, 0, 0),
                    steps = 12456,
                    walkingDistance = 9.96,
                    exerciseDuration = "01:22:17",
                    heartRate = 88,
                    floorsClimbed = 13,
                    runningDistance = 6.04,
                    spO2 = 97.15,
                    energyExpenditure = 832.69,
                    vitalCapacity = 4.06
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 2, 1, 0, 0),
                    steps = 13249,
                    walkingDistance = 10.6,
                    exerciseDuration = "00:59:36",
                    heartRate = 81,
                    floorsClimbed = 17,
                    runningDistance = 5.09,
                    spO2 = 96.21,
                    energyExpenditure = 817.30,
                    vitalCapacity = 4.31
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 31, 0, 0),
                    steps = 9876,
                    walkingDistance = 7.9,
                    exerciseDuration = "01:06:54",
                    heartRate = 89,
                    floorsClimbed = 15,
                    runningDistance = 3.48,
                    spO2 = 95.60,
                    energyExpenditure = 625.87,
                    vitalCapacity = 3.95
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 30, 0, 0),
                    steps = 10234,
                    walkingDistance = 8.19,
                    exerciseDuration = "01:11:29",
                    heartRate = 76,
                    floorsClimbed = 10,
                    runningDistance = 4.67,
                    spO2 = 98.73,
                    energyExpenditure = 664.12,
                    vitalCapacity = 3.88
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 29, 0, 0),
                    steps = 8754,
                    walkingDistance = 7.01,
                    exerciseDuration = "00:49:50",
                    heartRate = 84,
                    floorsClimbed = 9,
                    runningDistance = 2.55,
                    spO2 = 97.44,
                    energyExpenditure = 543.29,
                    vitalCapacity = 3.23
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 28, 0, 0),
                    steps = 11867,
                    walkingDistance = 9.49,
                    exerciseDuration = "01:15:55",
                    heartRate = 77,
                    floorsClimbed = 14,
                    runningDistance = 6.32,
                    spO2 = 96.89,
                    energyExpenditure = 730.48,
                    vitalCapacity = 3.97
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 27, 0, 0),
                    steps = 13458,
                    walkingDistance = 10.77,
                    exerciseDuration = "01:03:18",
                    heartRate = 92,
                    floorsClimbed = 18,
                    runningDistance = 5.86,
                    spO2 = 95.13,
                    energyExpenditure = 846.19,
                    vitalCapacity = 4.24
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 26, 0, 0),
                    steps = 10932,
                    walkingDistance = 8.75,
                    exerciseDuration = "01:29:47",
                    heartRate = 85,
                    floorsClimbed = 12,
                    runningDistance = 4.39,
                    spO2 = 94.56,
                    energyExpenditure = 712.34,
                    vitalCapacity = 3.86
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 25, 0, 0),
                    steps = 7432,
                    walkingDistance = 5.94,
                    exerciseDuration = "00:37:22",
                    heartRate = 78,
                    floorsClimbed = 11,
                    runningDistance = 3.24,
                    spO2 = 97.29,
                    energyExpenditure = 407.66,
                    vitalCapacity = 2.89
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 24, 0, 0),
                    steps = 8254,
                    walkingDistance = 6.6,
                    exerciseDuration = "01:04:11",
                    heartRate = 80,
                    floorsClimbed = 7,
                    runningDistance = 2.13,
                    spO2 = 98.41,
                    energyExpenditure = 476.18,
                    vitalCapacity = 3.14
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 23, 0, 0),
                    steps = 9632,
                    walkingDistance = 7.71,
                    exerciseDuration = "01:17:03",
                    heartRate = 86,
                    floorsClimbed = 13,
                    runningDistance = 5.49,
                    spO2 = 95.62,
                    energyExpenditure = 609.42,
                    vitalCapacity = 3.67
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 22, 0, 0),
                    steps = 7458,
                    walkingDistance = 5.97,
                    exerciseDuration = "00:53:29",
                    heartRate = 91,
                    floorsClimbed = 16,
                    runningDistance = 1.88,
                    spO2 = 96.55,
                    energyExpenditure = 412.31,
                    vitalCapacity = 2.75
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 21, 0, 0),
                    steps = 12345,
                    walkingDistance = 9.88,
                    exerciseDuration = "01:32:10",
                    heartRate = 83,
                    floorsClimbed = 19,
                    runningDistance = 7.03,
                    spO2 = 97.47,
                    energyExpenditure = 812.19,
                    vitalCapacity = 4.12
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 20, 0, 0),
                    steps = 8576,
                    walkingDistance = 6.86,
                    exerciseDuration = "00:45:37",
                    heartRate = 75,
                    floorsClimbed = 10,
                    runningDistance = 2.95,
                    spO2 = 96.03,
                    energyExpenditure = 463.52,
                    vitalCapacity = 3.29
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 19, 0, 0),
                    steps = 6754,
                    walkingDistance = 5.4,
                    exerciseDuration = "01:28:44",
                    heartRate = 88,
                    floorsClimbed = 5,
                    runningDistance = 1.78,
                    spO2 = 97.92,
                    energyExpenditure = 338.47,
                    vitalCapacity = 2.96
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 18, 0, 0),
                    steps = 11432,
                    walkingDistance = 9.15,
                    exerciseDuration = "00:59:12",
                    heartRate = 79,
                    floorsClimbed = 12,
                    runningDistance = 3.66,
                    spO2 = 95.86,
                    energyExpenditure = 688.24,
                    vitalCapacity = 3.78
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 17, 0, 0),
                    steps = 9314,
                    walkingDistance = 7.45,
                    exerciseDuration = "01:22:58",
                    heartRate = 84,
                    floorsClimbed = 14,
                    runningDistance = 4.12,
                    spO2 = 96.67,
                    energyExpenditure = 579.13,
                    vitalCapacity = 3.42
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 16, 0, 0),
                    steps = 10769,
                    walkingDistance = 8.62,
                    exerciseDuration = "01:35:27",
                    heartRate = 82,
                    floorsClimbed = 17,
                    runningDistance = 5.98,
                    spO2 = 98.34,
                    energyExpenditure = 725.81,
                    vitalCapacity = 4.05
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 15, 0, 0),
                    steps = 8796,
                    walkingDistance = 7.04,
                    exerciseDuration = "00:46:21",
                    heartRate = 90,
                    floorsClimbed = 8,
                    runningDistance = 2.07,
                    spO2 = 95.98,
                    energyExpenditure = 441.36,
                    vitalCapacity = 3.19
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 14, 0, 0),
                    steps = 13098,
                    walkingDistance = 10.49,
                    exerciseDuration = "01:10:25",
                    heartRate = 77,
                    floorsClimbed = 13,
                    runningDistance = 6.35,
                    spO2 = 97.24,
                    energyExpenditure = 836.47,
                    vitalCapacity = 4.28
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 13, 0, 0),
                    steps = 6854,
                    walkingDistance = 5.48,
                    exerciseDuration = "00:39:17",
                    heartRate = 86,
                    floorsClimbed = 10,
                    runningDistance = 2.14,
                    spO2 = 96.13,
                    energyExpenditure = 345.22,
                    vitalCapacity = 2.74
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 12, 0, 0),
                    steps = 9578,
                    walkingDistance = 7.66,
                    exerciseDuration = "01:16:44",
                    heartRate = 88,
                    floorsClimbed = 9,
                    runningDistance = 4.28,
                    spO2 = 97.59,
                    energyExpenditure = 614.29,
                    vitalCapacity = 3.83
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 11, 0, 0),
                    steps = 11246,
                    walkingDistance = 9.0,
                    exerciseDuration = "01:23:15",
                    heartRate = 85,
                    floorsClimbed = 11,
                    runningDistance = 3.95,
                    spO2 = 96.42,
                    energyExpenditure = 708.34,
                    vitalCapacity = 3.90
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 10, 0, 0),
                    steps = 8431,
                    walkingDistance = 6.74,
                    exerciseDuration = "00:52:38",
                    heartRate = 92,
                    floorsClimbed = 7,
                    runningDistance = 2.58,
                    spO2 = 95.71,
                    energyExpenditure = 503.47,
                    vitalCapacity = 3.12
                ),
                DailyActivity(
                    userID = 1,
                    date = LocalDateTime.of(2024, 1, 9, 0, 0),
                    steps = 7658,
                    walkingDistance = 6.13,
                    exerciseDuration = "01:01:12",
                    heartRate = 79,
                    floorsClimbed = 12,
                    runningDistance = 1.89,
                    spO2 = 97.85,
                    energyExpenditure = 398.22,
                    vitalCapacity = 2.83
                )
            )
        }

        // 添加卡片展示信息
        private fun provideInitialDisplayCards(): List<DisplayCard> {
            return listOf(
                DisplayCard(
                    id = 1,
                    cardName = "AbstractStepCard",
                    isDisplayed = true
                ),
                DisplayCard(
                    id = 2,
                    cardName = "AbstractFloorClimbedCard",
                    isDisplayed = true
                ), DisplayCard(
                    id = 3,
                    cardName = "AbstractWalkingDistanceCard",
                    isDisplayed = true
                ), DisplayCard(
                    id = 4,
                    cardName = "AbstractExerciseDurationCard",
                    isDisplayed = true
                ), DisplayCard(
                    id = 5,
                    cardName = "AbstractRunningDistanceCard",
                    isDisplayed = true
                ), DisplayCard(
                    id = 6,
                    cardName = "AbstractEnergyExpenditureCard",
                    isDisplayed = true
                ), DisplayCard(
                    id = 7,
                    cardName = "AbstractSleepRecordCard",
                    isDisplayed = true
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


