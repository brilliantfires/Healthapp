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
import com.example.healthapp.Navigation.StepDetailsScreenDestination
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.ui.ScreenLevel3.AddStepsDialog
import com.example.healthapp.ui.ToolClass.StepMonthChartView
import com.example.healthapp.ui.ToolClass.StepWeekChartView
import com.example.healthapp.ui.ToolClass.StepYearlyChartView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbstractItemOfStepDetailScreen(
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
    val sumValues = allActivity.sumOf { it.steps ?: 0 }
    val averageValues = if (allActivity.isNullOrEmpty()) 0 else sumValues / allActivity.size
    val theDate = LocalDateTime.now()
    val date = theDate.format(DateTimeFormatter.ISO_DATE)
    // 控制弹窗键值
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "步数", style = MaterialTheme.typography.titleLarge
                    )
                }
            }, navigationIcon = {
                Button(
                    onClick = {
                        // 使用最简单的弹出堆栈
                        navController.popBackStack()
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
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIos,
                            contentDescription = stringResource(id = R.string.back_text),
                            tint = Color.Blue
                        )
                        Text(
                            text = "摘要", style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.Blue
                            )
                        )
                    }
                }
            }, actions = {
                TextButton(
                    onClick = {
                        showAddDialog = true
                    }, modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "添加数据", style = TextStyle(
                            fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Blue
                        )
                    )
                }
            })
        }) { innerPadding ->
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
                                text = averageValues.toString(),
                                style = TextStyle(
                                    fontSize = 32.sp, fontWeight = FontWeight.Bold
                                )
                            )
                            Text(text = "步")
                        }
                        Text(
                            text = date, style = TextStyle(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Card(
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth(),
                        ) {
                            Column(modifier = Modifier.background(color = Color.White))
                            {
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
                                    "Week" -> StepWeekChartView(lastSevenDaysActivities)

                                    "Month" -> StepMonthChartView(lastMonthActivities)

                                    "Year" -> StepYearlyChartView(lastYearActivities)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                AnalyzeSteps(
                    steps = lastSevenDaysActivities.firstOrNull()?.steps ?: 0
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "关于步数", style = MaterialTheme.typography.titleLarge
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
                            text = "步数是指你一天中走了多少步。" + "计步器和数字运动追踪器可帮助你确定步数。" + "这类设备会为所有产生步数的活动计算步数，" + "如走路、跑步、上下楼梯、越野滑雪，甚至做家务活等活动。",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp  // 设置行距为24sp
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Mayo Clinic", style = TextStyle(
                                color = Color.Blue
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "选项", style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier
                    .fillMaxSize()
                    .clickable { }) {
                    Row(
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(16.dp)
                    ) {
                        Text(text = "添加到个人收藏")
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Filled.Star, contentDescription = "收藏"
                        )
                    }
                }
                Text(
                    text = "收藏之后，步数将会在“摘要”的“个人收藏”中显示。", style = TextStyle(
                        color = Color.Gray
                    ), modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                )
                Spacer(Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .background(color = Color.White)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(
                                        StepDetailsScreenDestination.createRoute(
                                            userId
                                        )
                                    )
                                }
                        ) {
                            Text(
                                text = "显示所有数据", style = TextStyle(
                                    fontSize = 16.sp
                                ), modifier = Modifier.padding(16.dp)
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
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(
                                        route = SourceAndVisitScreenDestination.createRoute("步数",userId)
                                    )
                                }
                        ) {
                            Text(
                                text = "数据源与访问", style = TextStyle(
                                    fontSize = 16.sp
                                ), modifier = Modifier.padding(16.dp)
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
            AddStepsDialog(userId = userId, dailyActivityViewModel = dailyActivityViewModel) {
                showAddDialog = false
            }
        }
    }
}

@Composable
fun AnalyzeSteps(
    steps: Int
) {
    var output by remember { mutableStateOf("") }
    when {
        steps < 3000 -> output =
            "分析与评价：\n" +
                    "● 这表明用户的日常活动非常有限，可能大部分时间都在静坐。\n" +
                    "● 可能的原因包括长时间的办公室工作、健康问题或缺乏运动习惯。\n" +
                    "\n建议：\n" +
                    "● 运动建议：开始进行短时散步，例如每天至少进行两次10分钟的步行。\n" +
                    "● 饮食建议：增加水果和蔬菜的摄入量，减少高热量、高脂肪的食物，以帮助提高能量水平。\n" +
                    "● 生活建议：定时休息，每工作1小时起身活动5-10分钟，避免长时间一动不动。"

        steps in 3000..5000 -> output =
            "分析与评价：\n" +
                    "● 步数稍微有所增加，但仍然未达到健康活动的推荐水平。\n" +
                    "● 用户有一定的活动量，但仍需增加以获得健康益处。\n" +
                    "\n建议：\n" +
                    "● 运动建议：尝试每天至少完成30分钟的中等强度活动，如快走。\n" +
                    "● 饮食建议：保持充足的水分摄入，适量增加蛋白质来源，以支持活动增加。\n" +
                    "● 生活建议：尝试采用站立工作站，减少连续久坐的时间。"

        steps in 5000..7500 -> output =
            "分析与评价：\n" +
                    "● 用户的活动量接近日常推荐的健康标准。\n" +
                    "● 可能是通过日常工作和一些基本的身体活动积累的步数。\n" +
                    "\n建议：\n" +
                    "● 运动建议：加入一些有氧运动课程，如游泳或自行车，至少每周三次。\n" +
                    "● 饮食建议：适量摄入复合碳水化合物和全谷物，为运动提供持续能量。\n" +
                    "● 生活建议：保持良好的睡眠习惯，每晚争取7-8小时的睡眠，以支持日间的活动。\n"

        steps > 7500 -> output = "分析与评价：\n" +
                "● 用户的活动量很高，超过了大多数健康成人的平均步数。\n" +
                "● 恭喜您已经将活动融入了日常生活中。\n" +
                "\n建议：\n" +
                "● 运动建议：维持当前活动水平，可以尝试增加一些力量训练，以促进肌肉平衡和整体力量。\n" +
                "● 饮食建议：确保摄入足够的蛋白质和健康脂肪，支持身体恢复和能量需求。\n" +
                "● 生活建议：注意适当的身体恢复和避免过度训练，确保有足够的休息和放松时间。"
    }
    Text(
        text = "分析与建议",
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
                text = "Yue", style = TextStyle(
                    color = Color.Blue,
                    lineHeight = 24.sp  // 设置行距为24sp
                )
            )
        }
    }
}

