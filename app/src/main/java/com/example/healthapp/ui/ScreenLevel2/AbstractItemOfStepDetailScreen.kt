package com.example.healthapp.ui.ScreenLevel2

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
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.ui.ToolClass.ChartView
import com.example.healthapp.ui.ToolClass.YearlyChartView
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbstractItemOfStepDetailScreen(
    userId: Int,
    userViewModel: UserViewModel,
    physicalProfileViewModel: PhysicalProfileViewModel,
    dailyActivityViewModel: DailyActivityViewModel,
    navController: NavHostController
) {
    val lastSevenDaysActivities by dailyActivityViewModel.getLastSevenActivities(userId)
        .collectAsState(initial = emptyList())
    val stepList: List<Int> = lastSevenDaysActivities.map { it.steps ?: 0 }
    var selectedChart by remember { mutableStateOf("Week") }
    Scaffold(topBar = {
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
                    // 下面是点对点转换界面
                    /*.navigate("AbstractScreen") {
                    // 清除HomePageScreen（包含）之上的堆栈
                    popUpTo("AbstractScreen") {
                        inclusive = true // HomePageScreen 也被清除
                    }
                    launchSingleTop = true    // 避免重复创建HomePageScreen的实例
                    restoreState = true       // 如果可能，恢复之前的状态
                }*/
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface, // 设置按钮背景颜色与背景一致
                    contentColor = MaterialTheme.colorScheme.onSurface // 设置按钮内容颜色以确保可见性
                ), modifier = Modifier.padding(start = 8.dp), contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIos,
                        contentDescription = stringResource(id = R.string.back_text),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "摘要", style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }, actions = {
            TextButton(
                onClick = {}, modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = "添加数据", style = TextStyle(
                        fontWeight = FontWeight.Bold, fontSize = 20.sp
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
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "平均")
                        Row(
                            verticalAlignment = Alignment.Bottom // 设置垂直对齐方式为底部对齐
                        ) {
                            Text(
                                text = "1,353", style = TextStyle(
                                    fontSize = 32.sp, fontWeight = FontWeight.Bold
                                )
                            )
                            Text(text = "步")
                        }
                        Text(
                            text = "2024年2月1日", style = TextStyle(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Card(
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth(),
                        ) {
                            Column {
                                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                                    Button(onClick = { selectedChart = "Week" }) {
                                        Text("7 Days")
                                    }
                                    Button(onClick = { selectedChart = "Month" }) {
                                        Text("1 Month")
                                    }
                                    Button(onClick = { selectedChart = "Year" }) {
                                        Text("1 Year")
                                    }
                                }

                                when (selectedChart) {
                                    "Week" -> ChartView(
                                        dailyActivityViewModel.getLastSevenActivities(
                                            userId
                                        )
                                    )

                                    "Month" -> ChartView(
                                        dailyActivityViewModel.getActivitiesFrom(
                                            LocalDateTime.now().minusMonths(1), userId
                                        )
                                    )

                                    "Year" -> YearlyChartView(
                                        dailyActivityViewModel.getActivitiesFrom(
                                            LocalDateTime.now().minusYears(1),
                                            userId
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "关于步数", style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "步数是指你一天中走了多少步。" + "计步器和数字运动追踪器可帮助你确定步数。" + "这类设备会为所有产生步数的活动计算步数，" + "如走路、跑步、上下楼梯、越野滑雪，甚至做家务活等活动。",
                            style = TextStyle(
                                fontSize = 16.sp
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
                        modifier = Modifier.padding(16.dp)
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
                    Column() {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { navController.navigate("StepDetailsScreen") }) {
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
                            modifier = Modifier.clickable { }) {
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
    }
}
