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
                    totalDuration = "04:00:00",
                    deepSleep = "01:00:00",
                    lightSleep = "01:00:00",
                    remSleep = "01:00:00",
                    awakeDuration = "01:00:00"
                ),
                SleepRecord(
                    sleepID = 2,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 5, 0, 0),
                    totalDuration = "08:00:00",
                    deepSleep = "03:00:00",
                    lightSleep = "02:00:00",
                    remSleep = "02:00:00",
                    awakeDuration = "01:00:00"
                ),
                SleepRecord(

                    sleepID = 3,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 4, 0, 0),
                    totalDuration = "09:00:00",
                    deepSleep = "05:00:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:00:00"
                ),
                SleepRecord(

                    sleepID = 4,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 3, 0, 0),
                    totalDuration = "08:00:00",
                    deepSleep = "04:00:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:00:00"
                ),
                SleepRecord(

                    sleepID = 5,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 2, 0, 0),
                    totalDuration = "12:00:00",
                    deepSleep = "05:00:00",
                    lightSleep = "03:00:00",
                    remSleep = "04:00:00",
                    awakeDuration = "00:00:00"
                ),
                SleepRecord(

                    sleepID = 6,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 1, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "03:00:00",
                    lightSleep = "03:00:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:00:00"
                ),
                SleepRecord(

                    sleepID = 7,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 31, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "04:00:00",
                    lightSleep = "01:00:00",
                    remSleep = "02:00:00",
                    awakeDuration = "00:00:00"
                ),
                SleepRecord(

                    sleepID = 8,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 30, 0, 0),
                    totalDuration = "11:00:00",
                    deepSleep = "08:00:00",
                    lightSleep = "01:00:00",
                    remSleep = "02:00:00",
                    awakeDuration = "00:00:00"
                ),
                SleepRecord(

                    sleepID = 9,
                    userID = 1,
                    date = LocalDateTime.of(2024, 4, 8, 0, 0),
                    totalDuration = "07:00:00",
                    deepSleep = "02:00:00",
                    lightSleep = "01:00:00",
                    remSleep = "03:00:00",
                    awakeDuration = "01:00:00"
                ),
                SleepRecord(

                    sleepID = 10,
                    userID = 1,
                    date = LocalDateTime.of(2024, 3, 28, 0, 0),
                    totalDuration = "05:00:00",
                    deepSleep = "02:00:00",
                    lightSleep = "02:00:00",
                    remSleep = "01:00:00",
                    awakeDuration = "00:00:00"
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
                    userID = 7,
                    date = LocalDateTime.of(2024, 4, 6, 0, 0),
                    mealType = "Snack",
                    calories = 863.91,
                    protein = 24.57,
                    fat = 2.21,
                    carbohydrates = 65.69
                ),
                NutritionRecord(
                    nutritionID = 2,
                    userID = 4,
                    date = LocalDateTime.of(2024, 4, 5, 0, 0),
                    mealType = "Snack",
                    calories = 569.29,
                    protein = 27.37,
                    fat = 16.78,
                    carbohydrates = 72.27
                ),
                NutritionRecord(
                    nutritionID = 3,
                    userID = 9,
                    date = LocalDateTime.of(2024, 4, 4, 0, 0),
                    mealType = "Breakfast",
                    calories = 830.55,
                    protein = 27.46,
                    fat = 14.57,
                    carbohydrates = 78.66
                ),
                NutritionRecord(
                    nutritionID = 4,
                    userID = 2,
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
                    userID = 10,
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
                    userID = 3,
                    date = LocalDateTime.of(2024, 4, 4, 0, 0),
                    drugName = "Ciprofloxacin",
                    dosage = "500mg",
                    frequency = "Once Daily",
                    purpose = "Antibiotic"
                ),
                MedicationRecord(
                    medicationID = 4,
                    userID = 7,
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
                    userID = 8,
                    date = LocalDateTime.of(2024, 4, 6, 0, 0),
                    bloodPressure = "115/89",
                    cholesterol = 199.15,
                    glucoseLevel = 127.43,
                    otherIndicators = "Oxygen Saturation: 95-100%"
                ),
                HealthIndicator(
                    indicatorID = 2,
                    userID = 10,
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
                    userID = 4,
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


