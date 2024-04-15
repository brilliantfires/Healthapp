package com.example.healthapp.ui.ToolClass

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.example.healthapp.data.entity.DailyActivity
import kotlinx.coroutines.flow.Flow

@Composable
fun SmallBarChartCompose(
    numberList: List<Number>,  // 接受 Number 类型的列表
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val barWidth = 40f
    Canvas(
        modifier = modifier
            .padding(end = 32.dp)
            .size(80.dp, 40.dp)
    ) {
        val maxNumber =
            numberList.maxOfOrNull { it.toDouble() } ?: 1.0  // 获取最大值以归一化高度，统一使用 Double 进行计算
        val chartHeight = size.height
        val barSpacing = 10f

        numberList.forEachIndexed { index, number ->
            val barHeight = (number.toDouble() / maxNumber) * chartHeight  // 使用toDouble确保计算的一致性
            val color = if (index < colors.size) colors[index] else Color.Blue
            val x = index * (barWidth + barSpacing)
            drawBar(x, chartHeight, barWidth, barHeight.toFloat(), color)  // 绘制条形
        }
    }
}

// 绘制单个条形的函数
private fun DrawScope.drawBar(
    x: Float,
    chartHeight: Float,
    width: Float,
    height: Float,
    color: Color,
    cornerRadius: Float = 10f  // 默认圆角大小为10
) {
    drawRoundRect(
        color = color,
        topLeft = androidx.compose.ui.geometry.Offset(x, chartHeight - height),
        size = androidx.compose.ui.geometry.Size(width, height),
        cornerRadius = CornerRadius(cornerRadius, cornerRadius)  // 应用圆角
    )
}


@Composable
fun ChartView(activitiesFlow: Flow<List<DailyActivity>>) {
    val activities by activitiesFlow.collectAsState(initial = emptyList())

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val maxSteps = activities.maxOfOrNull { it.steps } ?: 1 // Avoid division by zero
        val barWidth = size.width / activities.size
        activities.forEachIndexed { index, dailyActivity ->
            val barHeight = (dailyActivity.steps.toFloat() / maxSteps) * size.height
            drawRect(
                color = Color.Blue,
                topLeft = Offset(x = index * barWidth, y = size.height - barHeight),
                size = Size(
                    width = barWidth - 4.dp.toPx(),
                    height = barHeight
                ) // Adding some spacing between bars
            )
        }
    }
}

@Composable
fun YearlyChartView(activitiesFlow: Flow<List<DailyActivity>>) {
    val activities by activitiesFlow.collectAsState(initial = emptyList())

    // Aggregate data by month and calculate average steps
    val monthlyAverageSteps = activities
        .groupBy { it.date.month } // Group activities by month
        .map { (month, activities) ->
            month to activities.map { it.steps }.average() // Calculate average steps per month
        }.sortedBy { it.first } // Ensure the data is sorted by month

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)) {
        val maxAverage =
            monthlyAverageSteps.maxOfOrNull { it.second } ?: 1.0 // Avoid division by zero
        val barWidth = size.width / monthlyAverageSteps.size
        monthlyAverageSteps.forEachIndexed { index, (month, averageSteps) ->
            val barHeight = (averageSteps.toFloat() / maxAverage) * size.height
            drawRect(
                color = Color.Green,
                topLeft = Offset(x = index * barWidth, y = size.height - barHeight),
                size = Size(
                    width = barWidth - 4.dp.toPx(),
                    height = barHeight
                ) // Adding some spacing between bars
            )
        }
    }
}


