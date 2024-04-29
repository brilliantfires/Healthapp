package com.example.healthapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
import com.example.healthapp.data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SyncViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {
    init {
        getAllUserFromMySQL()
        getDisplayCardsByUserId()
        getAuthorsByUserId()
        getArticleTagRelationsByUserId()
        getArticlesByUserId()
        getArticleMediaByUserId()
        getArticleTagsByUserId()
    }

    fun syncAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataRepository.syncAllData()
            } catch (e: Exception) {
                // Handle any specific exceptions if needed
                println("Exception during sync: ${e.localizedMessage}")
            }
        }
    }

    fun pullAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataRepository.pullAllData()
            } catch (e: Exception) {
                // Handle any specific exceptions if needed
                println("Exception during sync: ${e.localizedMessage}")
            }
        }
    }
    fun pullAllArticle() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataRepository.pullAllArticle()
            } catch (e: Exception) {
                // Handle any specific exceptions if needed
                println("Exception during sync: ${e.localizedMessage}")
            }
        }
    }

    val registrationStatus = MutableLiveData<Boolean>()

    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            val result = dataRepository.registerUser(username, email, password)
            registrationStatus.postValue(result)
        }
    }

    val allUser = MutableLiveData<List<User>?>()
    fun getAllUserFromMySQL() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getAllUserFromMySQL()
            allUser.postValue(result)
        }
    }

    val userById = MutableLiveData<User?>()
    fun getUserById(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getUserById(userId)
            userById.postValue(result)
        }
    }

    // LiveData用于持有用户的睡眠记录
    val userSleepRecords = MutableLiveData<List<SleepRecord>?>()

    // LiveData用于持有用户的身体健康档案
    val userPhysicalProfile = MutableLiveData<PhysicalProfile?>()

    // LiveData用于持有用户的营养记录
    val userNutritionRecords = MutableLiveData<List<NutritionRecord>?>()

    // LiveData用于持有用户的用药记录
    val userMedicationRecords = MutableLiveData<List<MedicationRecord>?>()

    // LiveData用于持有用户的健康指标
    val userHealthIndicators = MutableLiveData<List<HealthIndicator>?>()

    // LiveData用于持有用户的运动记录
    val userExerciseRecords = MutableLiveData<List<ExerciseRecord>?>()

    // LiveData用于持有用户的展示卡片
    val userDisplayCards = MutableLiveData<List<DisplayCard>?>()

    // LiveData用于持有用户的日常活动
    val userDailyActivities = MutableLiveData<List<DailyActivity>?>()

    // LiveData用于持有作者信息
    val userAuthors = MutableLiveData<List<Author>?>()

    // LiveData用于持有文章标签关系
    val userArticleTagRelations = MutableLiveData<List<ArticleTagRelation>?>()

    // 文章标签
    val userArticleTags = MutableLiveData<List<ArticleTag>?>()

    // LiveData用于持有用户的文章
    val userArticles = MutableLiveData<List<Article>?>()

    // LiveData用于持有用户的文章媒体
    val userArticleMedia = MutableLiveData<List<ArticleMedia>?>()

    // 从数据库获取用户的睡眠记录
    fun getSleepRecordByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getSleepRecordByUserId(userId)
            userSleepRecords.postValue(result)
        }
    }

    // 从数据库获取用户的身体健康档案
    fun getPhysicalProfileByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getPhysicalProfileByUserId(userId)
            userPhysicalProfile.postValue(result)
        }
    }

    // 获取用户的营养记录
    fun getNutritionRecordsByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getNutritionRecordsByUserId(userId)
            userNutritionRecords.postValue(result)
        }
    }

    // 获取用户的用药记录
    fun getMedicationRecordsByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getMedicationRecordsByUserId(userId)
            userMedicationRecords.postValue(result)
        }
    }

    // 获取用户的健康指标
    fun getHealthIndicatorsByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getHealthIndicatorsByUserId(userId)
            userHealthIndicators.postValue(result)
        }
    }

    // 获取用户的运动记录
    fun getExerciseRecordsByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getExerciseRecordsByUserId(userId)
            userExerciseRecords.postValue(result)
        }
    }

    // 获取用户的展示卡片
    fun getDisplayCardsByUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getDisplayCardsByUserId()
            userDisplayCards.postValue(result)
        }
    }

    // 获取用户的日常活动
    fun getDailyActivitiesByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getDailyActivitiesByUserId(userId)
            userDailyActivities.postValue(result)
        }
    }

    // 获取用户相关的作者信息
    fun getAuthorsByUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getAuthorsByUserId()
            userAuthors.postValue(result)
        }
    }

    // 获取用户相关的文章标签关系
    fun getArticleTagRelationsByUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getArticleTagRelationsByUserId()
            userArticleTagRelations.postValue(result)
        }
    }

    // 获取用户相关的文章标签
    fun getArticleTagsByUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getArticleTagsByUserId()
            userArticleTags.postValue(result)
        }
    }

    // 获取用户相关的文章
    fun getArticlesByUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getArticlesByUserId()
            userArticles.postValue(result)
        }
    }

    // 获取用户相关的文章媒体
    fun getArticleMediaByUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dataRepository.getArticleMediaByUserId()
            userArticleMedia.postValue(result)
        }
    }

    // 进行数据更新
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _dailyActivity = MutableLiveData<DailyActivity?>()
    val dailyActivity: LiveData<DailyActivity?> = _dailyActivity

    private val _exerciseRecord = MutableLiveData<ExerciseRecord?>()
    val exerciseRecord: LiveData<ExerciseRecord?> = _exerciseRecord

    private val _healthIndicator = MutableLiveData<HealthIndicator?>()
    val healthIndicator: LiveData<HealthIndicator?> = _healthIndicator

    private val _medicationRecord = MutableLiveData<MedicationRecord?>()
    val medicationRecord: LiveData<MedicationRecord?> = _medicationRecord

    private val _nutritionRecord = MutableLiveData<NutritionRecord?>()
    val nutritionRecord: LiveData<NutritionRecord?> = _nutritionRecord

    private val _physicalProfile = MutableLiveData<PhysicalProfile?>()
    val physicalProfile: LiveData<PhysicalProfile?> = _physicalProfile

    private val _sleepRecord = MutableLiveData<SleepRecord?>()
    val sleepRecord: LiveData<SleepRecord?> = _sleepRecord

    fun updateUser(user: User) {
        viewModelScope.launch() {
            _user.value = dataRepository.updateUser(user.userId, user)
        }
    }

    fun updateDailyActivity(dailyActivity: DailyActivity) {
        viewModelScope.launch {
            _dailyActivity.value =
                dataRepository.updateDailyActivity(dailyActivity.activityID, dailyActivity)
        }
    }

    fun updateExerciseRecord(record: ExerciseRecord) {
        viewModelScope.launch {
            _exerciseRecord.value = dataRepository.updateExerciseRecord(record.exerciseID, record)
        }
    }

    fun updateHealthIndicator(indicator: HealthIndicator) {
        viewModelScope.launch {
            _healthIndicator.value =
                dataRepository.updateHealthIndicator(indicator.indicatorID, indicator)
        }
    }

    fun updateMedicationRecord(record: MedicationRecord) {
        viewModelScope.launch {
            _medicationRecord.value =
                dataRepository.updateMedicationRecord(record.medicationID, record)
        }
    }

    fun updateNutritionRecord(record: NutritionRecord) {
        viewModelScope.launch {
            _nutritionRecord.value =
                dataRepository.updateNutritionRecord(record.nutritionID, record)
        }
    }

    fun updatePhysicalProfile(profile: PhysicalProfile) {
        viewModelScope.launch {
            _physicalProfile.value = dataRepository.updatePhysicalProfile(profile.userID, profile)
        }
    }

    fun updateSleepRecord(record: SleepRecord) {
        viewModelScope.launch {
            _sleepRecord.value = dataRepository.updateSleepRecord(record.sleepID, record)
        }
    }

    private val _author = MutableLiveData<Author?>()
    val author: LiveData<Author?> = _author

    private val _articleTagRelation = MutableLiveData<ArticleTagRelation?>()
    val articleTagRelation: LiveData<ArticleTagRelation?> = _articleTagRelation

    private val _articleTag = MutableLiveData<ArticleTag?>()
    val articleTag: LiveData<ArticleTag?> = _articleTag

    private val _articleMedia = MutableLiveData<ArticleMedia?>()
    val articleMedia: LiveData<ArticleMedia?> = _articleMedia

    private val _article = MutableLiveData<Article?>()
    val article: LiveData<Article?> = _article

    fun updateAuthor(author: Author) {
        viewModelScope.launch {
            _author.value = dataRepository.updateAuthor(author.authorID, author)
        }
    }

    fun updateArticleTagRelation(articleTagRelation: ArticleTagRelation) {
        viewModelScope.launch {
            _articleTagRelation.value = dataRepository.updateArticleTagRelation(
                articleTagRelation.articleID,
                articleTagRelation
            )
        }
    }

    fun updateArticleTag(articleTag: ArticleTag) {
        viewModelScope.launch {
            _articleTag.value = dataRepository.updateArticleTag(articleTag.tagID, articleTag)
        }
    }

    fun updateArticleMedia(articleMedia: ArticleMedia) {
        viewModelScope.launch {
            _articleMedia.value =
                dataRepository.updateArticleMedia(articleMedia.mediaID, articleMedia)
        }
    }

    fun updateArticle(article: Article) {
        viewModelScope.launch {
            _article.value = dataRepository.updateArticle(article.articleID, article)
        }
    }

    fun updateDisplayCard(displayCard: DisplayCard) {
        viewModelScope.launch {
            dataRepository.updateDisplayCard(displayCard.id, displayCard)
        }
    }
}

class SyncViewModelFactory(private val dataRepository: DataRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SyncViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SyncViewModel(dataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
