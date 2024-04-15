package com.example.healthapp.ui.ScreenLevel3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.R
import com.example.healthapp.data.entity.SleepRecord
import com.example.healthapp.data.viewmodel.SleepRecordViewModel
import com.example.healthapp.ui.ToolClass.CustomDateInput
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepRecordDetailsScreen(
    userId: Int,
    sleepRecordViewModel: SleepRecordViewModel,
    navController: NavHostController
) {
    val sleepRecords by sleepRecordViewModel.allSleepRecords.collectAsState(initial = listOf())
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "睡眠记录详情"
                        )
                    }
                },
                navigationIcon = {
                    Button(
                        onClick = {
                            // 点击之后使用最简单的弹出界面堆栈，实现返回
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
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(id = R.string.back_text),
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
                    IconButton(
                        onClick = { showDialog = true },
                        modifier = Modifier.width(80.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "编辑",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp, start = 32.dp, end = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "总睡眠时间",
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "日期",
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    textAlign = TextAlign.End
                )
            }

            LazyColumn {
                items(sleepRecords) { activity ->
                    SleepRecordRow(
                        sleepRecord = activity
                    )
                }
            }
        }

        if (showDialog) {
            AddSleepRecordDialog(userId, sleepRecordViewModel) { showDialog = false }
        }
    }
}

@Composable
fun AddSleepRecordDialog(
    userId: Int,
    sleepRecordViewModel: SleepRecordViewModel,
    onDismiss: () -> Unit
) {
    val (hours, setHours) = remember { mutableStateOf("") }
    val (minutes, setMinutes) = remember { mutableStateOf("") }
    val (seconds, setSeconds) = remember { mutableStateOf("") }
    val dateText = remember { mutableStateOf("") }
    val totalDuration = remember { mutableStateOf("") }
    LaunchedEffect(hours, minutes, seconds) {
        totalDuration.value = "$hours" + "小时" + "${minutes.padStart(2, '0')}" + "分钟" + "${
            seconds.padStart(
                2,
                '0'
            )
        }" + "秒"
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "添加总睡眠时间") },
        // 输入运动时间和日期
        text = {
            Column {
                // 小时输入
                OutlinedTextField(
                    value = hours,
                    onValueChange = { newValue ->
                        if (newValue.length <= 2 && newValue.all { it.isDigit() }) {
                            setHours(newValue)
                        }
                    },
                    label = { Text("小时") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(IntrinsicSize.Min)
                )

                // 分钟输入
                OutlinedTextField(
                    value = minutes,
                    onValueChange = { newValue ->
                        if (newValue.length <= 2 && newValue.all { it.isDigit() } && (newValue.toIntOrNull()
                                ?: 0) in 0..59) {
                            setMinutes(newValue)
                        }
                    },
                    label = { Text("分钟") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(IntrinsicSize.Min)
                )

                // 秒输入
                OutlinedTextField(
                    value = seconds,
                    onValueChange = { newValue ->
                        if (newValue.length <= 2 && newValue.all { it.isDigit() } && (newValue.toIntOrNull()
                                ?: 0) in 0..59) {
                            setSeconds(newValue)
                        }
                    },
                    label = { Text("秒") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(IntrinsicSize.Min)
                )

                CustomDateInput(dateText = dateText)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val date =
                        LocalDate.parse(dateText.value, DateTimeFormatter.ISO_DATE).atStartOfDay()
                    sleepRecordViewModel.addOrUpdateTotalDuration(
                        userId,
                        date,
                        totalDuration.value
                    )
                    onDismiss()
                }) {
                Text(text = "确认")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(id = R.string.cancel_text),
                    fontSize = 16.sp
                )
            }
        }
    )
}

@Composable
fun SleepRecordRow(sleepRecord: SleepRecord) {
    val totalDuration by remember {
        mutableStateOf(sleepRecord.totalDuration)
    }
    val date by remember {
        mutableStateOf(sleepRecord.date)
    }
    // 创建一个状态来控制对话框的显示
    val showDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .clickable { showDialog.value = true },

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = totalDuration ?: "暂时无睡眠信息",
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
            Text(
                text = date.format(DateTimeFormatter.ISO_DATE),
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                textAlign = TextAlign.End
            )
        }
    }

    // 根据showDialog的状态决定是否展示对话框
    if (showDialog.value) {
        ShowSleepDetailDialog(sleepRecord = sleepRecord) {
            // 当对话框的关闭按钮被点击时，将showDialog设置为false
            showDialog.value = false
        }
    }
}

@Composable
fun ShowSleepDetailDialog(sleepRecord: SleepRecord, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Column {
                Text(
                    text = "睡眠详情",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Divider(
                    color = Color.Gray, // 设置分隔线的颜色
                    thickness = 1.dp, // 设置分隔线的厚度
                    modifier = Modifier.padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 0.dp,
                        bottom = 16.dp
                    ) // 添加内边距使内容不会贴近边缘
                    .fillMaxWidth(), // 确保列填满最大宽度
                verticalArrangement = Arrangement.spacedBy(8.dp) // 项与项之间添加间距
            ) {
                Text(
                    text = "总睡眠时间: ${sleepRecord.totalDuration}",
                    style = TextStyle(
                        fontSize = 18.sp, // 增加字体大小
                        fontWeight = FontWeight.Medium // 设置字体权重
                    )
                )
                Text(
                    text = "深度睡眠时间: ${sleepRecord.deepSleep}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "浅睡时间: ${sleepRecord.lightSleep}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "快速眼动时间: ${sleepRecord.remSleep}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "清醒时间: ${sleepRecord.awakeDuration}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("关闭")
            }
        }
    )
}
