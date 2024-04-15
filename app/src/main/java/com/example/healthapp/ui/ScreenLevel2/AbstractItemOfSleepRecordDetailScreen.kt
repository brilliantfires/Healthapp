package com.example.healthapp.ui.ScreenLevel2

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.data.viewmodel.SleepRecordViewModel
import com.example.healthapp.data.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbstractItemOfSleepRecordDetailScreen(
    userId: Int,
    userViewModel: UserViewModel,
    sleepRecordViewModel: SleepRecordViewModel, // 注意这里修正了您的代码中的小错误：添加了缺失的逗号
    navController: NavHostController
) {
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
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "摘要",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = "添加数据",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
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
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "平均睡眠时长")
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "7小时45分钟",
                                style = TextStyle(
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = "2024年2月1日",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Card(
                            modifier = Modifier.height(256.dp),
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "【这里展示睡眠质量趋势图的图片】",
                                    style = TextStyle(
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                // 以下可以根据需要添加更多相关信息，例如“关于睡眠的重要性”、“如何改善睡眠质量”的教育性内容等。
                Text(
                    text = "关于步数",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "步数是指你一天中走了多少步。" +
                                    "计步器和数字运动追踪器可帮助你确定步数。" +
                                    "这类设备会为所有产生步数的活动计算步数，" +
                                    "如走路、跑步、上下楼梯、越野滑雪，甚至做家务活等活动。",
                            style = TextStyle(
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Mayo Clinic",
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
                        modifier = Modifier.padding(16.dp)
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
                    Column() {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { navController.navigate("SleepRecordDetailsScreen") }
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
                                .clickable { }
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
}
