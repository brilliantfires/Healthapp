package com.example.healthapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.healthapp.data.HealthAppDatabase
import com.example.healthapp.data.mysql.RetrofitClient
import com.example.healthapp.data.repository.ArticleMediaRepository
import com.example.healthapp.data.repository.ArticleTagRelationRepository
import com.example.healthapp.data.repository.ArticleTagsRepository
import com.example.healthapp.data.repository.ArticlesRepository
import com.example.healthapp.data.repository.AuthorsRepository
import com.example.healthapp.data.repository.DailyActivityRepository
import com.example.healthapp.data.repository.DataRepository
import com.example.healthapp.data.repository.DisplayCardRepository
import com.example.healthapp.data.repository.ExerciseRecordRepository
import com.example.healthapp.data.repository.HealthIndicatorRepository
import com.example.healthapp.data.repository.MedicationRecordRepository
import com.example.healthapp.data.repository.NutritionRecordRepository
import com.example.healthapp.data.repository.PhysicalProfileRepository
import com.example.healthapp.data.repository.SleepRecordRepository
import com.example.healthapp.data.repository.UserRepository


class HealthApplication : Application() {

    // 创建 SharedPreferences 的实例
    val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("health_app_preferences", Context.MODE_PRIVATE)
    }

    val userRepository: UserRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        val apiService = RetrofitClient.apiService
        UserRepository(apiService, database.userDao(), sharedPreferences)
    }

    val physicalProfileRepository: PhysicalProfileRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        val apiService = RetrofitClient.apiService
        PhysicalProfileRepository(apiService, database.physicalProfileDao())
    }

    val healthIndicatorRepository: HealthIndicatorRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        HealthIndicatorRepository(database.healthIndicationDao())
    }

    val exerciseRecordRepository: ExerciseRecordRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        ExerciseRecordRepository(database.exerciseRecordDao())
    }

    val sleepRecordRepository: SleepRecordRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        val apiService = RetrofitClient.apiService
        SleepRecordRepository(apiService, database.sleepRecordDao())
    }

    val nutritionRecordRepository: NutritionRecordRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        NutritionRecordRepository(database.nutritionRecordDao())
    }

    val authorRepository: AuthorsRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        AuthorsRepository(database.authorDao())
    }

    val medicationRecordRepository: MedicationRecordRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        MedicationRecordRepository(database.medicationRecordDao())
    }

    val dailyActivityRepository: DailyActivityRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        DailyActivityRepository(database.dailyActivityDAO())
    }

    val articleTagsRepository: ArticleTagsRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        ArticleTagsRepository(database.articleTagsDao())
    }

    val articleTagRelationRepository: ArticleTagRelationRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        ArticleTagRelationRepository(database.articleTagRelationDao())
    }

    val articlesRepository: ArticlesRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        ArticlesRepository(database.articlesDao())
    }

    val articleMediaRepository: ArticleMediaRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        ArticleMediaRepository(database.articleMediaDAO())
    }

    val displayCardRepository: DisplayCardRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        DisplayCardRepository(database.displayCardDAO())
    }
    val dataRepository: DataRepository by lazy {
        val apiService = RetrofitClient.apiService
        val database = HealthAppDatabase.getDatabase(this)
        DataRepository(
            apiService,
            database.articleMediaDAO(),
            database.articlesDao(),
            database.articleTagRelationDao(),
            database.articleTagsDao(),
            database.authorDao(),
            database.dailyActivityDAO(),
            database.displayCardDAO(),
            database.exerciseRecordDao(),
            database.healthIndicationDao(),
            database.medicationRecordDao(),
            database.nutritionRecordDao(),
            database.physicalProfileDao(),
            database.sleepRecordDao(),
            database.userDao()
        )
    }
}
