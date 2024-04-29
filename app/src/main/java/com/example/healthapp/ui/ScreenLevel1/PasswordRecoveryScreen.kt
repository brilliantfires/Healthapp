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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.UserViewModel

@Composable
fun PasswordRecoveryScreen(navController: NavHostController, userViewModel: UserViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(true) }
    var newPasswordError by remember { mutableStateOf(true) }
    var confirmPasswordError by remember { mutableStateOf(true) }
    val resetPasswordStatus by userViewModel.resetPasswordStatus.observeAsState()
    var clickCount by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter =painterResource(id=R.drawable.health_app) ,
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

            Text("找回密码", fontSize = 24.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = { Text("用户名") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                label = { Text("邮箱") },
                isError = emailError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                trailingIcon = {
                    if (emailError) {
                        Icon(Icons.Filled.Error, contentDescription = "Error")
                    }
                }
            )
            if (emailError) {
                Text("请输入有效的邮箱地址", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    newPasswordError = it.length <= 7
                },
                label = { Text("新密码") },
                isError = newPasswordError,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                trailingIcon = {
                    if (newPasswordError) {
                        Icon(Icons.Filled.Error, contentDescription = "Error")
                    }
                }
            )
            if (newPasswordError) {
                Text("新密码长度必须超过8位", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = it != newPassword
                },
                label = { Text("确认新密码") },
                isError = confirmPasswordError,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            if (confirmPasswordError) {
                Text("确认密码与新密码不匹配", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (!emailError && !newPasswordError && !confirmPasswordError) {
                        userViewModel.resetPassword(username, email, newPassword)
                        clickCount++
                    }
                },
                enabled = !emailError && !newPasswordError && !confirmPasswordError,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Text("提交", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("已有账户？返回登录",
                color = Color.Blue,
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .clickable {
                        navController.navigate("LoginScreen") {
                            popUpTo("LoginScreen") { inclusive = true }
                        }
                    }
                    .align(Alignment.End))
        }
    }

    LaunchedEffect(clickCount) {
        if (resetPasswordStatus == true) {
            showDialog = 1
        } else if (resetPasswordStatus == false) {
            showDialog = 2
        } else {
            showDialog = 0
        }
    }
    if (showDialog == 1) {
        Dialog(
            onDismissRequest = { showDialog = 0 }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = Color.White)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(24.dp))
                // 头像居中显示
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
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
                    text = "智能健康管理系统",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    // 使用大标题
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "密码修改成功!!",
                    modifier = Modifier.padding(top = 24.dp, bottom = 20.dp),
                    style = MaterialTheme.typography.titleLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            showDialog = 0
                            navController.navigate("LoginScreen") {
                            }
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("返回登录")
                    }
                }
            }
        }
    }
    if (showDialog == 2) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(24.dp))
            // 头像居中显示
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
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
                text = "智能健康管理系统",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally),
                // 使用大标题
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "密码修改失败!!\n用户名或邮箱填写错误!",
                modifier = Modifier.padding(top = 24.dp, bottom = 20.dp),
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        showDialog = 0
                        navController.navigate("LoginScreen") {

                        }
                    },
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("返回登录")
                }
                Button(
                    onClick = {
                        showDialog = 0
                    },
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("继续尝试")
                }
            }
        }
    }
}
