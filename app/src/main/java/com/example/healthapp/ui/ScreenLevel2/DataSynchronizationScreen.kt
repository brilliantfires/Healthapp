package com.example.healthapp.ui.ScreenLevel2

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.ArticleMediaViewModel
import com.example.healthapp.data.viewmodel.ArticleTagRelationViewModel
import com.example.healthapp.data.viewmodel.ArticleTagsViewModel
import com.example.healthapp.data.viewmodel.ArticlesViewModel
import com.example.healthapp.data.viewmodel.AuthorsViewModel
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.DisplayCardViewModel
import com.example.healthapp.data.viewmodel.ExerciseRecordViewModel
import com.example.healthapp.data.viewmodel.HealthIndicatorViewModel
import com.example.healthapp.data.viewmodel.MedicationRecordViewModel
import com.example.healthapp.data.viewmodel.NutritionRecordViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.SleepRecordViewModel
import com.example.healthapp.data.viewmodel.SyncViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataSynchronizationScreen(
    navController: NavHostController,
    articleMediaViewModel: ArticleMediaViewModel,
    articlesViewModel: ArticlesViewModel,
    articleTagsViewModel: ArticleTagsViewModel,
    authorsViewModel: AuthorsViewModel,
    exerciseRecordViewModel: ExerciseRecordViewModel,
    healthIndicationViewModel: HealthIndicatorViewModel,
    medicationRecordViewModel: MedicationRecordViewModel,
    nutritionRecordViewModel: NutritionRecordViewModel,
    physicalProfileViewModel: PhysicalProfileViewModel,
    sleepRecordViewModel: SleepRecordViewModel,
    articleTagRelationViewModel: ArticleTagRelationViewModel,
    userViewModel: UserViewModel,
    dailyActivityViewModel: DailyActivityViewModel,
    displayCardViewModel: DisplayCardViewModel,
    syncViewModel: SyncViewModel
) {
    val userId = 1
    val sleepRecordsSize = sleepRecordViewModel.getAllSleepRecordsByUserId(userId)
        .collectAsState(initial = emptyList()).value.size
    val nutritionRecordsSize = nutritionRecordViewModel.getNutritionRecordsByUserId(userId)
        .collectAsState(initial = emptyList()).value.size
    val medicationCount =
        medicationRecordViewModel.getMedicationRecordsCountByUserId(userId).collectAsState(
            initial = emptyList()
        ).value.size
    val healthIndicatorsSize = healthIndicationViewModel.getHealthMetricByUserId(userId)
        .collectAsState(initial = emptyList()).value.size
    val exerciseRecordsSize = exerciseRecordViewModel.getExerciseRecordByUserId(userId)
        .collectAsState(initial = emptyList()).value.size
    val displayCardsSize =
        displayCardViewModel.allCards.collectAsState(initial = emptyList()).value.size
    val dailyActivitiesSize = dailyActivityViewModel.getActivitiesByUserId(userId)
        .collectAsState(initial = emptyList()).value.size
    val authorsSize = authorsViewModel.allAuthors.collectAsState(initial = emptyList()).value.size
    val articleTagsSize =
        articleTagsViewModel.allTags.collectAsState(initial = emptyList()).value.size
    val articleTagRelationsSize =
        articleTagRelationViewModel.allArticleTagRelations.collectAsState(initial = emptyList()).value.size
    val articlesSize =
        articlesViewModel.allArticles.collectAsState(initial = emptyList()).value.size
    val articleMediaSize =
        articleMediaViewModel.allArticleMedia().collectAsState(initial = emptyList()).value.size
    var totalCount = TotalRecord(
        userId,
        articleMediaViewModel,
        articlesViewModel,
        articleTagsViewModel,
        authorsViewModel,
        exerciseRecordViewModel,
        healthIndicationViewModel,
        medicationRecordViewModel,
        nutritionRecordViewModel,
        physicalProfileViewModel,
        sleepRecordViewModel,
        articleTagRelationViewModel,
        userViewModel,
        dailyActivityViewModel,
        displayCardViewModel
    )

    var currentCountState = totalCount
    val preCount = remember {
        sleepRecordsSize + nutritionRecordsSize + medicationCount +
                healthIndicatorsSize + exerciseRecordsSize + displayCardsSize +
                dailyActivitiesSize + authorsSize + articleTagsSize +
                articleTagRelationsSize + articlesSize + articleMediaSize + 2
    }
    // 当 totalCountState 发生变化时，更新 currentCountState
    LaunchedEffect(totalCount) {
        currentCountState = totalCount
    }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()  // 创建或获取协程作用域

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(id = R.string.health_detail_info_text))
                    }
                },
                navigationIcon = {
                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface, // 设置按钮背景颜色与背景一致
                            contentColor = MaterialTheme.colorScheme.onSurface // 设置按钮内容颜色以确保可见性
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .width(80.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIos,
                                contentDescription = stringResource(id = R.string.back_text),
                                tint = Color.Blue
                            )
                            Text(
                                text = "设置",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.Blue
                                )
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                        },
                        modifier = Modifier.width(80.dp)
                    ) {

                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                // 头像居中显示
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.ab5_hiit),
                        contentDescription = stringResource(id = R.string.avatar_text),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            //定义120dp的圆形，clip
                            .size(140.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(Modifier.height(32.dp))

                Card(
                    modifier = Modifier.animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(color = Color.White)
                            .clickable {
                                expanded = !expanded
                            }
                    ) {
                        Text(
                            text = "数据同步",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(16.dp)

                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (expanded) {
                            Button(
                                onClick = {
                                    expanded = !expanded

                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.LightGray,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.padding(end = 16.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "收起",
                                    fontSize = 16.sp,
                                    color = Color.Blue,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        } else {
                            Button(
                                onClick = {
                                    expanded = !expanded

                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.padding(end = 16.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "展开",
                                    fontSize = 16.sp,
                                    color = Color.Blue,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                    // 数据同步项
                    if (expanded) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color.LightGray)
                                .clip(RoundedCornerShape(8.dp))
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "全部同步",
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = {
                                    syncViewModel.pullAllData()
                                    coroutineScope.launch {
                                        delay(6000) // 延迟两秒
                                        showToast(context, "数据同步成功")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.padding(end = 0.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "更新",
                                    color = Color.Blue
                                )
                            }
                        }
                        SyncItem("用户数据", userViewModel::fetchUserAndStore, userId)
                        SyncItem(
                            "睡眠记录",
                            sleepRecordViewModel::getSleepRecordsByUserIdAndStore,
                            userId
                        )
                        SyncItem(
                            "体检记录",
                            physicalProfileViewModel::getPhysicalProfileByUserIdAndStore,
                            userId
                        )
                        SyncItem(
                            "营养记录",
                            nutritionRecordViewModel::getNutritionRecordsByUserIdAndStore,
                            userId
                        )
                        SyncItem(
                            "健康指标",
                            healthIndicationViewModel::getHealthMetricsByUserIdAndStore,
                            userId
                        )
                        SyncItem(
                            "运动记录",
                            exerciseRecordViewModel::getExerciseRecordsByUserIdAndStore,
                            userId
                        )
                        SyncItem(
                            "用药记录",
                            medicationRecordViewModel::getMedicationRecordByUserIdAndStore,
                            userId
                        )
                        SyncItem(
                            "日常活动",
                            dailyActivityViewModel::getDailyActivitiesByUserIdAndStore,
                            userId
                        )
                        SyncItem1("作者信息", authorsViewModel::getAllAuthorsAndStore)
                        SyncItem1("分类信息", articleTagsViewModel::getAllArticleTagsAndStore)
                        SyncItem1("文章信息", articlesViewModel::getAllArticlesAndStore)
                        SyncItem1("媒体信息", articleMediaViewModel::getAllArticleMediaAndStore)
                        SyncItem1(
                            "文章标签关系",
                            articleTagRelationViewModel::getAllArticleTagRelationsAndStore
                        )
                    }
                    if (expanded == false) {
                        Divider(
                            color = Color.Gray, // 设置分隔线的颜色
                            thickness = 1.dp, // 设置分隔线的厚度
                            modifier = Modifier
                                .background(color = Color.White)
                                .padding(horizontal = 16.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(color = Color.White)
                    ) {
                        Text(
                            text = "数据备份",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    syncViewModel.syncAllData()
                                    delay(2000) // 延迟两秒
                                    showToast(context, "数据备份成功")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(end = 16.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Filled.Sync,
                                contentDescription = "同步"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "当前账户数据记录条数",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = currentCountState.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                Card() {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .background(
                                    color = Color.White
                                )
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "数据概览",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 16.dp,
                                    bottom = 0.dp
                                )
                            )
                        }
                        Divider(
                            color = Color.Black, // 设置分隔线的颜色
                            thickness = 2.dp, // 设置分隔线的厚度
                            modifier = Modifier
                                .background(color = Color.White)
                                .padding(start = 16.dp, end = 16.dp, bottom = 0.dp, top = 0.dp)
                        )
                        DataItem("睡眠记录数", sleepRecordsSize)

                        DataItem("营养记录数", nutritionRecordsSize)
                        DataItem("药物记录数", medicationCount)
                        DataItem("健康指标数", healthIndicatorsSize)
                        DataItem("运动记录数", exerciseRecordsSize)
                        DataItem("展示卡片数", displayCardsSize)
                        DataItem("日常活动数", dailyActivitiesSize)
                        DataItem("作者数", authorsSize)
                        DataItem("文章标签数", articleTagsSize)
                        DataItem("文章标签关系数", articleTagRelationsSize)
                        DataItem("文章数", articlesSize)
                        DataItem("文章媒体数", articleMediaSize)
                    }
                }
            }
        }
    }
}

@Composable
fun TotalRecord(
    userId: Int,
    articleMediaViewModel: ArticleMediaViewModel,
    articlesViewModel: ArticlesViewModel,
    articleTagsViewModel: ArticleTagsViewModel,
    authorsViewModel: AuthorsViewModel,
    exerciseRecordViewModel: ExerciseRecordViewModel,
    healthIndicatorViewModel: HealthIndicatorViewModel,
    medicationRecordViewModel: MedicationRecordViewModel,
    nutritionRecordViewModel: NutritionRecordViewModel,
    physicalProfileViewModel: PhysicalProfileViewModel,
    sleepRecordViewModel: SleepRecordViewModel,
    articleTagRelationViewModel: ArticleTagRelationViewModel,
    userViewModel: UserViewModel,
    dailyActivityViewModel: DailyActivityViewModel,
    displayCardViewModel: DisplayCardViewModel
): Int {
    val sleepRecordsSize = sleepRecordViewModel.getAllSleepRecordsByUserId(userId)
        .collectAsState(initial = emptyList()).value.size
    val nutritionRecordsSize = nutritionRecordViewModel.getNutritionRecordsByUserId(userId)
        .collectAsState(initial = emptyList()).value.size
    val medicationCount =
        medicationRecordViewModel.getMedicationRecordsCountByUserId(userId).collectAsState(
            initial = emptyList()
        ).value.size
    val healthIndicatorsSize = healthIndicatorViewModel.getHealthMetricByUserId(userId)
        .collectAsState(initial = emptyList()).value.size
    val exerciseRecordsSize = exerciseRecordViewModel.getExerciseRecordByUserId(userId)
        .collectAsState(initial = emptyList()).value.size
    val displayCardsSize =
        displayCardViewModel.allCards.collectAsState(initial = emptyList()).value.size
    val dailyActivitiesSize = dailyActivityViewModel.getActivitiesByUserId(userId)
        .collectAsState(initial = emptyList()).value.size
    val authorsSize = authorsViewModel.allAuthors.collectAsState(initial = emptyList()).value.size
    val articleTagsSize =
        articleTagsViewModel.allTags.collectAsState(initial = emptyList()).value.size
    val articleTagRelationsSize =
        articleTagRelationViewModel.allArticleTagRelations.collectAsState(initial = emptyList()).value.size
    val articlesSize =
        articlesViewModel.allArticles.collectAsState(initial = emptyList()).value.size
    val articleMediaSize =
        articleMediaViewModel.allArticleMedia().collectAsState(initial = emptyList()).value.size

    // 使用 derivedStateOf 重新计算总数
    val totalRecords = remember(
        sleepRecordsSize, nutritionRecordsSize, medicationCount, healthIndicatorsSize,
        exerciseRecordsSize, displayCardsSize, dailyActivitiesSize, authorsSize,
        articleTagsSize, articleTagRelationsSize, articlesSize, articleMediaSize
    ) {
        sleepRecordsSize + nutritionRecordsSize + medicationCount +
                healthIndicatorsSize + exerciseRecordsSize + displayCardsSize +
                dailyActivitiesSize + authorsSize + articleTagsSize +
                articleTagRelationsSize + articlesSize + articleMediaSize
    }

    // 返回计算的总记录数
    return totalRecords + 2
}


@Composable
fun DataItem(label: String, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
        )
        Text(
            text = count.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
        )
    }
    if (label != "文章媒体数") {
        Divider(
            color = Color.Gray, // 设置分隔线的颜色
            thickness = 1.dp, // 设置分隔线的厚度
            modifier = Modifier
                .background(color = Color.White)
                .padding(horizontal = 16.dp)
        )
    }
}


@Composable
fun SyncItem(title: String, action: (Int) -> Unit, userId: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.LightGray)
            .clip(RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = { action(userId) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(end = 0.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("更新")
        }
    }
}

@Composable
fun SyncItem1(title: String, action: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.LightGray)
            .clip(RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = { action() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(end = 0.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("更新")
        }
    }
}

fun showToast(context: android.content.Context, message: String) {
    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
}