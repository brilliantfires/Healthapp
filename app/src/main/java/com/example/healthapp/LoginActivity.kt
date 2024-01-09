//package com.example.healthapp
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.healthapp.ui.theme.HealthappTheme
//import com.example.healthapp.MyApp
//
//class LoginActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            HealthappTheme {
//
//            }
//        }
//    }
//}
//
//@Composable
//fun LoginScreen() {
//    // 定义用户名和密码的状态
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    // 定义登录按钮的点击事件
//    val onLoginClicked = {
//        // 验证用户名和密码是否正确
//        if (username == "admin" && password == "123456") {
//            // 登录成功，跳转到主界面
//            // TODO: 实现跳转逻辑
//            MyApp(modifier = Modifier.fillMaxSize())
//        } else {
//            // 登录失败，弹出提示
//            // TODO: 实现提示逻辑
//        }
//    }
//
//    // 使用Column布局来垂直排列组件
//    Column(
//        modifier = Modifier
//            .fillMaxSize() // 填充满屏幕
//            .padding(16.dp) // 设置内边距为16dp
//    ) {
//        // 使用Spacer组件来添加间隙
//        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分
//
//        // 创建用户名输入框
//        TextField(
//            value = username, // 绑定用户名状态
//            onValueChange = { username = it }, // 当文本变化时，更新用户名状态
//            label = { Text(stringResource(R.string.username_hint)) }, // 设置标签文本为“用户名”
//            singleLine = true, // 设置为单行输入
//            colors = TextFieldDefaults.colors( // 设置颜色
//                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
//                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
//            )
//        )
//
//        // 添加16dp的间隙
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 创建密码输入框
//        TextField(
//            value = password, // 绑定密码状态
//            onValueChange = { password = it }, // 当文本变化时，更新密码状态
//            label = { Text(stringResource(R.string.password_hint)) }, // 设置标签文本为“密码”
//            singleLine = true, // 设置为单行输入
//            visualTransformation = PasswordVisualTransformation(), // 设置文本的可视化转换为密码形式
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // 设置键盘类型为密码键盘
//            colors = TextFieldDefaults.colors( // 设置颜色
//                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
//                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
//            )
//        )
//
//        // 添加16dp的间隙
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 创建登录按钮
//        Button(
//            onClick = onLoginClicked, // 设置点击事件为登录按钮的点击事件
//            modifier = Modifier.align(Alignment.End) // 设置对齐方式为右对齐
//        ) {
//            Text("登录") // 设置按钮文本为“登录”
//        }
//
//        // 使用Spacer组件来添加间隙
//        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分
//    }
//}
//
//// 通过正则表达式来判断邮箱的格式是否为合法
//// 使用 matches() 方法来检查邮箱地址是否与正则表达式匹配
//fun isValidEmail(email: String): Boolean {
//    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
//    return email.matches(emailRegex)
//}
//
//@Composable
//fun RegisterScreen() {
//    // 定义用户名、密码、确认密码和邮箱的状态
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//
//    // 定义注册按钮的点击事件
//    val onRegisterClicked = {
//        // 验证用户名、密码、确认密码和邮箱是否合法
//        if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword == password && isValidEmail(email)) {
//            // 注册成功，跳转到主界面
//            // TODO: 实现跳转逻辑
//        } else {
//            // 注册失败，弹出提示
//            // TODO: 实现提示逻辑
//        }
//    }
//
//    // 使用Column布局来垂直排列组件
//    Column(
//        modifier = Modifier
//            .fillMaxSize() // 填充满屏幕
//            .padding(16.dp) // 设置内边距为16dp
//    ) {
//        // 使用Spacer组件来添加间隙
//        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分
//
//        // 创建用户名输入框
//        TextField(
//            value = username, // 绑定用户名状态
//            onValueChange = { username = it }, // 当文本变化时，更新用户名状态
//            label = { Text(stringResource(R.string.username_hint)) }, // 设置标签文本为“用户名”
//            singleLine = true, // 设置为单行输入
//            colors = TextFieldDefaults.colors( // 设置颜色
//                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
//                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
//            )
//        )
//
//        // 添加16dp的间隙
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 创建密码输入框
//        TextField(
//            value = password, // 绑定密码状态
//            onValueChange = { password = it }, // 当文本变化时，更新密码状态
//            label = { Text(stringResource(R.string.password_hint)) }, // 设置标签文本为“密码”
//            singleLine = true, // 设置为单行输入
//            visualTransformation = PasswordVisualTransformation(), // 设置文本的可视化转换为密码形式
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // 设置键盘类型为密码键盘
//            colors = TextFieldDefaults.colors( // 设置颜色
//                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
//                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
//            )
//        )
//
//        // 添加16dp的间隙
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 创建确认密码输入框
//        TextField(
//            value = confirmPassword, // 绑定确认密码状态
//            onValueChange = { confirmPassword = it }, // 当文本变化时，更新确认密码状态
//            label = { Text(stringResource(R.string.confirm_password_hint)) }, // 设置标签文本为“确认密码”
//            singleLine = true, // 设置为单行输入
//            visualTransformation = PasswordVisualTransformation(), // 设置文本的可视化转换为密码形式
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // 设置键盘类型为密码键盘
//            colors = TextFieldDefaults.colors( // 设置颜色
//                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
//                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
//            )
//        )
//
//        // 添加16dp的间隙
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 创建邮箱输入框
//        TextField(
//            value = email, // 绑定邮箱状态
//            onValueChange = { email = it }, // 当文本变化时，更新邮箱状态
//            label = { Text(stringResource(R.string.email_hint)) }, // 设置标签文本为“邮箱”
//            singleLine = true, // 设置为单行输入
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // 设置键盘类型为邮箱键盘
//            colors = TextFieldDefaults.colors( // 设置颜色
//                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
//                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
//            )
//        )
//
//        // 添加16dp的间隙
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 创建注册按钮
//        Button(
//            onClick = onRegisterClicked, // 设置点击事件为注册按钮的点击事件
//            modifier = Modifier.align(Alignment.End) // 设置对齐方式为右对齐
//        ) {
//            Text(stringResource(R.string.register_button_text)) // 设置按钮文本为“注册”
//        }
//
//        // 使用Spacer组件来添加间隙
//        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分
//    }
//}
//
//fun isValidPhone(phone: String): Boolean {
//    val phoneRegex = "1[3-9]\\d{9}".toRegex()
//    return phone.matches(phoneRegex)
//}
//
//@Composable
//fun ResetPasswordScreen() {
//    // 定义手机号、验证码、新密码和确认新密码的状态
//    var phone by remember { mutableStateOf("") }
//    var code by remember { mutableStateOf("") }
//    var newPassword by remember { mutableStateOf("") }
//    var confirmNewPassword by remember { mutableStateOf("") }
//
//    // 定义发送验证码和重置密码的点击事件
//    val onSendCodeClicked = {
//        // 验证手机号是否合法
//        if (isValidPhone(phone)) {
//            // 发送验证码到手机号
//            // TODO: 实现发送逻辑
//        } else {
//            // 手机号不合法，弹出提示
//            // TODO: 实现提示逻辑
//        }
//    }
//
//    val onResetPasswordClicked = {
//        // 验证验证码、新密码和确认新密码是否合法
//        if (code.isNotEmpty() && newPassword.isNotEmpty() && confirmNewPassword == newPassword) {
//            // 重置密码成功，跳转到登录界面
//            // TODO: 实现跳转逻辑
//        } else {
//            // 重置密码失败，弹出提示
//            // TODO: 实现提示逻辑
//        }
//    }
//
//    // 使用Column布局来垂直排列组件
//    Column(
//        modifier = Modifier
//            .fillMaxSize() // 填充满屏幕
//            .padding(16.dp) // 设置内边距为16dp
//    ) {
//        // 使用Spacer组件来添加间隙
//        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分
//
//        // 创建手机号输入框
//        TextField(
//            value = phone, // 绑定手机号状态
//            onValueChange = { phone = it }, // 当文本变化时，更新手机号状态
//            label = { Text("手机号") }, // 设置标签文本为“手机号”
//            singleLine = true, // 设置为单行输入
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // 设置键盘类型为手机号键盘
//            colors = TextFieldDefaults.colors( // 设置颜色
//                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
//                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
//            )
//        )
//
//        // 添加16dp的间隙
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 创建验证码输入框和发送验证码按钮,这两个恰好是横着的
//        Row(
//            modifier = Modifier.fillMaxWidth(), // 填充满宽度
//            horizontalArrangement = Arrangement.SpaceBetween // 水平排列方式为两端对齐
//        ) {
//            // 创建验证码输入框
//            TextField(
//                value = code, // 绑定验证码状态
//                onValueChange = { code = it }, // 当文本变化时，更新验证码状态
//                label = { Text("验证码") }, // 设置标签文本为“验证码”
//                singleLine = true, // 设置为单行输入
//                modifier = Modifier.weight(1f)
//            )
//
//            //添加 8dp 的间隔空间
//            Spacer(modifier = Modifier.width(8.dp))
//
//            //创建发送验证码按钮
//            Button(
//                onClick = onSendCodeClicked,
//                modifier = Modifier.width(80.dp)
//            ) {
//                Text(stringResource(R.string.send_verification_code_button_text))
//            }
//        }// end Row
//
//        // 同一列中继续添加 16 dp的空间
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 创建新密码输入框
//        TextField(
//            value = newPassword,
//            onValueChange = { newPassword = it },
//            label = { Text(stringResource(R.string.new_password_text)) }, // 设置标签文本为“新密码”
//            singleLine = true, // 设置为单行输入
//            visualTransformation = PasswordVisualTransformation(), // 设置文本的可视化转换为密码形式
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // 设置键盘类型为密码键盘
//            colors = TextFieldDefaults.colors( // 设置颜色
//                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
//                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
//            )
//        )
//
//        // 添加16dp的间隙
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 创建确认新密码输入框
//        TextField(
//            value = confirmNewPassword, // 绑定确认新密码状态
//            onValueChange = { confirmNewPassword = it }, // 当文本变化时，更新确认新密码状态
//            label = { Text(stringResource(R.string.confirm_new_password_text)) }, // 设置标签文本为“确认新密码”
//            singleLine = true, // 设置为单行输入
//            visualTransformation = PasswordVisualTransformation(), // 设置文本的可视化转换为密码形式
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // 设置键盘类型为密码键盘
//            colors = TextFieldDefaults.colors( // 设置颜色
//                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
//                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
//            )
//        )
//
//        // 添加16dp的间隙
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 创建重置密码按钮
//        Button(
//            onClick = onResetPasswordClicked, // 设置点击事件为重置密码的点击事件
//            modifier = Modifier.align(Alignment.End) // 设置对齐方式为右对齐
//        ) {
//            Text(stringResource(R.string.reset_password_text)) // 设置按钮文本为“重置密码”
//        }
//
//        // 使用Spacer组件来添加间隙
//        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分
//    }
//}// end Column
//@Preview
//@Composable
//fun LoginPreview() {
//    HealthappTheme {
//        LoginScreen()
//    }
//}