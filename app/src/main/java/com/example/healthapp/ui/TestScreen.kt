package com.example.healthapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TestScreen() {
    // 假设有一些步骤数据和颜色
    val steps = listOf(150, 200, 100, 300)
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Magenta)

    // BarChartCompose(steps, colors)
}
