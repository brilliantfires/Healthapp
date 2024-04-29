package com.example.healthapp.data.mysql

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<User>

    @GET("users")
    suspend fun getAllUsers(): Response<List<User>?>

    @POST("users")
    suspend fun createUser(@Body user: User): Response<User>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: Int): Response<Void>

    @FormUrlEncoded
    @POST("users/login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<User>

    @GET("users/by-email")
    suspend fun getUserByEmail(@Query("email") email: String): Response<User>

    @PUT("users/updateByEmail")
    suspend fun updateUser(
        @Query("email") email: String,
        @Query("passwordHash") passwordHash: String
    ): Response<User>

    // Sleep Record APIs
    @GET("sleepRecords")
    suspend fun getAllSleepRecords(): Response<List<SleepRecord>>

    @POST("sleepRecords")
    suspend fun createSleepRecord(@Body sleepRecord: SleepRecord): Response<SleepRecord>

    @DELETE("sleepRecords/{id}")
    suspend fun deleteSleepRecord(@Path("id") id: Int): Response<Void>

    // 这里需要新添加一个函数，是根据userId获取所有的sleepRecords
    @GET("sleepRecords/user/{userID}")
    suspend fun getSleepRecordsByUserId(@Path("userID") userID: Int): Response<List<SleepRecord>>

    // Physical Profile APIs
    @GET("physicalProfiles/{userId}")
    suspend fun getPhysicalProfileById(@Path("userId") userId: Int): Response<PhysicalProfile>

    @POST("physicalProfiles")
    suspend fun createPhysicalProfile(@Body profile: PhysicalProfile): Response<PhysicalProfile>

    @DELETE("physicalProfiles/{userId}")
    suspend fun deletePhysicalProfile(@Path("userId") userId: Int): Response<Void>

    // Nutrition Record APIs
    @GET("nutritionRecords/user/{userId}")
    suspend fun getNutritionRecordsByUserId(@Path("userId") userId: Int): Response<List<NutritionRecord>>

    @POST("nutritionRecords")
    suspend fun createNutritionRecord(@Body record: NutritionRecord): Response<NutritionRecord>

    @DELETE("nutritionRecords/{id}")
    suspend fun deleteNutritionRecord(@Path("id") id: Int): Response<Void>

    // medication
    @GET("medicationRecords/user/{userId}")
    suspend fun getMedicationRecordsByUserId(@Path("userId") userId: Int): Response<List<MedicationRecord>>

    @POST("medicationRecords")
    suspend fun createMedicationRecord(@Body medicationRecord: MedicationRecord): Response<MedicationRecord>

    @DELETE("medicationRecords/{id}")
    suspend fun deleteMedicationRecord(@Path("id") id: Int): Response<Void>

    // Health Indicator APIs
    @GET("healthIndicators/user/{userId}")
    suspend fun getHealthIndicatorsByUserId(@Path("userId") userId: Int): Response<List<HealthIndicator>>

    @POST("healthIndicators")
    suspend fun createHealthIndicator(@Body indicator: HealthIndicator): Response<HealthIndicator>

    @DELETE("healthIndicators/{id}")
    suspend fun deleteHealthIndicator(@Path("id") id: Int): Response<Void>

    // Exercise Record APIs
    @GET("exerciseRecords/user/{userId}")
    suspend fun getExerciseRecordsByUserId(@Path("userId") userId: Int): Response<List<ExerciseRecord>>

    @POST("exerciseRecords")
    suspend fun createExerciseRecord(@Body record: ExerciseRecord): Response<ExerciseRecord>

    @DELETE("exerciseRecords/{id}")
    suspend fun deleteExerciseRecord(@Path("id") id: Int): Response<Void>

    // DisplayCard APIs
    @POST("displayCards")
    suspend fun createDisplayCard(@Body card: DisplayCard): Response<DisplayCard>

    @GET("displayCards/{id}")
    suspend fun getDisplayCardById(@Path("id") id: Int): Response<DisplayCard>

    @GET("displayCards")
    suspend fun getAllDisplayCards(): Response<List<DisplayCard>>

    @DELETE("displayCards/{id}")
    suspend fun deleteDisplayCard(@Path("id") id: Int): Response<Void>

    @PUT("displayCards/{id}/displayStatus")
    suspend fun updateDisplayStatus(
        @Path("id") id: Int,
        @Body isDisplayed: Boolean
    ): Response<DisplayCard>

    // DailyActivities APIs
    @POST("dailyActivities")
    suspend fun createDailyActivity(@Body activity: DailyActivity): Response<DailyActivity>

    @GET("dailyActivities/user/{userId}")
    suspend fun getDailyActivitiesByUserId(@Path("userId") userId: Int): Response<List<DailyActivity>>

    @DELETE("dailyActivities/{id}")
    suspend fun deleteDailyActivity(@Path("id") id: Int): Response<Void>

    // Author APIs
    @POST("authors")
    suspend fun createAuthor(@Body author: Author): Response<Author>

    @GET("authors/{authorId}")
    suspend fun getAuthorById(@Path("authorId") authorId: Int): Response<Author>

    @GET("authors/all")
    suspend fun getAllAuthors(): Response<List<Author>>

    @DELETE("authors/{authorId}")
    suspend fun deleteAuthor(@Path("authorId") authorId: Int): Response<Void>

    // ArticleTagRelation APIs
    @POST("articleTagRelations")
    suspend fun createArticleTagRelation(@Body relation: ArticleTagRelation): Response<ArticleTagRelation>

    @DELETE("articleTagRelations/{articleId}/{tagId}")
    suspend fun deleteArticleTagRelation(
        @Path("articleId") articleId: Int,
        @Path("tagId") tagId: Int
    ): Response<Void>

    @GET("articleTagRelations/all")
    suspend fun getAllArticleTagRelations(): Response<List<ArticleTagRelation>>

    // ArticleTag APIs
    @POST("articleTags")
    suspend fun createArticleTag(@Body tag: ArticleTag): Response<ArticleTag>

    @GET("articleTags/{tagId}")
    suspend fun getArticleTagById(@Path("tagId") tagId: Int): Response<ArticleTag>

    @DELETE("articleTags/{tagId}")
    suspend fun deleteArticleTag(@Path("tagId") tagId: Int): Response<Void>

    @GET("articleTags/all")
    suspend fun getAllArticleTags(): Response<List<ArticleTag>>

    // ArticleMedia APIs
    @POST("articleMedia")
    suspend fun createArticleMedia(@Body media: ArticleMedia): Response<ArticleMedia>

    @GET("articleMedia/{mediaId}")
    suspend fun getArticleMediaById(@Path("mediaId") mediaId: Int): Response<ArticleMedia>

    @DELETE("articleMedia/{mediaId}")
    suspend fun deleteArticleMedia(@Path("mediaId") mediaId: Int): Response<Void>

    @GET("articleMedia/all")
    suspend fun getAllArticleMedia(): Response<List<ArticleMedia>>

    // Articles APIs
    @POST("articles")
    suspend fun createArticle(@Body article: Article): Response<Article>

    @GET("articles/{articleID}")
    suspend fun getArticleById(@Path("articleID") articleID: Int): Response<Article>

    @GET("articles/all")
    suspend fun getAllArticles(): Response<List<Article>>

    @DELETE("articles/{articleID}")
    suspend fun deleteArticle(@Path("articleID") articleID: Int): Response<Void>

    // 用于进行更新的函数
    // 更新用户信息
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): Response<User>

    // 更新每日活动记录
    @PUT("dailyActivities/{activityId}")
    suspend fun updateDailyActivity(
        @Path("activityId") activityId: Int,
        @Body dailyActivity: DailyActivity
    ): Response<DailyActivity>

    // 更新运动记录
    @PUT("exerciseRecords/{id}")
    suspend fun updateExerciseRecord(
        @Path("id") id: Int,
        @Body record: ExerciseRecord
    ): Response<ExerciseRecord>

    // 更新健康指标
    @PUT("healthIndicators/{id}")
    suspend fun updateHealthIndicator(
        @Path("id") id: Int,
        @Body indicator: HealthIndicator
    ): Response<HealthIndicator>

    // 更新用药记录
    @PUT("medicationRecords/{id}")
    suspend fun updateMedicationRecord(
        @Path("id") id: Int,
        @Body record: MedicationRecord
    ): Response<MedicationRecord>

    // 更新营养记录
    @PUT("nutritionRecords/{id}")
    suspend fun updateNutritionRecord(
        @Path("id") id: Int,
        @Body record: NutritionRecord
    ): Response<NutritionRecord>

    // 更新身体健康档案
    @PUT("physicalProfiles/{userId}")
    suspend fun updatePhysicalProfile(
        @Path("userId") userId: Int,
        @Body profile: PhysicalProfile
    ): Response<PhysicalProfile>

    // 更新睡眠记录
    @PUT("sleepRecords/{id}")
    suspend fun updateSleepRecord(
        @Path("id") id: Int,
        @Body sleepRecord: SleepRecord
    ): Response<SleepRecord>

    // 更新作者
    @PUT("authors/{id}")
    suspend fun updateAuthor(
        @Path("id") id: Int,
        @Body author: Author
    ): Response<Author>

    // 更新文章标签关系
    @PUT("articleTagRelations/{articleID}")
    suspend fun updateArticleTagRelation(
        @Path("articleID") articleID: Int,
        @Body articleTagRelation: ArticleTagRelation
    ): Response<ArticleTagRelation>

    // 更新文章关系标签
    @PUT("articleTags/{tagID}")
    suspend fun updateArticleTag(
        @Path("tagID") tagID: Int,
        @Body articleTag: ArticleTag
    ): Response<ArticleTag>

    // 更新文章媒体
    @PUT("articleMedia/{mediaID}")
    suspend fun updateArticleMedia(
        @Path("mediaID") mediaID: Int,
        @Body articleMedia: ArticleMedia
    ): Response<ArticleMedia>

    // 更新文章媒体
    @PUT("articles/{id}")
    suspend fun updateArticle(
        @Path("id") id: Int,
        @Body article: Article
    ): Response<Article>

    // 更新文章媒体
    @PUT("displayCards/{id}")
    suspend fun updateDisplayCard(
        @Path("id") id: Int,
        @Body displayCard: DisplayCard
    ): Response<DisplayCard>


}