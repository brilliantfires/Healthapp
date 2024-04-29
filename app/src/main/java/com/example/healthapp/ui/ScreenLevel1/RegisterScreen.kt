package com.example.healthapp.ui.ScreenLevel1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.R
import com.example.healthapp.data.viewmodel.SyncViewModel

@Composable
fun RegisterScreen(
    syncViewModel: SyncViewModel,
    navController: NavHostController
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreeToPrivacyPolicy by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(true) }
    var emailError by remember { mutableStateOf(true) }
    var passwordError by remember { mutableStateOf(true) }
    var confirmPasswordError by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id=R.drawable.health_app) ,
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

            Text("创建新账号!", fontSize = 24.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = it.length <= 3
                },
                label = { Text("用户名") },
                singleLine = true,
                isError = usernameError
            )
            if (usernameError) {
                Column(
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("用户名长度必须大于3位", color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                label = { Text("邮箱") },
                singleLine = true,
                isError = emailError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            if (emailError) {
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
                    passwordError = it.length <= 8
                },
                label = { Text("密码") },
                singleLine = true,
                isError = passwordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = it != password
                },
                label = { Text("确认密码") },
                singleLine = true,
                isError = confirmPasswordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            if (confirmPasswordError) {
                Column(
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("确认密码与密码不匹配", color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreeToPrivacyPolicy,
                    onCheckedChange = { agreeToPrivacyPolicy = it }
                )
                Text(text = "同意隐私政策",
                    modifier = Modifier.clickable { agreeToPrivacyPolicy = !agreeToPrivacyPolicy })
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (agreeToPrivacyPolicy && !usernameError && !emailError && !passwordError && !confirmPasswordError) {
                        syncViewModel.registerUser(username, email, password)
                        showSuccessDialog = true
                    }
                },
                enabled = agreeToPrivacyPolicy && !usernameError && !emailError && !passwordError && !confirmPasswordError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    "注册",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("已有账户? 返回登录",
                color = Color.Blue,
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .clickable {
                        navController.popBackStack()
                    }
                    .align(Alignment.End)
            )
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("注册成功") },
            text = { Text("您的账号已创建成功") },
            confirmButton = {
                Button(onClick = { navController.navigate("LoginScreen") }) {
                    Text("去登录")
                }
            }
        )
    }
}
