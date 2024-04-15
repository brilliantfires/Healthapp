package com.example.healthapp.ui.ToolClass

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// 用于在输入框中输入年月日
@Composable
fun CustomDateInput(dateText: MutableState<String>) {
    val (year, setYear) = remember { mutableStateOf("") }
    val (month, setMonth) = remember { mutableStateOf("") }
    val (day, setDay) = remember { mutableStateOf("") }

    // 更新整个日期状态
    LaunchedEffect(year, month, day) {
        dateText.value = "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
    }

    Row {
        // 年份输入
        OutlinedTextField(
            value = year,
            onValueChange = { newValue ->
                if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                    setYear(newValue)
                }
            },
            label = { Text("年") },
            modifier = Modifier.width(100.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 月份输入
        OutlinedTextField(
            value = month,
            onValueChange = { newValue ->
                if (newValue.length <= 2 && newValue.all { it.isDigit() } && (newValue.toIntOrNull()
                        ?: 0) <= 12) {
                    setMonth(newValue)
                }
            },
            label = { Text("月") },
            modifier = Modifier.width(80.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = { setMonth(month.padStart(2, '0')) })
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 日输入
        OutlinedTextField(
            value = day,
            onValueChange = { newValue ->
                if (newValue.length <= 2 && newValue.all { it.isDigit() } && (newValue.toIntOrNull()
                        ?: 0) <= 31) {
                    setDay(newValue)
                }
            },
            label = { Text("日") },
            modifier = Modifier.width(80.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(onDone = { setDay(day.padStart(2, '0')) })
        )
    }
}