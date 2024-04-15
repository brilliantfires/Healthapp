package com.example.healthapp.ui.ScreenLevel3

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// 该类用于显示历史步数数据的界面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepDetailsScreen(
    userId: Int,
    dailyActivityViewModel: DailyActivityViewModel,
    navController: NavHostController
) {
    val dailyActivity by dailyActivityViewModel.getActivitiesByUserId(userId)
        .collectAsState(initial = null)
    // 用于记住弹开输入框的状态，不至于因为状态变化又恢复false
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(id = R.string.all_step_data_text))
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
                            contentDescription = stringResource(id = R.string.edit_text),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) { // 使用Column包裹
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            // 这个Row包含步数和日期的标题，应始终显示
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp, start = 32.dp, end = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.step_numbers),
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = stringResource(id = R.string.date_text),
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.End
                )
            }

            // 这里是实际显示步数数据的LazyColumn
            dailyActivity?.let { activities ->
                LazyColumn {
                    items(activities) { activity ->
                        activity.steps?.let {
                            StepRow(
                                stepCount = it,
                                date = activity.date.format(dateFormatter)
                            )
                        }
                    }
                }
            }
        }

        if (showDialog) {
            // 弹出窗口逻辑，用于添加步数和日期
            AddStepsDialog(userId, dailyActivityViewModel, onDismiss = { showDialog = false })
        }
    }

}

@Composable
fun AddStepsDialog(
    userId: Int,
    dailyActivityViewModel: DailyActivityViewModel,
    onDismiss: () -> Unit
) {
    // 使用 remember 保存日期和步数状态
    val stepCount = remember { mutableStateOf("") }
    val dateText = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.add_stepData_text),
                color = Color.Black,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column {
                // 步数输入
                OutlinedTextField(
                    value = stepCount.value,
                    onValueChange = { newValue ->
                        // 只保留输入值中的数字字符
                        stepCount.value = newValue.filter { it.isDigit() }
                    },
                    label = { Text(stringResource(id = R.string.input_steps_text)) },
                    // 控制只弹出数字框
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                // 日期输入
                CustomDateInput(dateText = dateText)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // 在这里处理确认逻辑
                    val steps = stepCount.value.toIntOrNull() ?: 0
                    val date =
                        LocalDate.parse(dateText.value, DateTimeFormatter.ISO_DATE).atStartOfDay()
                    dailyActivityViewModel.addOrUpdateSteps(userId, date, steps)
                    onDismiss() // 关闭对话框
                }) {
                Text(
                    text = stringResource(id = R.string.confirm_text),
                    fontSize = 16.sp
                )
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
fun StepRow(stepCount: Int, date: String) {
    Card(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "$stepCount",
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Start
            )
            Text(
                text = date,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.End
            )
        }
    }
}

