package com.example.healthapp.ui.ScreenLevel1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.healthapp.Navigation.AbstractScreenDestination
import com.example.healthapp.Navigation.AdminScreenDestination
import com.example.healthapp.Navigation.BottomNavigationBar
import com.example.healthapp.Navigation.HealthDetailsScreenDestination
import com.example.healthapp.R
import com.example.healthapp.data.entity.User
import com.example.healthapp.data.viewmodel.SleepRecordViewModel
import com.example.healthapp.data.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    userId: Int,
    userViewModel: UserViewModel,
    sleepRecordViewModel: SleepRecordViewModel,
    onBackClicked: () -> Unit, // 假设这是返回上一级界面的逻辑
    navController: NavHostController,
) {
    // 初始化需要的数据
    val user by userViewModel.getUserById(userId)
        .collectAsState(initial = null)

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(id = R.string.setting_text),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    Button(
                        onClick = {
                            navController.navigate(AbstractScreenDestination.createRoute(userId)) {
                                popUpTo(navController.graph.startDestinationId)
                                // launchSingleTop = true    // 这里是清除导航堆栈，也就是无法通过点击返回来回到刚才的界面
                                restoreState = true
                            }
                            // onBackClicked() // 非编辑模式下执行返回操作
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
                    IconButton(
                        onClick = {},
                        modifier = Modifier.width(80.dp)
                    ) {
//                        Icon(
//                            Icons.Filled.DoNotTouch,
//                            stringResource(id = R.string.nothing_text),
//                            tint = MaterialTheme.colorScheme.surfaceContainer // 在编辑模式下使用灰色图标表示禁用
//                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, userId)
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // 头像居中显示
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ab5_hiit),
                    contentDescription = stringResource(id = R.string.avatar_text),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        //定义140dp的圆形，clip
                        .size(140.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(Modifier.height(16.dp))

            // 显示用户姓名
            Text(
                text = "${user?.username}",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                // 使用大标题
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(32.dp))

            // 显示健康详细信息行
            Card(
                modifier = Modifier.clickable {
                    navController.navigate(HealthDetailsScreenDestination.createRoute(userId)) {
                        // popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true    // 这里是清除导航堆栈，也就是无法通过点击返回来回到刚才的界面
                        restoreState = true
                    }
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.health_detail_info_text),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            // .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(id = R.string.enter_text),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // 功能标题
            Text(
                text = stringResource(id = R.string.function_text),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )

            // 功能标题下板块
            Card() {
                // 此处是同步与备份设置行
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("DataSynchronizationScreen") {
                                restoreState = true
                            }
                        }
                        .background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(R.string.data_sync_text),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
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
                        .padding(horizontal = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            showDialog = true
                        }
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "账户管理",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
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
                        .padding(horizontal = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = AdminScreenDestination.route)
                        }
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "管理员账户",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
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
                        .padding(horizontal = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
//                            navController.navigate("HealthDetailScreen") {
////                        popUpTo(navController.graph.startDestinationId)
//                                // launchSingleTop = true    // 这里是清除导航堆栈，也就是无法通过点击返回来回到刚才的界面
//                                restoreState = true
//                            }
                        }
                        .background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.notification_text),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
//                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(id = R.string.enter_text),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // 隐私标题
            Text(
                text = stringResource(id = R.string.privacy_text),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
            // 隐私标题下的三个板块
            Card() {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("PrivacyPolicyScreen") {
                                restoreState = true
                            }
                        }
                        .background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.privacy_policy_text),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 8.dp)
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
                        .padding(horizontal = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("FeedbackScreen") {
                                restoreState = true
                            }
                        }
                        .background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.feedback_text),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
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
                        .padding(horizontal = 16.dp)

                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("AboutAppScreen") {
//                        popUpTo(navController.graph.startDestinationId)
                                // launchSingleTop = true    // 这里是清除导航堆栈，也就是无法通过点击返回来回到刚才的界面
                                restoreState = true
                            }
                        }
                        .background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.about_app_text),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(id = R.string.enter_text),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
        }
    }

    if (showDialog) {
        CustomLogoutDialog(user,
            onDismissRequest = { showDialog = false },
            onLogoutConfirm = {
                showDialog = false
                navController.navigate("LoginScreen") {
                    popUpTo("LoginScreen")
                    launchSingleTop = true    // 这里是清除导航堆栈，也就是无法通过点击返回来回到刚才的界面
                    restoreState = true
                }
            }
        )
    }
}

@Composable
fun CustomLogoutDialog(user: User?, onDismissRequest: () -> Unit, onLogoutConfirm: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // 使用 Column 和 Spacer 来构建布局
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            // 头像居中显示
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ab5_hiit),
                    contentDescription = stringResource(id = R.string.avatar_text),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        //定义140dp的圆形，clip
                        .size(140.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(Modifier.height(16.dp))

            // 显示用户姓名
            Text(
                text = "${user?.username}",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                // 使用大标题
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "是否退出登录？",
                modifier = Modifier.padding(top = 24.dp, bottom = 20.dp),
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("取消")
                }

                Button(
                    onClick = { onLogoutConfirm() },
                    modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("注销")
                }
            }
        }
    }
}