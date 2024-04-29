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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.Navigation.SleepRecordDetailsScreenDestination
import com.example.healthapp.Navigation.SourceAndVisitScreenDestination
import com.example.healthapp.data.viewmodel.SleepRecordViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.ui.ScreenLevel3.AddSleepRecordDialog
import com.example.healthapp.ui.ToolClass.SleepRecordMonthChartView
import com.example.healthapp.ui.ToolClass.SleepRecordWeekChartView
import com.example.healthapp.ui.ToolClass.SleepRecordYearlyChartView
import com.example.healthapp.ui.ToolClass.convertTimeToHours
import com.example.healthapp.ui.ToolClass.timeToDouble
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbstractItemOfSleepRecordDetailScreen(
    userId: Int,
    userViewModel: UserViewModel,
    sleepRecordViewModel: SleepRecordViewModel, // 注意这里修正了您的代码中的小错误：添加了缺失的逗号
    navController: NavHostController
) {
    val lastSevenSleepRecord by sleepRecordViewModel.getLatestSevenSleepRecord(userId)
        .collectAsState(
            initial = emptyList()
        )
    val lastMonthSleepRecord by sleepRecordViewModel.getLastMonthSleepRecords(userId)
        .collectAsState(
            initial = emptyList()
        )
    val lastYearSleepRecord by sleepRecordViewModel.getLastYearSleepRecords(userId).collectAsState(
        initial = emptyList()
    )
    var selectedChart by remember { mutableStateOf("Week") }
    val allRecords by sleepRecordViewModel.getAllSleepRecordsByUserId(userId)
        .collectAsState(initial = emptyList())
    val sumDuration = allRecords.sumOf { timeToDouble(it.totalDuration ?: "0:0:0") }
    val averageDuration = if (allRecords.isNullOrEmpty()) 0.0 else sumDuration / allRecords.size
    val theDate = LocalDateTime.now()
    val date = theDate.format(DateTimeFormatter.ISO_DATE)
    // 控制弹窗键值
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "睡眠记录")
                    }
                },
                navigationIcon = {
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.padding(start = 8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIos,
                                contentDescription = "返回",
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
                        Text(text = "平均睡眠时长")
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = String.format("%.2f", averageDuration),
                                style = TextStyle(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
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

                                // 使用 Row 布局来安排按钮，并设置宽度平分空间
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
                                    "Week" -> SleepRecordWeekChartView(lastSevenSleepRecord)

                                    "Month" -> SleepRecordMonthChartView(lastMonthSleepRecord)

                                    "Year" -> SleepRecordYearlyChartView(lastYearSleepRecord)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                AnalyzeSleepDuration(
                    convertTimeToHours(
                        lastSevenSleepRecord.firstOrNull()?.totalDuration ?: "0:0:0"
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))
                // 以下可以根据需要添加更多相关信息，例如“关于睡眠的重要性”、“如何改善睡眠质量”的教育性内容等。
                Text(
                    text = "关于睡眠",
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
                            text = "睡眠是身体和心理恢复的关键过程，它分为多个阶段，包括浅度睡眠和深度睡眠。" +
                                    "浅度睡眠有助于身体放松，是睡眠周期的初期阶段。" +
                                    "深度睡眠，或称为缓慢波睡眠，是身体修复、记忆巩固和能量恢复的关键阶段。" +
                                    "良好的睡眠习惯有助于保持健康和提高日间的警觉性。",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp  // 设置行距为24sp
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "来源：Mayo Clinic",
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
                                        SleepRecordDetailsScreenDestination.createRoute(userId)
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
                                        route = SourceAndVisitScreenDestination.createRoute("睡眠时间",userId)
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
            AddSleepRecordDialog(userId = userId, sleepRecordViewModel = sleepRecordViewModel) {
                showAddDialog = false
            }
        }
    }
}

@Composable
fun AnalyzeSleepDuration(
    sleepDuration: Double
) {
    var output by remember { mutableStateOf("") }
    when {
        sleepDuration < 5.0 -> output =
            "分析与评价：\n" +
                    "● 您的睡眠时间不足，可能导致疲劳累积和认知功能下降。\n" +
                    "● 长期睡眠不足可能增加健康风险，如心脏病、糖尿病和情绪问题。\n" +
                    "\n建议：\n" +
                    "● 睡眠建议：尝试建立固定的睡眠时间表，晚上确保至少7-8小时的连续睡眠。\n" +
                    "● 环境建议：优化睡眠环境，减少噪音和光线干扰，使用舒适的床垫和枕头。\n" +
                    "● 生活建议：避免晚上摄入咖啡因和大餐，减少晚上使用电子设备的时间。\n"

        sleepDuration in 5.0..6.5 -> output =
            "分析与评价：\n" +
                    "● 您的睡眠时间略低于推荐水平，可能感觉不够精神饱满。\n" +
                    "● 稍微增加睡眠时间可以帮助改善整体健康和日间表现。\n" +
                    "\n建议：\n" +
                    "● 睡眠建议：逐步增加每晚睡眠时间，目标是每晚至少7小时。\n" +
                    "● 环境建议：确保睡前有放松的准备时间，如阅读或冥想。\n" +
                    "● 生活建议：创建放松的夜间例行程序，帮助身体准备进入睡眠状态。\n"

        sleepDuration in 6.5..8.0 -> output =
            "分析与评价：\n" +
                    "● 您的睡眠时间基本符合成人的健康推荐标准。\n" +
                    "● 保持这种睡眠习惯对维持良好的身体和心理健康至关重要。\n" +
                    "\n建议：\n" +
                    "● 睡眠建议：继续维持当前的睡眠模式，保持一致的睡眠和起床时间。\n" +
                    "● 环境建议：继续优化睡眠环境，保持安静、黑暗和适当的温度。\n" +
                    "● 生活建议：保持健康的生活方式，包括规律的运动和均衡的饮食。\n"

        sleepDuration > 8.0 -> output =
            "分析与评价：\n" +
                    "● 您的睡眠时间充足，这对于恢复体力和精神状态非常有益。\n" +
                    "● 确保睡眠质量同样重要，过多的睡眠有时可能是疲劳或健康问题的标志。\n" +
                    "\n建议：\n" +
                    "● 睡眠建议：如果经常感到疲劳，即使睡眠充足，考虑检查睡眠质量和可能的健康问题。\n" +
                    "● 环境建议：持续关注睡眠质量，如有需要，可以使用睡眠跟踪器。\n" +
                    "● 生活建议：维持健康的日常习惯，包括适当的运动和避免晚上过晚饮食。\n"
    }
    Text(
        text = "睡眠时长分析与建议",
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
                text = "愿您拥有良好的睡眠！", style = TextStyle(
                    color = Color.Blue,
                    lineHeight = 24.sp // 设置行距为24sp
                )
            )
        }
    }
}

