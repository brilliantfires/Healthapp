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
import com.example.healthapp.Navigation.FloorsClimbedDetailsScreenDestination
import com.example.healthapp.Navigation.SourceAndVisitScreenDestination
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.ui.ScreenLevel3.AddFloorsClimbedDialog
import com.example.healthapp.ui.ToolClass.FloorMonthChartView
import com.example.healthapp.ui.ToolClass.FloorWeekChartView
import com.example.healthapp.ui.ToolClass.FloorYearlyChartView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbstractItemOfFloorsClimbedDetailScreen(
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
    val sumValues = allActivity.sumOf { it.floorsClimbed ?: 0 }
    val averageValues = if (allActivity.isNullOrEmpty()) 0 else sumValues / allActivity.size
    val theDate = LocalDateTime.now()
    val date = theDate.format(DateTimeFormatter.ISO_DATE)
    // 控制弹窗键值
    var showAddDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "爬楼层数")
                    }
                },
                navigationIcon = {
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
                                text = averageValues.toString(),
                                style = TextStyle(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(text = "层")
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
                                    "Week" -> FloorWeekChartView(lastSevenDaysActivities)

                                    "Month" -> FloorMonthChartView(lastMonthActivities)

                                    "Year" -> FloorYearlyChartView(lastYearActivities)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                AnalyzeFloors(
                    floors = lastSevenDaysActivities.firstOrNull()?.floorsClimbed ?: 0
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "关于爬楼层数",
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
                            text = "爬楼梯是一种简便而有效的有氧运动，有助于增强心肺功能、提高肌肉力量，尤其是下肢和臀部肌肉。" +
                                    "定期爬楼梯可以提高心血管健康，帮助控制体重，甚至降低慢性疾病的风险。" +
                                    "爬楼梯对比乘电梯，可以显著增加日常活动量，是一种易于实践的健康生活方式。" +
                                    "此外，它不需要特别的设备或昂贵的健身会员费，只需利用日常环境中的楼梯即可开始锻炼。",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp  // 设置行距为24sp
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "来源：World Health Organization",
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
                                        FloorsClimbedDetailsScreenDestination.createRoute(userId)
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
                                        route = SourceAndVisitScreenDestination.createRoute("爬楼层数",userId)
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
            AddFloorsClimbedDialog(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel
            ) {
                showAddDialog = false
            }
        }
    }
}

@Composable
fun AnalyzeFloors(
    floors: Int
) {
    var output by remember { mutableStateOf("") }
    when {
        floors < 10 -> output =
            "分析与评价：\n" +
                    "● 用户很少爬楼梯，可能主要使用电梯或生活和工作环境不便于爬楼梯。\n" +
                    "● 需要增加这种简单而有效的活动来改善心肺功能和增强肌肉力量。\n" +
                    "\n建议：\n" +
                    "● 运动建议：尝试开始每天爬楼梯，哪怕是少量几层，逐渐增加。\n" +
                    "● 饮食建议：增加富含铁和钙的食物，如绿叶蔬菜和奶制品，帮助肌肉和骨骼健康。\n" +
                    "● 生活建议：选择走楼梯而非电梯，哪怕一开始只是一部分楼层。"

        floors in 10..20 -> output =
            "分析与评价：\n" +
                    "● 用户偶尔选择爬楼梯，但还有很大的提升空间。\n" +
                    "● 爬楼梯的次数已经有一定的基础，可以进一步增加。\n" +
                    "\n建议：\n" +
                    "● 运动建议：制定每天至少爬楼20层的目标，并逐步增加。\n" +
                    "● 饮食建议：保证足够的蛋白质摄入，如鸡肉、鱼类，支持肌肉发展。\n" +
                    "● 生活建议：在日常活动中尽可能选择楼梯，例如在上班或购物中心。"

        floors in 20..50 -> output =
            "分析与评价：\n" +
                    "● 用户定期爬楼梯，这是一个很好的体力活动习惯。\n" +
                    "● 这个级别的爬楼层数可以带来显著的心血管和肌肉力量好处。\n" +
                    "\n建议：\n" +
                    "● 运动建议：挑战更高的楼层数，例如设定每天30-50层的新目标。\n" +
                    "● 饮食建议：增加含镁的食物（如坚果和种子），帮助肌肉和神经功能。\n" +
                    "● 生活建议：尝试在休息日进行户外徒步，增加身体活动的多样性。"

        floors > 50 -> output =
            "分析与评价：\n" +
                    "● 用户已将爬楼梯作为日常重要的身体活动部分。\n" +
                    "● 这种活动水平有助于维持优良的心血管健康和良好的身体机能。\n" +
                    "\n建议：\n" +
                    "● 运动建议：维持当前的爬楼活动，可以尝试加入其他形式的训练，如短跑或体能训练，以提高全身力量和耐力。\n" +
                    "● 饮食建议：确保摄入足够的碳水化合物和高质量脂肪，以支持高强度的体力活动。\n" +
                    "● 生活建议：确保有足够的恢复时间和适当的休息，如充足的睡眠和定期的深度放松。"
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
                text = "Yue",
                style = TextStyle(
                    color = Color.Blue,
                    lineHeight = 24.sp  // 设置行距为24sp
                )
            )
        }
    }
}