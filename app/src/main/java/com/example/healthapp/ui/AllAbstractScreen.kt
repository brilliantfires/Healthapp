package com.example.healthapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.DisplayCardViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.SleepRecordViewModel
import com.example.healthapp.data.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAbstractScreen(
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



    Scaffold(topBar = {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.all_abstract_text),
                        style = MaterialTheme.typography.titleLarge
                    )
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
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .width(80.dp),
                    contentPadding = PaddingValues(0.dp)
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
                                    navController.navigate("HealthDetailScreen") {
                                        // 清除HomePageScreen（包含）之上的堆栈
                                        popUpTo("HealthDetailScreen") {
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
    }) { innerPadding ->
        AllAbstractBodyContent(
            userId,
            Modifier.padding(innerPadding),
            navController,
            dailyActivityViewModel,
            sleepRecordViewModel,
            displayCardViewModel
        )
    }
}

@Composable
fun AllAbstractBodyContent(
    userId: Int,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    dailyActivityViewModel: DailyActivityViewModel,
    sleepRecordViewModel: SleepRecordViewModel,
    displayCardViewModel: DisplayCardViewModel,
) {
    val displayedCards by displayCardViewModel.displayedCards.observeAsState(initial = emptyList())
    val allCards by displayCardViewModel.allCards.observeAsState(initial = emptyList())

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(allCards) { card ->
            when (card.cardName) {
                "AbstractStepCard" -> AbstractStepCard(
                    userId, dailyActivityViewModel, navController, "AbstractItemOfStepDetailScreen"
                )

                "AbstractFloorClimbedCard" -> AbstractFloorClimbedCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    "AbstractItemOfFloorsClimbedDetailScreen"
                )

                "AbstractWalkingDistanceCard" -> AbstractWalkingDistanceCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    "AbstractItemOfWalkDistanceDetailScreen"
                )

                "AbstractExerciseDurationCard" -> AbstractExerciseDurationCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    "AbstractItemOfExerciseDurationDetailScreen"
                )

                "AbstractRunningDistanceCard" -> AbstractRunningDistanceCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    "AbstractItemOfRunningDistanceDetailScreen"
                )

                "AbstractEnergyExpenditureCard" -> AbstractEnergyExpenditureCard(
                    userId,
                    dailyActivityViewModel,
                    navController,
                    "AbstractItemOfEnergyDetailScreen"
                )

                "AbstractSleepRecordCard" -> AbstractSleepRecordeCard(
                    userId,
                    sleepRecordViewModel,
                    navController,
                    "AbstractItemOfSleepRecordDetailScreen"
                )

                else -> {} // 对于不认识的cardName，不展示任何内容
            }
        }

        /*// 根据需要添加更多卡片
        item {
            Card(modifier = Modifier.clickable { }
                // .padding(vertical = 16.dp) // 在LazyColumn中，最好将padding移到Card内部或者使用padding()修饰符单独应用于每个item
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Stars,
                        contentDescription = "应用图标"
                    )
                    Text(
                        text = "显示所有健康数据",
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "进入"
                    )
                }
            }
        }*/
    }

}