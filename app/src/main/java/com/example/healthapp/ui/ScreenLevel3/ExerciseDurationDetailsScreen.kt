package com.example.healthapp.ui.ScreenLevel3

import androidx.compose.foundation.background
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
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.ui.ToolClass.CustomDateInput
import com.example.healthapp.ui.ToolClass.formatTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDurationDetailsScreen(
    userId: Int,
    dailyActivityViewModel: DailyActivityViewModel,
    navController: NavHostController
) {
    val dailyActivity by dailyActivityViewModel.getActivitiesByUserId(userId)
        .collectAsState(initial = listOf())
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text = "跑步时间详情")
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
                                tint = Color.Blue
                            )
                            Text(
                                text = stringResource(id = R.string.back_text),
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
                        onClick = { showDialog = true },
                        modifier = Modifier.width(80.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "编辑",
                            tint = Color.Blue
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
                    text = "跑步时间",
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
                items(dailyActivity) { activity ->
                    activity.exerciseDuration?.let {
                        ExerciseDurationRow(
                            exerciseDuration = it,
                            date = activity.date.format(dateFormatter)
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AddExerciseDurationDialog(userId, dailyActivityViewModel) { showDialog = false }
        }
    }
}

@Composable
fun AddExerciseDurationDialog(
    userId: Int,
    dailyActivityViewModel: DailyActivityViewModel,
    onDismiss: () -> Unit
) {
    val (hours, setHours) = remember { mutableStateOf("") }
    val (minutes, setMinutes) = remember { mutableStateOf("") }
    val (seconds, setSeconds) = remember { mutableStateOf("") }
    val dateText = remember { mutableStateOf("") }
    val exerciseDuration = remember { mutableStateOf("") }
    LaunchedEffect(hours, minutes, seconds) {
        exerciseDuration.value = "$hours:${minutes.padStart(2, '0')}:${seconds.padStart(2, '0')}"
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "添加跑步时间") },
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
                    label = { Text("小时 (HH)") },
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
                    label = { Text("分钟 (MM)") },
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
                    label = { Text("秒 (SS)") },
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
                    dailyActivityViewModel.addOrUpdateExerciseDuration(
                        userId,
                        date,
                        exerciseDuration.value
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
fun ExerciseDurationRow(exerciseDuration: String, date: String) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatTime(exerciseDuration),
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
            Text(
                text = date,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                textAlign = TextAlign.End
            )
        }
    }
}
