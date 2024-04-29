package com.example.healthapp.ui.ScreenLevel1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.Navigation.AbstractItemOfEnergyDetailScreenDestination
import com.example.healthapp.Navigation.AbstractItemOfExerciseDurationDetailScreenDestination
import com.example.healthapp.Navigation.AbstractItemOfFloorsClimbedDetailScreenDestination
import com.example.healthapp.Navigation.AbstractItemOfRunningDistanceDetailScreenDestination
import com.example.healthapp.Navigation.AbstractItemOfSleepRecordDetailScreenDestination
import com.example.healthapp.Navigation.AbstractItemOfStepDetailScreenDestination
import com.example.healthapp.Navigation.AbstractItemOfWalkDistanceDetailScreenDestination
import com.example.healthapp.Navigation.AllAbstractScreenDestination
import com.example.healthapp.Navigation.BottomNavigationBar
import com.example.healthapp.Navigation.HealthDetailsScreenDestination
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.DisplayCardViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.SleepRecordViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.ui.ToolClass.SmallBarChartCompose
import com.example.healthapp.ui.ToolClass.formatTime
import com.example.healthapp.ui.ToolClass.timeToDouble
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbstractScreen(
    userId: Int,
    userViewModel: UserViewModel,
    physicalProfileViewModel: PhysicalProfileViewModel,
    dailyActivityViewModel: DailyActivityViewModel,
    displayCardViewModel: DisplayCardViewModel,
    sleepRecordViewModel: SleepRecordViewModel,
    navController: NavHostController
) {
    // 初始化需要的数据
    val user by userViewModel.getUserById(userId).collectAsState(initial = null)
    // 初始化需要的数据
    val physicalProfile by physicalProfileViewModel.getPhysicalProfileById(userId)
        .collectAsState(initial = null)
    // 初始化需要的数据
    val dailyActivity by dailyActivityViewModel.getDailyActivityById(userId)
        .collectAsState(initial = null)
    // 控制是否展示弹出的选择框
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.abstract_text),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    Button(
                        onClick = {
                            showDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface, // 设置按钮背景颜色与背景一致
                            contentColor = MaterialTheme.colorScheme.onSurface // 设置按钮内容颜色以确保可见性
                        ),
                        modifier = Modifier.padding(start = 8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp),
                        ) {
                            Text(
                                text = "编辑",
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
                    Box(
                        modifier = Modifier
                            .width(80.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ab5_hiit),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable(
                                    onClick = {
                                        navController.navigate(
                                            HealthDetailsScreenDestination.createRoute(
                                                userId
                                            )
                                        ) {
                                            // 清除HomePageScreen（包含）之上的堆栈
                                            popUpTo(
                                                HealthDetailsScreenDestination.createRoute(
                                                    userId
                                                )
                                            ) {
                                                inclusive = true // HomePageScreen 也被清除
                                            }
                                            launchSingleTop = true    // 避免重复创建HomePageScreen的实例
                                            restoreState = true       // 如果可能，恢复之前的状态
                                        }
                                    },
                                    indication = rememberRipple(
                                        bounded = true,
                                        color = Color.Gray
                                    ), // 这里正确应用rememberRipple
                                    interactionSource = remember { MutableInteractionSource() } // 为点击提供一个交互源
                                ),
                            contentDescription = stringResource(id = R.string.avatar_text)
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, userId)
        }
    ) { innerPadding ->
        AbstractBodyContent(
            userId,
            Modifier.padding(innerPadding),
            navController,
            dailyActivityViewModel,
            sleepRecordViewModel,
            displayCardViewModel
        )
    }
    if (showDialog) {
        // 弹出窗口逻辑，用于选择需要显示的卡片视图
        ChooseDisplayCardsDialog(displayCardViewModel, onDismiss = { showDialog = false })
    }
}

@Composable
fun ChooseDisplayCardsDialog(
    displayCardViewModel: DisplayCardViewModel, onDismiss: () -> Unit
) {
    val displayCards by displayCardViewModel.allCards.collectAsState(initial = emptyList())

    // 定义一个map来映射cardName到对应的显示词
    val cardNameToDisplayText = mapOf(
        "AbstractStepCard" to "步数",
        "AbstractFloorClimbedCard" to "爬楼梯层数",
        "AbstractWalkingDistanceCard" to "步行距离",
        "AbstractExerciseDurationCard" to "跑步时间",
        "AbstractRunningDistanceCard" to "跑步距离",
        "AbstractEnergyExpenditureCard" to "能量消耗",
        "AbstractSleepRecordCard" to "睡眠时间"
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp)
            ) {
                Text(
                    text = "选择展示的卡片",
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }, text = {
            // 使用LazyColumn显示卡片和复选框
            LazyColumn {
                items(displayCards) { card ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = card.isDisplayed,
                            onCheckedChange = { isChecked ->
                                // 更新卡片显示状态
                                displayCardViewModel.updateCardDisplayStatus(card.id, isChecked)
                            }
                        )
                        val displayedText = cardNameToDisplayText[card.cardName] ?: card.cardName
                        Text(
                            text = displayedText,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }, confirmButton = {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .width(120.dp)
                ) {
                    Text(
                        text = "保存设置", fontSize = 16.sp
                    )
                }
            }

        }
    )
}


@Composable
fun AbstractBodyContent(
    userId: Int,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    dailyActivityViewModel: DailyActivityViewModel,
    sleepRecordViewModel: SleepRecordViewModel,
    displayCardViewModel: DisplayCardViewModel,
) {
    val displayedCards by displayCardViewModel.displayedCards.observeAsState(initial = emptyList())
    val allCards by displayCardViewModel.allCards.collectAsState(initial = emptyList())

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(displayedCards) { card ->
            when (card.cardName) {
                "AbstractStepCard" -> AbstractStepCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    AbstractItemOfStepDetailScreenDestination.createRoute(userId)
                )

                "AbstractFloorClimbedCard" -> AbstractFloorClimbedCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    AbstractItemOfFloorsClimbedDetailScreenDestination.createRoute(userId)
                )

                "AbstractWalkingDistanceCard" -> AbstractWalkingDistanceCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    AbstractItemOfWalkDistanceDetailScreenDestination.createRoute(userId)
                )

                "AbstractExerciseDurationCard" -> AbstractExerciseDurationCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    AbstractItemOfExerciseDurationDetailScreenDestination.createRoute(userId)
                )

                "AbstractRunningDistanceCard" -> AbstractRunningDistanceCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    AbstractItemOfRunningDistanceDetailScreenDestination.createRoute(userId)
                )

                "AbstractEnergyExpenditureCard" -> AbstractEnergyExpenditureCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    AbstractItemOfEnergyDetailScreenDestination.createRoute(userId)
                )

                "AbstractSleepRecordCard" -> AbstractSleepRecordeCard(
                    userId,
                    sleepRecordViewModel,
                    navController,
                    AbstractItemOfSleepRecordDetailScreenDestination.createRoute(userId)
                )

                else -> {} // 对于不认识的cardName，不展示任何内容
            }
        }

        // 根据需要添加更多卡片
        item {
            Card(
                modifier = Modifier
                    .clickable {
                        navController.navigate(
                            route = AllAbstractScreenDestination.createRoute(
                                userId
                            )
                        ) {
                            // 清除HomePageScreen（包含）之上的堆栈
                            popUpTo(
                                route = AllAbstractScreenDestination.createRoute(
                                    userId
                                )
                            ) {
                                inclusive = true // HomePageScreen 也被清除
                            }
                            launchSingleTop = true    // 避免重复创建HomePageScreen的实例
                            restoreState = true       // 如果可能，恢复之前的状态
                        }
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Stars,
                        contentDescription = "应用图标",
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                    )
                    Text(
                        text = "显示所有健康数据",
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "进入",
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp)
                    )
                }
            }

        }
    }

}

// 最近一天的步数卡片
@Composable
fun AbstractStepCard(
    userId: Int, viewModel: DailyActivityViewModel, navController: NavHostController, route: String
) {
    // 使用viewModel获取数据流并转换为可观察的状态
    val latestActivity by viewModel.getLatestActivityByUserId(userId).collectAsState(initial = null)

    val lastSevenDaysActivities by viewModel.getLastSevenActivities(userId)
        .collectAsState(initial = emptyList())
    val stepList: List<Int> = lastSevenDaysActivities.map { it.steps ?: 0 }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(route = route) {
                    // 清除HomePageScreen（包含）之上的堆栈
                    popUpTo(route = route) {
                        inclusive = true // HomePageScreen 也被清除
                    }
                    launchSingleTop = true    // 避免重复创建HomePageScreen的实例
                    restoreState = true       // 如果可能，恢复之前的状态
                }
            }

    ) {
        Column(
            modifier = Modifier.background(color = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.DirectionsRun, contentDescription = "步数"
                )
                Text(
                    text = "步数",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "日期: ${latestActivity?.date?.format(DateTimeFormatter.ISO_DATE) ?: ""}"
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "进入"
                )
            }
            Row(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${latestActivity?.steps ?: "加载中..."}" + "步",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                // 随机排序颜色列表
                val randomColors = remember { mutableStateOf(colors.shuffled()) }
                SmallBarChartCompose(stepList, randomColors.value)
            }
        }
    }
}


// 最近一天的爬楼梯层数卡片
@Composable
fun AbstractFloorClimbedCard(
    userId: Int, viewModel: DailyActivityViewModel, navController: NavHostController, route: String
) {
    // 假设viewModel中已经有了获取最新一天爬楼梯信息的方法
    val latestActivity by viewModel.getLatestActivityByUserId(userId).collectAsState(initial = null)
    val date by remember {
        mutableStateOf(latestActivity?.date ?: LocalDateTime.now())
    }
    val lastSevenDaysActivities by viewModel.getLastSevenActivities(userId)
        .collectAsState(initial = emptyList())
    val floorsClimbedList: List<Int> = lastSevenDaysActivities.map { it.floorsClimbed ?: 0 }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(route = route) {
                    // 配置导航选项
                    popUpTo(route = route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
    ) {
        Column(modifier = Modifier.background(color = Color.White)) {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Stairs, contentDescription = "爬楼梯"
                )
                Text(
                    text = "爬楼层数",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "日期: ${latestActivity?.date?.format(DateTimeFormatter.ISO_DATE) ?: ""}"
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "进入"
                )
            }
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${latestActivity?.floorsClimbed ?: "0"}" + "层",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                // 随机排序颜色列表
                val randomColors = remember { mutableStateOf(colors.shuffled()) }
                SmallBarChartCompose(floorsClimbedList, randomColors.value)
            }
        }
    }
}

// 这个卡片展示最新一天的走路距离
@Composable
fun AbstractWalkingDistanceCard(
    userId: Int, viewModel: DailyActivityViewModel, navController: NavHostController, route: String
) {

    val latestActivity by viewModel.getLatestActivityByUserId(userId).collectAsState(initial = null)
    val lastSevenDaysActivities by viewModel.getLastSevenActivities(userId)
        .collectAsState(initial = emptyList())
    val walkingDistanceList: List<Double> =
        lastSevenDaysActivities.map { it.walkingDistance ?: 0.0 }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(route = route) {
                    popUpTo(route = route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
    ) {
        Column(modifier = Modifier.background(color = Color.White)) {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.DirectionsWalk, contentDescription = "走路距离"
                )
                Text(
                    text = "步行距离",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "日期: ${latestActivity?.date?.format(DateTimeFormatter.ISO_DATE) ?: ""}"
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "查看详情"
                )
            }
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildString {
                        val distance = latestActivity?.walkingDistance
                        when {
                            distance == null -> append("加载中...")
                            distance > 1000.0 -> append(
                                String.format(
                                    "%.2f",
                                    distance / 1000
                                ) + " 公里"
                            )

                            distance in 30.0..1000.0 -> append("${distance}米")
                            else -> append("${distance} 公里")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                // 随机排序颜色列表
                val randomColors = remember { mutableStateOf(colors.shuffled()) }
                SmallBarChartCompose(walkingDistanceList, randomColors.value)
            }
        }
    }
}

// 这个卡片展示最新一天的跑步时间
@Composable
fun AbstractExerciseDurationCard(
    userId: Int, viewModel: DailyActivityViewModel, navController: NavHostController, route: String
) {
    val latestActivity by viewModel.getLatestActivityByUserId(userId).collectAsState(initial = null)
    val lastSevenDaysActivities by viewModel.getLastSevenActivities(userId)
        .collectAsState(initial = emptyList())
    val exerciseDurationList: List<String> =
        lastSevenDaysActivities.map { it.exerciseDuration ?: "00:00:00" }
    val doubleExerciseDurationList = exerciseDurationList.map { timeStr -> timeToDouble(timeStr) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(route = route) {
                    popUpTo(route = route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
    ) {
        Column(modifier = Modifier.background(color = Color.White)) {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.DirectionsRun, contentDescription = "跑步时间"
                )
                Text(
                    text = "跑步时间",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "日期: ${latestActivity?.date?.format(DateTimeFormatter.ISO_DATE) ?: ""}"
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "查看详情"
                )
            }
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val formattedTime = formatTime(latestActivity?.exerciseDuration ?: "00:00:00")
                val hasFormatTime =
                    if (formattedTime == "00小时00分钟00秒") "该日期未跑步" else formattedTime
                Text(
                    text = hasFormatTime,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                // 随机排序颜色列表
                val randomColors = remember { mutableStateOf(colors.shuffled()) }
                SmallBarChartCompose(doubleExerciseDurationList, randomColors.value)
            }
        }
    }
}

// 最新一天的跑步距离卡片
@Composable
fun AbstractRunningDistanceCard(
    userId: Int, viewModel: DailyActivityViewModel, navController: NavHostController, route: String
) {
    // 假设viewModel.getLatestRunningDistanceByUserId(userId)是一个已实现的方法
    val latestActivity by viewModel.getLatestActivityByUserId(userId).collectAsState(initial = null)
    val lastSevenDaysActivities by viewModel.getLastSevenActivities(userId)
        .collectAsState(initial = emptyList())
    val runningDistanceList: List<Double> =
        lastSevenDaysActivities.map { it.runningDistance ?: 0.0 }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(route = route) {
                    // 配置导航选项
                    popUpTo(route = route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
    ) {
        Column(modifier = Modifier.background(color = Color.White)) {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.DirectionsRun, contentDescription = "跑步距离"
                )
                Text(
                    text = "跑步距离",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "日期: ${latestActivity?.date?.format(DateTimeFormatter.ISO_DATE) ?: ""}"
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "查看详情"
                )
            }
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildString {
                        val distance = latestActivity?.runningDistance
                        when {
                            distance == null -> append("加载中...")
                            distance > 1000.0 -> append(
                                String.format(
                                    "%.2f",
                                    distance / 1000
                                ) + "公里"
                            )

                            distance in 30.0..1000.0 -> append("${distance}米")
                            else -> append("${distance} 公里")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                // 随机排序颜色列表
                val randomColors = remember { mutableStateOf(colors.shuffled()) }
                SmallBarChartCompose(runningDistanceList, randomColors.value)
            }
        }
    }
}

// 最新一天的能量消耗卡片
@Composable
fun AbstractEnergyExpenditureCard(
    userId: Int, viewModel: DailyActivityViewModel, navController: NavHostController, route: String
) {
    val latestActivity by viewModel.getLatestActivityByUserId(userId).collectAsState(initial = null)
    val lastSevenDaysActivities by viewModel.getLastSevenActivities(userId)
        .collectAsState(initial = emptyList())
    val energyExpenditureList: List<Double> =
        lastSevenDaysActivities.map { it.energyExpenditure ?: 0.0 }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(route = route) {
                    // 配置导航选项
                    popUpTo(route = route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
    ) {
        Column(modifier = Modifier.background(color = Color.White)) {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.FitnessCenter, contentDescription = "能量消耗"
                )
                Text(
                    text = "能量消耗",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "日期: ${latestActivity?.date?.format(DateTimeFormatter.ISO_DATE) ?: ""}"
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "查看详情"
                )
            }
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = " ${
                    latestActivity?.energyExpenditure?.let {
                        String.format(
                            "%.2f", it
                        )
                    } ?: "加载中..."
                }千卡",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f))


                // 随机排序颜色列表
                val randomColors = remember { mutableStateOf(colors.shuffled()) }
                SmallBarChartCompose(energyExpenditureList, randomColors.value)
            }
        }
    }
}

// 最新一天的睡眠时间卡片
@Composable
fun AbstractSleepRecordeCard(
    userId: Int, viewModel: SleepRecordViewModel, navController: NavHostController, route: String
) {
    val sleepRecord by viewModel.getLatestSleepRecordByUserId(userId).collectAsState(initial = null)
    val latestSevenSleepRecords by viewModel.getLatestSevenSleepRecord(userId).collectAsState(
        initial = emptyList()
    )
    val newSevenSleepRecords = latestSevenSleepRecords.map { it.totalDuration ?: "00:00:00" }
    val doubleLatestSevenSleepRecords =
        newSevenSleepRecords.map { timeStr -> timeToDouble(timeStr) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(route = route) {
                    // 配置导航选项
                    popUpTo(route = route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
    ) {
        Column(modifier = Modifier.background(color = Color.White)) {
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Bed, contentDescription = "睡眠时间"
                )
                Text(
                    text = "睡眠时间",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "日期: ${sleepRecord?.date?.format(DateTimeFormatter.ISO_DATE) ?: ""}"
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "查看详情"
                )
            }
            Row(
                modifier = Modifier.padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val formattedTime = formatTime(sleepRecord?.totalDuration ?: "00:00:00")
                val hasFormatTime =
                    if (formattedTime == "00小时00分钟00秒") "该日期暂无睡眠数据" else formattedTime
                Text(
                    text = hasFormatTime,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                // 随机排序颜色列表
                val randomColors = remember { mutableStateOf(colors.shuffled()) }
                SmallBarChartCompose(doubleLatestSevenSleepRecords, randomColors.value)
            }
        }
    }
}

val colors = listOf(
    Color.Red, Color.Green, Color.Blue, Color.Magenta,
    Color.Yellow, Color.Cyan, Color.Gray
)