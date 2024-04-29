package com.example.healthapp.ui.ScreenLevel2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.Navigation.SourceAndVisitScreenDestination
import com.example.healthapp.Navigation.WalkDistanceDetailScreenDestination
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.ui.ScreenLevel3.AddWalkingDistanceDialog
import com.example.healthapp.ui.ToolClass.WalkingDistanceMonthChartView
import com.example.healthapp.ui.ToolClass.WalkingDistanceWeekChartView
import com.example.healthapp.ui.ToolClass.WalkingDistanceYearlyChartView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbstractItemOfWalkDistanceDetailScreen(
    userId: Int,
    userViewModel: UserViewModel,
    physicalProfileViewModel: PhysicalProfileViewModel,
    dailyActivityViewModel: DailyActivityViewModel,
    navController: NavHostController
) {
    // 获取最近7条的数据
    val lastSevenDaysActivities by dailyActivityViewModel.getLastSevenActivities(userId)
        .collectAsState(initial = emptyList())
    var selectedChart by remember { mutableStateOf("Week") }
    // 获取最近一个月的活动数据
    val lastMonthActivities by dailyActivityViewModel.getLastMonthActivities(userId)
        .collectAsState(initial = emptyList())
    // 获取最近一年的活动数据，并计算每月平均步数
    val lastYearActivities by dailyActivityViewModel.getLastYearActivities(userId)
        .collectAsState(initial = emptyList())
    // 获取平均值
    val allActivity by dailyActivityViewModel.allDailyActivities.collectAsState(initial = emptyList())
    val sumValues = allActivity.sumOf { it.walkingDistance ?: 0.0 }
    val averageValues = if (allActivity.isNullOrEmpty()) 0.0 else sumValues / allActivity.size
    val theDate = LocalDateTime.now()
    val date = theDate.format(DateTimeFormatter.ISO_DATE)
    // 控制弹窗键值
    var showAddDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "步行距离")
                    }
                },
                navigationIcon = {
                    Button(
                        onClick = {
                            // 使用最简单的弹出堆栈
                            navController.popBackStack()
                            // 下面是点对点转换界面
                            /*.navigate("AbstractScreen") {
                            // 清除HomePageScreen（包含）之上的堆栈
                            popUpTo("AbstractScreen") {
                                inclusive = true // HomePageScreen 也被清除
                            }
                            launchSingleTop = true    // 避免重复创建HomePageScreen的实例
                            restoreState = true       // 如果可能，恢复之前的状态
                        }*/
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
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIos,
                                contentDescription = stringResource(id = R.string.back_text),
                                tint = Color.Blue
                            )
                            Text(
                                text = "摘要",
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
                    TextButton(
                        onClick = {
                            showAddDialog = true
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = "添加数据",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.Blue
                            )
                        )
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(16.dp)
                    ) {
                        Text(text = "平均")
                        Row(
                            verticalAlignment = Alignment.Bottom // 设置垂直对齐方式为底部对齐
                        ) {
                            val (displayValue, unit) = when {
                                averageValues > 1000 -> Pair(averageValues / 1000, "公里") // 大于1000米时转换为公里
                                averageValues > 30 -> Pair(averageValues, "米") // 30到1000米之间直接显示米
                                else -> Pair(averageValues, "公里") // 小于或等于30米时也显示为公里，可能需要逻辑上的调整
                            }

                            Text(
                                text = String.format("%.2f", displayValue),
                                style = TextStyle(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(text = unit) // 显示单位
                        }
                        Text(
                            text = date,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Card(
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth(),
                        ) {
                            Column(modifier = Modifier.background(color = Color.White)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = Color.LightGray,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    // 每个按钮的样式
                                    val buttonModifier = Modifier
                                        .weight(1f)
                                        .padding(start = 4.dp, end = 4.dp)

                                    // 定义按钮的基本样式
                                    val buttonColors = ButtonDefaults.buttonColors(
                                        containerColor = Color.LightGray,
                                        contentColor = Color.Black
                                    )

                                    // 定义动态样式，反映当前选中的按钮
                                    fun getButtonStyle(chartType: String) =
                                        if (selectedChart == chartType) {
                                            buttonColors.copy(containerColor = Color.White)
                                        } else {
                                            buttonColors
                                        }

                                    // 生成三个按钮
                                    Button(
                                        onClick = { selectedChart = "Week" },
                                        colors = getButtonStyle("Week"),
                                        modifier = buttonModifier,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("周视图")
                                    }

                                    Button(
                                        onClick = { selectedChart = "Month" },
                                        colors = getButtonStyle("Month"),
                                        modifier = buttonModifier,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("月视图")
                                    }

                                    Button(
                                        onClick = { selectedChart = "Year" },
                                        colors = getButtonStyle("Year"),
                                        modifier = buttonModifier,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("年视图")
                                    }
                                }

                                when (selectedChart) {
                                    "Week" -> WalkingDistanceWeekChartView(lastSevenDaysActivities)

                                    "Month" -> WalkingDistanceMonthChartView(lastMonthActivities)

                                    "Year" -> WalkingDistanceYearlyChartView(lastYearActivities)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                AnalyzeWalkingDistance(
                    lastSevenDaysActivities.firstOrNull()?.walkingDistance ?: 0.0
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "关于步行",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "步行距离是指您通过步行活动覆盖的总距离。定期步行有助于增强心肺功能，改善血液循环，降低心脏病和糖尿病等慢性疾病的风险。\n" +
                                    "监测步行距离可以帮助您了解日常活动量，并鼓励您保持或增加活动水平以达到健康目标。\n" +
                                    "现代智能手机和可穿戴设备经常包含计步器或健康应用，可用于追踪和记录您每天的步行距离。",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp // 设置行距为24sp
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "来源：Centers for Disease Control and Prevention (CDC)",
                            style = TextStyle(
                                color = Color.Blue
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "选项",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { }
                ) {
                    Row(
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(16.dp)
                    ) {
                        Text(text = "添加到个人收藏")
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "收藏"
                        )
                    }
                }
                Text(
                    text = "收藏之后，步数将会在“摘要”的“个人收藏”中显示。",
                    style = TextStyle(
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                )
                Spacer(Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier.background(color = Color.White)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(
                                        WalkDistanceDetailScreenDestination.createRoute(userId)
                                    )
                                }
                        ) {
                            Text(
                                text = "显示所有数据",
                                style = TextStyle(
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = "进入",
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                        Divider(
                            color = Color.Gray, // 设置分隔线的颜色
                            thickness = 1.dp, // 设置分隔线的厚度
                            modifier = Modifier.padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(
                                        route = SourceAndVisitScreenDestination.createRoute("步行距离",userId)
                                    )
                                }
                        ) {
                            Text(
                                text = "数据源与访问",
                                style = TextStyle(
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = "进入",
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                    }
                }
            }
        }
        if (showAddDialog) {
            AddWalkingDistanceDialog(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel
            ) {
                showAddDialog = false
            }
        }
    }
}

@Composable
fun AnalyzeWalkingDistance(
    distance: Double
) {
    var output by remember { mutableStateOf("") }
    when {
        distance < 3.0 -> output =
            "分析与评价：\n" +
                    "● 您的日常走路距离较短，可能是由于生活或工作环境限制，或缺少步行的习惯。\n" +
                    "● 尝试增加步行距离可以帮助改善心血管健康，增加日常能量消耗。\n" +
                    "\n建议：\n" +
                    "● 运动建议：开始通过简单的活动如散步增加活动量，尝试每天至少走满30分钟。\n" +
                    "● 饮食建议：保持合理的饮食，避免过多摄入高热量食物，支持活动所需的能量。\n" +
                    "● 生活建议：在日常生活中寻找增加步行的机会，如停车远一点或选择楼梯而非电梯。\n"

        distance in 3.0..7.5 -> output =
            "分析与评价：\n" +
                    "● 您的日常走路距离适中，显示您已经将步行融入日常生活。\n" +
                    "● 继续保持这样的活动水平有助于维持健康体重和良好的体能状态。\n" +
                    "\n建议：\n" +
                    "● 运动建议：尝试将更多步行时间融入到每天的日程，比如用步行替代短途交通工具。\n" +
                    "● 饮食建议：确保膳食平衡，增加蔬菜和水果的摄入，以获取足够的纤维和维生素。\n" +
                    "● 生活建议：考虑使用健康跟踪器或智能手表来监测您的活动量，并设定步行目标。\n"

        distance in 7.5..12.0 -> output =
            "分析与评价：\n" +
                    "● 您的日常走路距离较长，表明您非常活跃，并优先考虑步行作为主要的活动形式。\n" +
                    "● 这样的活动量对维护心血管健康和总体健康都极为有益。\n" +
                    "\n建议：\n" +
                    "● 运动建议：维持或增加步行强度，考虑加入一些小坡道或不同的路线增加挑战。\n" +
                    "● 饮食建议：增加蛋白质和健康脂肪的摄入，支持更高的活动量。\n" +
                    "● 生活建议：保持适当的身体恢复措施，如进行定期的拉伸和肌肉放松活动。\n"

        distance > 12.0 -> output =
            "分析与评价：\n" +
                    "● 您的日常走路距离非常长，可能涉及长时间的步行或徒步旅行。\n" +
                    "● 您的生活方式显示出高度的身体活动，这有助于维持优秀的身体健康和耐力。\n" +
                    "\n建议：\n" +
                    "● 运动建议：保持这一活动量，尝试周期性参与更具挑战性的徒步活动，如山地徒步。\n" +
                    "● 饮食建议：确保高质量的能量来源，包括复杂碳水化合物、足够的水和电解质。\n" +
                    "● 生活建议：投资一双好的行走鞋和合适的装备，确保舒适和效率。\n"
    }
    Text(
        text = "步行距离分析与建议",
        style = MaterialTheme.typography.titleLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = output,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp // 设置行距为24sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "保持活力！", style = TextStyle(
                    color = Color.Blue,
                    lineHeight = 24.sp // 设置行距为24sp
                )
            )
        }
    }
}

