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
import com.example.healthapp.Navigation.ExerciseDurationDetailsScreenDestination
import com.example.healthapp.Navigation.SourceAndVisitScreenDestination
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.ui.ScreenLevel3.AddExerciseDurationDialog
import com.example.healthapp.ui.ToolClass.ExerciseDurationMonthChartView
import com.example.healthapp.ui.ToolClass.ExerciseDurationWeekChartView
import com.example.healthapp.ui.ToolClass.ExerciseDurationYearlyChartView
import com.example.healthapp.ui.ToolClass.convertTimeToHours
import com.example.healthapp.ui.ToolClass.timeToDouble
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbstractItemOfExerciseDurationDetailScreen(
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
    val sumDuration = allActivity.sumOf { timeToDouble(it.exerciseDuration ?: "0:0:0") }
    val averageDuration = if (allActivity.isNullOrEmpty()) 0.0 else sumDuration / allActivity.size
    val theDate = LocalDateTime.now()
    val date = theDate.format(DateTimeFormatter.ISO_DATE)
    // 控制弹窗的键值
    var showAddDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "跑步时间")
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
                    modifier = Modifier.fillMaxWidth()
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
                            Text(
                                text = String.format("%.2f", averageDuration),
                                style = TextStyle(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(text = "小时")
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
                                    "Week" -> ExerciseDurationWeekChartView(lastSevenDaysActivities)

                                    "Month" -> ExerciseDurationMonthChartView(lastMonthActivities)

                                    "Year" -> ExerciseDurationYearlyChartView(lastYearActivities)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                AnalyzeRunningTime(
                    convertTimeToHours(
                        lastSevenDaysActivities.firstOrNull()?.exerciseDuration ?: "0:0:0"
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "关于跑步运动",
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
                            text = "跑步是一种高效的有氧运动，可以显著提高心血管健康、增强肌肉力量，并促进情绪稳定。" +
                                    "它对于燃烧卡路里、减轻体重以及增加耐力和速度都非常有效。" +
                                    "开始跑步前，选择合适的跑鞋和舒适的装备是非常重要的，这有助于减少受伤风险。" +
                                    "建议初学者从短距离跑步开始，逐渐增加距离和强度，并注意身体的适应反应。" +
                                    "保持适当的水分和营养补充也是跑步时不可忽视的关键因素。",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp  // 设置行距为24sp
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "来源：American Heart Association",
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
                    Column(
                        modifier = Modifier.background(color = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(
                                        ExerciseDurationDetailsScreenDestination.createRoute(userId)
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
                                        route = SourceAndVisitScreenDestination.createRoute("跑步时间",userId)
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
            AddExerciseDurationDialog(userId, dailyActivityViewModel) { showAddDialog = false }
        }
    }
}

@Composable
fun AnalyzeRunningTime(
    runningTime: Double
) {
    var output by remember { mutableStateOf("") }
    when {
        runningTime <= 0.5 -> output =
            "分析与评价：\n" +
                    "● 您的跑步时间较短，表明可能是初学者或是在尝试融入跑步到您的日常生活中。\n" +
                    "● 短时间的跑步也是一种很好的开始，有助于逐步建立体能。\n" +
                    "\n建议：\n" +
                    "● 运动建议：尝试逐渐延长跑步时间，目标是每次至少跑30分钟。\n" +
                    "● 饮食建议：跑步前后补充轻质的碳水化合物和蛋白质，如香蕉和酸奶，以提供能量和促进恢复。\n" +
                    "● 生活建议：确保穿着适合的跑鞋，避免跑步伤害。\n"

        runningTime in 0.5..1.0 -> output =
            "分析与评价：\n" +
                    "● 您的跑步时间适中，适合保持基本的身体健康。\n" +
                    "● 持续这样的跑步习惯有助于提高心肺功能和整体体能。\n" +
                    "\n建议：\n" +
                    "● 运动建议：保持此跑步习惯，可尝试间歇跑步来提高速度和耐力。\n" +
                    "● 饮食建议：确保摄入足够的流体，特别是在跑步前后，以及增加富含电解质的食物，如椰子水。\n" +
                    "● 生活建议：定期更换跑鞋，每500公里左右更换一次，以保护关节和肌肉。"

        runningTime in 1.0..2.0 -> output =
            "分析与评价：\n" +
                    "● 您的跑步时间显示出高于平均水平的体能和承诺。\n" +
                    "● 长时间的跑步可以极大地提高心血管健康，并增强心理耐力。\n" +
                    "\n建议：\n" +
                    "● 运动建议：考虑参加马拉松等更长距离的赛事，为自己设定新的挑战。\n" +
                    "● 饮食建议：在长时间跑步期间，确保补充能量胶或含糖运动饮料，以维持血糖水平和耐力。\n" +
                    "● 生活建议：适当增加休息日，确保身体得到充分的恢复。"

        runningTime > 2.0 -> output =
            "分析与评价：\n" +
                    "● 您的跑步时间非常长，表明您可能是经验丰富的长距离跑者。\n" +
                    "● 维持这种高水平的体能要求科学的训练和良好的身体管理。\n" +
                    "\n建议：\n" +
                    "● 运动建议：保持训练多样性，加入速度训练和坡道训练以提高效率和强度。\n" +
                    "● 饮食建议：关注营养的均衡，特别是蛋白质的摄入，以及在长跑过程中及时补充碳水化合物。\n" +
                    "● 生活建议：关注身体的任何疲劳迹象或潜在伤害，与专业人士合作进行身体维护和调整训练计划。"
    }
    Text(
        text = "跑步时间分析与建议",
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
                    lineHeight = 24.sp  // 设置行距为24sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "继续努力！", style = TextStyle(
                    color = Color.Blue,
                    lineHeight = 24.sp  // 设置行距为24sp
                )
            )
        }
    }
}
