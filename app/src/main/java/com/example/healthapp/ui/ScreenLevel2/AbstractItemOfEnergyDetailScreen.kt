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
import com.example.healthapp.Navigation.ActivityEnergyConsumptionScreenDestination
import com.example.healthapp.Navigation.SourceAndVisitScreenDestination
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.ui.ScreenLevel3.AddEnergyExpenditureDialog
import com.example.healthapp.ui.ToolClass.EnergyMonthChartView
import com.example.healthapp.ui.ToolClass.EnergyWeekChartView
import com.example.healthapp.ui.ToolClass.EnergyYearlyChartView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbstractItemOfEnergyDetailScreen(
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
    val allActivity by dailyActivityViewModel.allDailyActivities.collectAsState(initial = emptyList())

    val sumEnergy = allActivity.sumOf { it.energyExpenditure ?: 0.0 }
    val averageEnergy = if (allActivity.isNullOrEmpty()) 0.0 else sumEnergy / allActivity.size
    val theDate = LocalDateTime.now()
    val date = theDate.format(DateTimeFormatter.ISO_DATE)
    //添加数据按钮的键值
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "能量消耗")
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
                            showDialog = true
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
                                text = String.format("%.2f", averageEnergy),
                                style = TextStyle(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(text = "卡路里")
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
                                    "Week" -> EnergyWeekChartView(lastSevenDaysActivities)

                                    "Month" -> EnergyMonthChartView(lastMonthActivities)

                                    "Year" -> EnergyYearlyChartView(lastYearActivities)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                AnalyzeEnergy(
                    calories = lastSevenDaysActivities.firstOrNull()?.energyExpenditure ?: 0.0
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "关于能量消耗",
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
                            text = "能量消耗是指在日常生活中，人体进行各种活动所消耗的能量总量。" +
                                    "这包括基础代谢率（即身体在休息状态下维持生命所需的最低能量消耗）和通过活动产生的额外能量消耗。" +
                                    "走路、跑步、做家务和任何形式的锻炼都会增加能量消耗，有助于控制体重和改善健康状况。",
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
                    Column(
                        modifier = Modifier.background(color = Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(
                                        ActivityEnergyConsumptionScreenDestination.createRoute(
                                            userId
                                        )
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
                                        route = SourceAndVisitScreenDestination.createRoute(
                                            "能量消耗",
                                            userId
                                        )
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
    }
    if (showDialog) {
        // 弹出窗口逻辑，用于添加步数和日期
        AddEnergyExpenditureDialog(
            userId,
            dailyActivityViewModel,
            onDismiss = { showDialog = false })
    }
}

@Composable
fun AnalyzeEnergy(
    calories: Double
) {
    var output by remember { mutableStateOf("") }
    when {
        calories < 1500 -> output =
            "分析与评价：\n" +
                    "● 您的日常能量消耗非常低，可能是由于较少的身体活动或静态生活方式。\n" +
                    "● 增加日常活动可以帮助提高新陈代谢。\n" +
                    "\n建议：\n" +
                    "● 运动建议：适量增加日常的轻中度活动，如散步或简单的家庭锻炼。\n" +
                    "● 饮食建议：适当增加健康的碳水化合物和高质量蛋白质，以支持更多的身体活动。\n" +
                    "● 生活建议：定期进行小运动，比如每隔一小时站起来活动身体5分钟。\n"

        calories in 1500.0..2500.0 -> output =
            "分析与评价：\n" +
                    "● 用户的日常活动水平适中，可能包括工作日常活动及一定量的体育锻炼。\n" +
                    "● 这个能量消耗范围通常适合大多数正常活动水平和体重的成年人。\n" +
                    "\n建议：\n" +
                    "● 运动建议：尝试加入更多的有氧和力量训练组合，如每周进行2-3次力量训练和3-5次有氧运动。\n" +
                    "● 饮食建议：确保足够的蛋白质摄入支持肌肉恢复和增长，同时维持膳食中的均衡。\n" +
                    "● 生活建议：保持活跃的社交生活和足够的休息，这有助于保持身体和精神的健康。"

        calories > 2500 -> output =
            "分析与评价：\n" +
                    "● 用户可能是活跃的运动员或从事高强度体力劳动的个体。\n" +
                    "● 高水平的能量消耗需要更多的营养支持和适当的恢复策略。\n" +
                    "\n建议：\n" +
                    "● 运动建议：保持高水平的训练强度，并定期与专业人员讨论训练计划的调整。\n" +
                    "● 饮食建议：增加碳水化合物的摄入以支持高强度训练需求，同时注意蛋白质和健康脂肪的摄入量。\n" +
                    "● 生活建议：重视运动后的恢复，包括足够的睡眠、肌肉放松和适当的体能恢复活动。"
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