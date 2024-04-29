package com.example.healthapp.ui.ScreenLevel2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.Navigation.ActivityEnergyConsumptionScreenDestination
import com.example.healthapp.Navigation.ExerciseDurationDetailsScreenDestination
import com.example.healthapp.Navigation.FloorsClimbedDetailsScreenDestination
import com.example.healthapp.Navigation.RunningDistanceDetailScreenDestination
import com.example.healthapp.Navigation.SleepRecordDetailsScreenDestination
import com.example.healthapp.Navigation.StepDetailsScreenDestination
import com.example.healthapp.Navigation.WalkDistanceDetailScreenDestination
import com.example.healthapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceAndVisitScreen(
    userId: Int,
    name: String,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "数据源与访问",
                            style = MaterialTheme.typography.titleLarge
                        )
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
                                text = "返回",
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
                        onClick = { },
                        modifier = Modifier.width(80.dp)
                    ) {
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                }

            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = painterResource(id = R.drawable.heat),
                    contentDescription = "App Icon",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = name,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = "允许读取数据的APP和服务",
                    modifier = Modifier
                        .padding(start = 32.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    color = Color.Gray
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color.White)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "无",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
                Text(
                    text = "与以上App和服务共享活动能量数据。App和服务在请求访问你的健康数据时，会被添加到以上列表中。\n",
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    color = Color.Gray
                )


                Text(
                    text = "允许读取数据的调研机构",
                    modifier = Modifier
                        .padding(start = 32.dp, bottom = 8.dp, top = 8.dp)
                        .fillMaxWidth(),
                    color = Color.Gray
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color.White)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "无",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
                Text(
                    text = "与上列调研机构共享活动能量数据。你在“研究”App中授权之后，它们将被添加到列表中。你可以前往“研究”App查看并管理已注册的所有调研。\n",
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    color = Color.Gray
                )


                Text(
                    text = "数据源",
                    modifier = Modifier
                        .padding(start = 32.dp, bottom = 8.dp, top = 8.dp)
                        .fillMaxWidth(),
                    color = Color.Gray
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color.White)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                when (name) {
                                    "步数" -> navController.navigate(
                                        StepDetailsScreenDestination.createRoute(
                                            userId
                                        )
                                    )

                                    "爬楼层数" -> navController.navigate(
                                        FloorsClimbedDetailsScreenDestination.createRoute(userId)
                                    )

                                    "步行距离" -> navController.navigate(
                                        WalkDistanceDetailScreenDestination.createRoute(userId)
                                    )

                                    "跑步距离" -> navController.navigate(
                                        RunningDistanceDetailScreenDestination.createRoute(userId)
                                    )

                                    "跑步时间" -> navController.navigate(
                                        ExerciseDurationDetailsScreenDestination.createRoute(userId)
                                    )

                                    "能量消耗" -> navController.navigate(
                                        ActivityEnergyConsumptionScreenDestination.createRoute(
                                            userId
                                        )
                                    )

                                    "睡眠时间" -> navController.navigate(
                                        SleepRecordDetailsScreenDestination.createRoute(userId)
                                    )
                                }
                            }
                            .background(color = Color.White)
                    ) {
                        Icon(
                            Icons.Filled.PhoneIphone,
                            contentDescription = stringResource(id = R.string.enter_text),
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .width(32.dp)
                        )
                        Text(
                            text = "Yue 的 XiaoMi 12SUltra",
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Filled.KeyboardArrowRight,
                            contentDescription = stringResource(id = R.string.enter_text),
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 48.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                when (name) {
                                    "步数" -> navController.navigate(
                                        StepDetailsScreenDestination.createRoute(
                                            userId
                                        )
                                    )

                                    "爬楼层数" -> navController.navigate(
                                        FloorsClimbedDetailsScreenDestination.createRoute(userId)
                                    )

                                    "步行距离" -> navController.navigate(
                                        WalkDistanceDetailScreenDestination.createRoute(userId)
                                    )

                                    "跑步距离" -> navController.navigate(
                                        RunningDistanceDetailScreenDestination.createRoute(userId)
                                    )

                                    "跑步时间" -> navController.navigate(
                                        ExerciseDurationDetailsScreenDestination.createRoute(userId)
                                    )

                                    "能量消耗" -> navController.navigate(
                                        ActivityEnergyConsumptionScreenDestination.createRoute(
                                            userId
                                        )
                                    )

                                    "睡眠时间" -> navController.navigate(
                                        SleepRecordDetailsScreenDestination.createRoute(userId)
                                    )
                                }
                            }
                            .background(color = Color.White)
                    ) {
                        Icon(
                            Icons.Filled.PhoneIphone,
                            contentDescription = stringResource(id = R.string.enter_text),
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .width(32.dp)
                        )
                        Text(
                            text = "未知设备",
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Filled.KeyboardArrowRight,
                            contentDescription = stringResource(id = R.string.enter_text),
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }
                Text(
                    text = "允许以上数据源更新你的活动能量数据。若有多个来源，则会根据上列优先顺序选用一个来源。\n",
                    modifier = Modifier
                        .padding(start = 32.dp, end = 32.dp, top = 8.dp)
                        .fillMaxWidth(),
                    color = Color.Gray
                )
            }
        }
    }
}
