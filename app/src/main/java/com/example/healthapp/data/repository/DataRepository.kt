package com.example.healthapp.data.repository

import android.util.Log
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
import com.example.healthapp.data.mysql.ApiService
import com.example.healthapp.data.mysql.RetrofitClient
import java.time.LocalDateTime

class DataRepository(
    private val apiService: ApiService,
    private val articleMediaDAO: ArticleMediaDAO,
    private val articlesDAO: ArticlesDAO,
    private val articleTagRelationDAO: ArticleTagRelationDAO,
    private val articleTagsDAO: ArticleTagsDAO,
    private val authorsDAO: AuthorsDAO,
    private val dailyActivityDAO: DailyActivityDAO,
    private val displayCardDAO: DisplayCardDao,
    private val exerciseRecordDAO: ExerciseRecordDAO,
    private val healthIndicatorDAO: HealthIndicatorDAO,
    private val medicationRecordDAO: MedicationRecordDAO,
    private val nutritionRecordDAO: NutritionRecordDAO,
    private val physicalProfileDAO: PhysicalProfileDAO,
    private val sleepRecordDAO: SleepRecordDAO,
    private val userDAO: UserDAO
) {

    // 同步所有数据到远程服务器
    suspend fun syncAllData() {
        try {
            syncUsers()
            syncSleepRecords()
            syncHealthIndicators()
            syncExerciseRecords()
            syncMedicationRecords()
            syncNutritionRecords()
            syncPhysicalProfiles()
            syncDailyActivities()
            syncDisplayCards()
            syncAuthors()
            syncArticleTags()
            syncArticleTagRelations()
            syncArticles()
            syncArticleMedia()
        } catch (e: Exception) {
            // 处理可能的异常，如网络错误、数据库访问问题等
        }
    }

    private suspend fun syncUsers() {
        val users = userDAO.getAllUsersN()
        users.forEach { user ->
            apiService.createUser(user)
        }
    }

    private suspend fun syncSleepRecords() {
        val sleepRecords = sleepRecordDAO.getAllSleepRecords()
        sleepRecords.forEach { record ->
            apiService.createSleepRecord(record)
        }
    }

    private suspend fun syncHealthIndicators() {
        val healthIndicators = healthIndicatorDAO.getAllHealthMetricsN()
        healthIndicators.forEach { indicator ->
            apiService.createHealthIndicator(indicator)
        }
    }

    private suspend fun syncExerciseRecords() {
        val exerciseRecords = exerciseRecordDAO.getAllExerciseRecordsN()
        exerciseRecords.forEach { record ->
            apiService.createExerciseRecord(record)
        }
    }

    private suspend fun syncMedicationRecords() {
        val medicationRecords = medicationRecordDAO.getAllMedicationRecordsN()
        medicationRecords.forEach { record ->
            apiService.createMedicationRecord(record)
        }
    }

    private suspend fun syncNutritionRecords() {
        val nutritionRecords = nutritionRecordDAO.getAllNutritionRecords()
        nutritionRecords.forEach { record ->
            apiService.createNutritionRecord(record)
        }
    }

    private suspend fun syncPhysicalProfiles() {
        val physicalProfiles = physicalProfileDAO.getAllUserProfilesN()
        physicalProfiles.forEach { profile ->
            apiService.createPhysicalProfile(profile)
        }
    }

    private suspend fun syncDailyActivities() {
        val dailyActivities = dailyActivityDAO.getAllDailyActivitiesN()
        dailyActivities.forEach { activity ->
            apiService.createDailyActivity(activity)
        }
    }

    private suspend fun syncDisplayCards() {
        val displayCards = displayCardDAO.getAllCardsN()
        displayCards.forEach { card ->
            apiService.createDisplayCard(card)
        }
    }

    private suspend fun syncAuthors() {
        val authors = authorsDAO.getAllAuthorsN()
        authors.forEach { author ->
            apiService.createAuthor(author)
        }
    }

    private suspend fun syncArticleTags() {
        val articleTags = articleTagsDAO.getAllTagsN()
        articleTags.forEach { tag ->
            apiService.createArticleTag(tag)
        }
    }

    private suspend fun syncArticleTagRelations() {
        val articleTagRelations = articleTagRelationDAO.getAllArticleTagRelationsN()
        articleTagRelations.forEach { relation ->
            apiService.createArticleTagRelation(relation)
        }
    }

    private suspend fun syncArticles() {
        val articles = articlesDAO.getAllArticlesN()
        articles.forEach { article ->
            apiService.createArticle(article)
        }
    }

    private suspend fun syncArticleMedia() {
        val articleMedia = articleMediaDAO.allArticleMediaN()
        articleMedia.forEach { media ->
            apiService.createArticleMedia(media)
        }
    }

    val userId = 1
    suspend fun pullAllData() {
        try {
            pullUsers()
            pullSleepRecords()
            pullHealthIndicators()
            pullExerciseRecords()
            pullMedicationRecords()
            pullNutritionRecords()
            pullPhysicalProfiles()
            pullDailyActivities()
            pullDisplayCards()
            pullAuthors()
            //delay(1000) // 等待1秒
            pullArticleTags()
            //delay(1000) // 等待1秒

            //delay(1000) // 等待1秒
            pullArticles()
            //delay(1000) // 等待1秒
            pullArticleMedia()
            pullArticleTagRelations()
        } catch (e: Exception) {
            // Handle possible exceptions
        }
    }

    suspend fun pullAllArticle() {
        try {
            pullAuthors()
            //delay(1000) // 等待1秒
            pullArticleTags()
            //delay(1000) // 等待1秒

            //delay(1000) // 等待1秒
            pullArticles()
            //delay(1000) // 等待1秒
            pullArticleMedia()
            pullArticleTagRelations()
        } catch (e: Exception) {
            // Handle possible exceptions
        }
    }


    private suspend fun pullUsers() {
        val response = apiService.getUserById(userId)
        if (response.isSuccessful) {
            response.body()?.let {
                userDAO.updateUser(it)
            }
        }
    }

    private suspend fun pullSleepRecords() {
        val response = apiService.getSleepRecordsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { records ->
                sleepRecordDAO.insertAll(records)
            }
        }
    }

    private suspend fun pullPhysicalProfiles() {
        val response = apiService.getPhysicalProfileById(userId)
        if (response.isSuccessful) {
            response.body()?.let { networkPhysicalProfile ->
                physicalProfileDAO.updateUserProfile(networkPhysicalProfile)
            }
        }
    }

    private suspend fun pullNutritionRecords() {
        val response = RetrofitClient.apiService.getNutritionRecordsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { nutritionRecords ->
                nutritionRecords.forEach { networkNutritionRecord ->
                    nutritionRecordDAO.insertNutritionRecord(networkNutritionRecord)
                }
            }
        }
    }

    private suspend fun pullDailyActivities() {
        val response = RetrofitClient.apiService.getDailyActivitiesByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { dailyActivities ->
                dailyActivityDAO.insertAll(dailyActivities)
            }
        }
    }

    private suspend fun pullHealthIndicators() {
        val response =
            apiService.getHealthIndicatorsByUserId(userId) // Assuming you have a way to determine userId
        if (response.isSuccessful) {
            response.body()?.let { indicators ->
                healthIndicatorDAO.insertAll(indicators)
            }
        }
    }

    private suspend fun pullDisplayCards() {
        val response = RetrofitClient.apiService.getAllDisplayCards()
        if (response.isSuccessful) {
            response.body()?.let { cards ->
                displayCardDAO.insertAll(cards)
            }
        }
    }

    private suspend fun pullExerciseRecords() {
        val response = apiService.getExerciseRecordsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { records ->
                exerciseRecordDAO.insertAll(records)
            }
        }
    }

    private suspend fun pullMedicationRecords() {
        val response = apiService.getMedicationRecordsByUserId(userId)
        if (response.isSuccessful) {
            response.body()?.let { records ->
                medicationRecordDAO.insertAll(records)
            }
        }
    }

    private suspend fun pullArticles() {
        val response = RetrofitClient.apiService.getAllArticles()
        if (response.isSuccessful) {
            response.body()?.let { authors ->
                articlesDAO.insertAll(authors)
            }
        }
    }

    private suspend fun pullArticleMedia() {
        val response = RetrofitClient.apiService.getAllArticleMedia()
        if (response.isSuccessful) {
            response.body()?.let { authors ->
                articleMediaDAO.insertAll(authors)
            }
        }
    }

    private suspend fun pullAuthors() {
        val response = apiService.getAllAuthors()
        if (response.isSuccessful) {
            response.body()?.let { media ->
                authorsDAO.insertAll(media)
            }
        }
    }

    private suspend fun pullArticleTags() {
        val response = apiService.getAllArticleTags()
        if (response.isSuccessful) {
            response.body()?.let { media ->
                articleTagsDAO.insertAll(media)
            }
        }
    }

    private suspend fun pullArticleTagRelations() {
        val response = apiService.getAllArticleTagRelations()
        if (response.isSuccessful) {
            response.body()?.let { media ->
                articleTagRelationDAO.insertAll(media)
            }
        }
    }

    // 用户注册功能
    suspend fun registerUser(username: String, email: String, password: String): Boolean {
        var newUser = User(
            username = username,
            email = email,
            phoneNumber = "",
            passwordHash = password,
            profilePicture = "/images/profiles/user1",
            role = "USER",
            bloodType = "A",
            gender = "MALE",
            dateOfBirth = LocalDateTime.of(1970, 1, 1, 0, 0),
            dateCreated = LocalDateTime.now(),
            lastLogin = LocalDateTime.now(),
            isWheelchairUser = false,
            skinType = "TYPE_II",
            heartRateAffectingDrugs = "null"
        )

        // 首先调用 API 创建用户并检查响应是否成功
        val response1 = apiService.createUser(newUser)
        if (response1.isSuccessful) {
            response1.body()?.let {
                // 从响应中获取更新后的用户信息，其中应包含数据库分配的 userId
                //newUser.userId = it.userId
                // 使用更新后的 newUser 创建物理配置文件
                val newProfile = PhysicalProfile(
                    userID = it.userId, // 确保 userID 不是 null
                    height = 0.0,
                    weight = 0.0,
                    bmi = 0.0,
                    myopiaDegree = null,
                    wearsGlasses = false,
                    bloodType = "A",
                    shoeSize = 0.0
                )
                newUser = it
                // 再次调用 API 创建物理配置文件
                val response2 = apiService.createPhysicalProfile(newProfile)
                if (response2.isSuccessful) {
                    response2.body()?.let { profile ->
                        // 只有当两个 API 调用都成功后才将用户和物理配置文件保存到本地数据库
                        userDAO.insertUser(newUser)
                        physicalProfileDAO.insert(profile)
                        return true
                    }
                }
            }
        }
        return false
    }

    // 从MySQL获取所有的数据
    // 首先是获取所有的用户数据
    suspend fun getAllUserFromMySQL(): List<User>? {
        val response = apiService.getAllUsers()
        if (response.isSuccessful) {
            // 检查响应体是否为null，并返回数据
            return response.body() ?: emptyList()
        }
        return null
    }

    // 根据UserId获取SleepRecord数据
    suspend fun getSleepRecordByUserId(userId: Int): List<SleepRecord> {
        val response = apiService.getSleepRecordsByUserId(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取PhysicalProfile数据
    suspend fun getPhysicalProfileByUserId(userId: Int): PhysicalProfile? {
        val response = apiService.getPhysicalProfileById(userId)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    // 根据UserId获取NutritionRecord数据
    suspend fun getNutritionRecordsByUserId(userId: Int): List<NutritionRecord> {
        val response = apiService.getNutritionRecordsByUserId(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取MedicationRecord数据
    suspend fun getMedicationRecordsByUserId(userId: Int): List<MedicationRecord> {
        val response = apiService.getMedicationRecordsByUserId(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取HealthIndicator数据
    suspend fun getHealthIndicatorsByUserId(userId: Int): List<HealthIndicator> {
        val response = apiService.getHealthIndicatorsByUserId(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取ExerciseRecord数据
    suspend fun getExerciseRecordsByUserId(userId: Int): List<ExerciseRecord> {
        val response = apiService.getExerciseRecordsByUserId(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取DisplayCard数据
    suspend fun getDisplayCardsByUserId(): List<DisplayCard> {
        val response = apiService.getAllDisplayCards()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取DailyActivity数据
    suspend fun getDailyActivitiesByUserId(userId: Int): List<DailyActivity> {
        val response = apiService.getDailyActivitiesByUserId(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取Authors数据
    suspend fun getAuthorsByUserId(): List<Author> {
        val response = apiService.getAllAuthors()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取ArticleTags数据
    suspend fun getArticleTagsByUserId(): List<ArticleTag> {
        val response = apiService.getAllArticleTags()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取ArticleTagRelation数据
    suspend fun getArticleTagRelationsByUserId(): List<ArticleTagRelation> {
        val response = apiService.getAllArticleTagRelations()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取Articles数据
    suspend fun getArticlesByUserId(): List<Article> {
        val response = apiService.getAllArticles()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据UserId获取ArticleMedia数据
    suspend fun getArticleMediaByUserId(): List<ArticleMedia> {
        val response = apiService.getAllArticleMedia()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    // 根据userId来获取User
    suspend fun getUserById(userId: Int): User? {
        val response = apiService.getUserById(userId)
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }

    // 进行数据更新的操作
    suspend fun updateUser(id: Int, user: User): User? {
        return try {
            val response = apiService.updateUser(id, user)
            if (response.isSuccessful) {
                Log.d("UpdateUser", "Success: ？？？")
                response.body()
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateDailyActivity(activityId: Int, dailyActivity: DailyActivity): DailyActivity? {
        return try {
            val response = apiService.updateDailyActivity(activityId, dailyActivity)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateExerciseRecord(id: Int, record: ExerciseRecord): ExerciseRecord? {
        return try {
            val response = apiService.updateExerciseRecord(id, record)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateHealthIndicator(id: Int, indicator: HealthIndicator): HealthIndicator? {
        return try {
            val response = apiService.updateHealthIndicator(id, indicator)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateMedicationRecord(id: Int, record: MedicationRecord): MedicationRecord? {
        return try {
            val response = apiService.updateMedicationRecord(id, record)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateNutritionRecord(id: Int, record: NutritionRecord): NutritionRecord? {
        return try {
            val response = apiService.updateNutritionRecord(id, record)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updatePhysicalProfile(userId: Int, profile: PhysicalProfile): PhysicalProfile? {
        return try {
            val response = apiService.updatePhysicalProfile(userId, profile)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateSleepRecord(id: Int, sleepRecord: SleepRecord): SleepRecord? {
        return try {
            val response = apiService.updateSleepRecord(id, sleepRecord)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateAuthor(id: Int, author: Author): Author? {
        return try {
            val response = apiService.updateAuthor(id, author)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateArticleTagRelation(
        articleID: Int,
        articleTagRelation: ArticleTagRelation
    ): ArticleTagRelation? {
        return try {
            val response = apiService.updateArticleTagRelation(articleID, articleTagRelation)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateArticleTag(tagID: Int, articleTag: ArticleTag): ArticleTag? {
        return try {
            val response = apiService.updateArticleTag(tagID, articleTag)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateArticleMedia(mediaID: Int, articleMedia: ArticleMedia): ArticleMedia? {
        return try {
            val response = apiService.updateArticleMedia(mediaID, articleMedia)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateArticle(id: Int, article: Article): Article? {
        return try {
            val response = apiService.updateArticle(id, article)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateDisplayCard(id: Int, displayCard: DisplayCard): DisplayCard? {
        return try {
            val response = apiService.updateDisplayCard(id, displayCard)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

}
