package com.example.healthapp.ui.ScreenLevel1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.healthapp.Navigation.SettingDestination
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    userViewModel: UserViewModel, navController: NavHostController
) {
    val loginSuccess by userViewModel.loginSuccess.observeAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agreeToPrivacyPolicy by remember { mutableStateOf(false) }
    val errorMessage by userViewModel.errorMessage.observeAsState("")
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var loginAttempt by remember { mutableStateOf(0) }  // 登录尝试计数器
    var usernameError by remember { mutableStateOf(true) }         // 记录输入邮箱格式是否正确
    var passwordError by remember { mutableStateOf(true) }         // 记录输入的密码格式是否正确

    val theLoginUser by userViewModel.theLoginUser.observeAsState(initial = null)

    if (showErrorDialog) {
        userViewModel.errorResult(email = username)
        ShowErrorInformation(
            navController,
            errorMessage,
            onDismiss = { showErrorDialog = false },  // AlertDialog消失处理
            onRegisterClick = {
                navController.navigate("RegisterScreen") {
                    //popUpTo("RegisterScreen")
                    //launchSingleTop = true    // 这里是清除导航堆栈，也就是无法通过点击返回来回到刚才的界面
                    restoreState = true
                }
            }
        )
    }

    if (showSuccessDialog) {
        userViewModel.saveUserId(theLoginUser?.userId ?: 1)
        ShowSuccessAndLogin(
            userId = theLoginUser?.userId ?: 1,
            navController = navController,
            onDismiss = { showSuccessDialog = false },  // AlertDialog消失处理
            onRegisterClick = {}
        )
    }

    LaunchedEffect(loginAttempt) {
        when (loginSuccess) {
            true -> showSuccessDialog = true
            false -> showErrorDialog = true
            else -> { /* no action needed */
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.health_app),
            contentDescription = "App Icon",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text("登录您的账号!", fontSize = 24.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                label = { Text("邮箱") },
                singleLine = true,
                isError = usernameError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                trailingIcon = {
                    if (usernameError) {
                        Icon(Icons.Filled.Error, contentDescription = "Error")
                    }
                }
            )
            if (usernameError) {
                Column(
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("请输入有效的邮箱地址", color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = it.length <= 7
                },
                label = { Text("密码") },
                singleLine = true,
                isError = passwordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                trailingIcon = {
                    if (passwordError) {
                        Icon(Icons.Filled.Error, contentDescription = "Error")
                    }
                },
                keyboardActions = KeyboardActions(onDone = {
                    if (agreeToPrivacyPolicy && !usernameError && !passwordError) {
                        userViewModel.loginUser(username, password)
                    }
                })
            )
            if (passwordError) {
                Column(
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("密码长度必须超过8位", color = Color.Red)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = agreeToPrivacyPolicy,
                    onCheckedChange = { agreeToPrivacyPolicy = it })
                Text(text = "同意隐私政策",
                    modifier = Modifier.clickable { agreeToPrivacyPolicy = !agreeToPrivacyPolicy })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (agreeToPrivacyPolicy && !usernameError && !passwordError) {
                        userViewModel.loginUser(username, password)
                        loginAttempt++
                    }
                },
                enabled = agreeToPrivacyPolicy && !usernameError && !passwordError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    "登录",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            Text("忘记密码?",
                color = Color.Blue,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        navController.navigate("PasswordRecoveryScreen") {
                            restoreState = true
                        }
                    }
                    .align(Alignment.End))

            Spacer(modifier = Modifier.height(8.dp))

            Text("没有账户?立刻注册!",
                color = Color.Blue,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        navController.navigate("RegisterScreen") {
                            //popUpTo("RegisterScreen")
                            //launchSingleTop = true    // 这里是清除导航堆栈，也就是无法通过点击返回来回到刚才的界面
                            restoreState = true
                        }
                    }
                    .align(Alignment.End))
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ShowSuccessAndLogin(
    userId: Int,
    navController: NavHostController,
    onDismiss: () -> Unit,
    onRegisterClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            // 用户点击对话框外部或返回按钮时调用
            onDismiss()
        },
        title = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "登录成功!",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        text = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "将在3秒钟之内跳转 . . .",
                    fontSize = 20.sp
                )
            }

        },
        confirmButton = {
            // 确认按钮如果不需要，可以不添加
        },
        dismissButton = {
            // 如果不需要额外的按钮也可以省略
        }
    )

    // 使用 LaunchedEffect 启动一个协程，实现延时后的动作
    LaunchedEffect(Unit) {
        delay(2500)  // 延迟3秒钟
        onDismiss()  // 关闭对话框
        //delay(100)
        navController.navigate(SettingDestination.createRoute(userId))  // 替换为你需要导航到的实际路由
    }
}


@Composable
fun ShowErrorInformation(
    navController: NavHostController,
    errorMessage: String,
    onDismiss: () -> Unit,  // 添加一个回调函数用于处理对话框消失
    onRegisterClick: () -> Unit  // 添加一个回调函数用于处理注册按钮点击
) {
    Dialog(
        onDismissRequest = {
            // 用户点击对话框外部或返回按钮时调用
            onDismiss()  // 调用外部传入的消失处理函数
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "登录错误!",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = errorMessage,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            onRegisterClick()  // 调用外部传入的注册按钮处理函数
                        }, shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("注册账号")
                    }
                    Button(
                        onClick = {
                            onDismiss()  // 点击重新登录后消失Dialog
                        }, shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("重新登录")
                    }
                }
            }
        }
    }
}


