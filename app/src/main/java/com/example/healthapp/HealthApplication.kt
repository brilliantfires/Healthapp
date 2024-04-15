package com.example.healthapp

import android.app.Application
import com.example.healthapp.data.HealthAppDatabase
import com.example.healthapp.data.repository.ArticleMediaRepository
import com.example.healthapp.data.repository.ArticleTagRelationRepository
import com.example.healthapp.data.repository.ArticleTagsRepository
import com.example.healthapp.data.repository.ArticlesRepository
import com.example.healthapp.data.repository.AuthorsRepository
import com.example.healthapp.data.repository.DailyActivityRepository
import com.example.healthapp.data.repository.DisplayCardRepository
import com.example.healthapp.data.repository.ExerciseRecordRepository
import com.example.healthapp.data.repository.HealthIndicatorRepository
import com.example.healthapp.data.repository.MedicationRecordRepository
import com.example.healthapp.data.repository.NutritionRecordRepository
import com.example.healthapp.data.repository.PhysicalProfileRepository
import com.example.healthapp.data.repository.SleepRecordRepository
import com.example.healthapp.data.repository.UserRepository


class HealthApplication : Application() {

    val userRepository: UserRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        UserRepository(database.userDao())
    }

    val physicalProfileRepository: PhysicalProfileRepository by lazy {
        val database = HealthAppDatabase.getDatabase(this)
        PhysicalProfileRepository(database.physicalProfileDao())
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
        SleepRecordRepository(database.sleepRecordDao())
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

    companion object {
        // Optionally, if needed, add a companion object here
    }
}
