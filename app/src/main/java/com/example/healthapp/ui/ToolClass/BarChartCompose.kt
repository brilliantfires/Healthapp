package com.example.healthapp.ui.ToolClass

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthapp.data.entity.DailyActivity
import com.example.healthapp.data.entity.SleepRecord
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

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
fun StepWeekChartView(data: List<DailyActivity>) {
    val maxValue = data.maxOfOrNull { it.steps ?: 0 }?.toFloat() ?: 1f // 获取最大步数以归一化高度
    val barWidth = 40.dp
    val barSpacing = 8.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
        // .padding(16.dp)
    ) {
        val chartHeight = size.height
        val barWidthPx = barWidth.toPx()
        val barSpacingPx = barSpacing.toPx()

        data.forEachIndexed { index, activity ->
            val steps = activity.steps ?: 0
            val date = activity.date.format(DateTimeFormatter.ofPattern("M/d")) // 格式化日期
            val barHeight = (steps / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f // 留出空间显示日期和步数

            val x = index * (barWidthPx + barSpacingPx)

            // 绘制条形
            drawRoundRect(
                color = Color.Red,
                topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                size = Size(barWidthPx, barHeight),
                cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
            )

            // 绘制步数
            drawContext.canvas.nativeCanvas.drawText(
                steps.toString(),
                x + barWidthPx / 2,
                chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 14.sp.toPx()
                }
            )

            // 绘制日期
            drawContext.canvas.nativeCanvas.drawText(
                date,
                x + barWidthPx / 2,
                chartHeight - 10.dp.toPx(), // 日期显示在下方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 12.sp.toPx()
                }
            )
        }
    }
}


@Composable
fun StepMonthChartView(data: List<DailyActivity>) {
    // 确定展示日期范围：从今天向前数30天
    val today = LocalDateTime.now()
    val startOfPeriod = today.minusDays(29).toLocalDate()  // 向前数30天（包含今天）

    // 初始化数据映射，每天的步数默认为0
    val dailySteps = mutableMapOf<LocalDate, Int>()
    for (i in 0 until 30) {  // 循环30天
        dailySteps[startOfPeriod.plusDays(i.toLong())] = 0
    }

    // 填充实际数据
    data.filter { it.date.toLocalDate() in startOfPeriod..today.toLocalDate() }
        .forEach { activity ->
            dailySteps[activity.date.toLocalDate()] = activity.steps ?: 0
        }

    val sortedData = dailySteps.entries.sortedBy { it.key }  // 按日期排序

    // 获取最大步数以归一化高度
    val maxValue = sortedData.maxOfOrNull { it.value }?.toFloat() ?: 1f  // 使用最大步数归一化

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 30  // 固定为30天的数据宽度

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            sortedData.forEachIndexed { index, entry ->
                val (date, steps) = entry
                val barHeight =
                    (steps.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f  // 为日期和步数留出空间

                val x = index * (barWidthPx + barSpacingPx)

                // 绘制条形
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
                )

                // 绘制步数
                drawContext.canvas.nativeCanvas.drawText(
                    steps.toString(),
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()
                    }
                )

                // 绘制日期
                val dateString = date.format(DateTimeFormatter.ofPattern("M/d"))
                drawContext.canvas.nativeCanvas.drawText(
                    dateString,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(), // 日期显示在下方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()
                    }
                )
            }
        }
    }
}


@Composable
fun StepYearlyChartView(data: List<DailyActivity>) {
    // 初始化一个Map，其中键是月份，值是步数列表
    val activitiesByMonth = (1..12).associateWith { mutableListOf<Int>() }

    // 填充步数列表
    for (activity in data) {
        val month = activity.date.monthValue
        activitiesByMonth[month]?.add(activity.steps ?: 0)
    }

    // 计算每个月的平均步数
    val monthlyAverages = activitiesByMonth.map {
        //month是键，stepsList是值
            (month, stepsList) ->
        val averageSteps = if (stepsList.isNotEmpty()) stepsList.average().toInt() else 0
        // 返回的新map，任然是键 值结构
        month to averageSteps  // 将月份和平均步数作为键值对存储
    }

    // 找到最大平均步数以归一化高度
    val maxValue = monthlyAverages.maxOfOrNull { it.second }?.toFloat() ?: 1f

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 12  // 总是显示12个月

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            // chartHeight 是表格高度的意思？
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            monthlyAverages.forEachIndexed { index, (month, averageSteps) ->
                // barHeight 代表是条的高度
                val barHeight =
                    (averageSteps.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f
                val x = index * (barWidthPx + barSpacingPx)
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()),
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)
                )
                // 显示月份，格式化为“MMM”（如Jan, Feb, ...）
                val monthLabel =
                    YearMonth.of(2020, month).format(DateTimeFormatter.ofPattern("MMM"))
                drawContext.canvas.nativeCanvas.drawText(
                    monthLabel,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(),  // 增加一点垂直空间以避免文字与条形图重叠
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()  // 调整文本大小
                    }
                )
                // 显示平均步数
                drawContext.canvas.nativeCanvas.drawText(
                    averageSteps.toString(),
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(),  // 在条形图上方留出空间显示步数
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()  // 调整文本大小
                    }
                )
            }
        }
    }
}

@Composable
fun FloorWeekChartView(data: List<DailyActivity>) {
    val maxValue = data.maxOfOrNull { it.floorsClimbed ?: 0 }?.toFloat() ?: 1f // 获取最大步数以归一化高度
    val barWidth = 40.dp
    val barSpacing = 8.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
        // .padding(16.dp)
    ) {
        val chartHeight = size.height
        val barWidthPx = barWidth.toPx()
        val barSpacingPx = barSpacing.toPx()

        data.forEachIndexed { index, activity ->
            // 根据data的数据，逐个生成条形图
            val floors = activity.floorsClimbed ?: 0
            val date = activity.date.format(DateTimeFormatter.ofPattern("M/d")) // 格式化日期
            val barHeight =
                (floors / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f // 留出空间显示日期和步数

            val x = index * (barWidthPx + barSpacingPx)

            // 绘制条形
            drawRoundRect(
                color = Color.Red,
                topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                size = Size(barWidthPx, barHeight),
                cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
            )

            // 绘制步数
            drawContext.canvas.nativeCanvas.drawText(
                floors.toString(),
                x + barWidthPx / 2,
                chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 14.sp.toPx()
                }
            )

            // 绘制日期
            drawContext.canvas.nativeCanvas.drawText(
                date,
                x + barWidthPx / 2,
                chartHeight - 10.dp.toPx(), // 日期显示在下方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 12.sp.toPx()
                }
            )
        }
    }
}


@Composable
fun FloorMonthChartView(data: List<DailyActivity>) {
    // 确定展示日期范围：从今天向前数30天
    val today = LocalDateTime.now()
    val startOfPeriod = today.minusDays(29).toLocalDate()  // 向前数30天（包含今天）

    // 初始化数据映射，每天的步数默认为0
    val dailyFloors = mutableMapOf<LocalDate, Int>()
    for (i in 0 until 30) {  // 循环30天
        dailyFloors[startOfPeriod.plusDays(i.toLong())] = 0
    }

    // 填充实际数据
    data.filter { it.date.toLocalDate() in startOfPeriod..today.toLocalDate() }
        .forEach { activity ->
            dailyFloors[activity.date.toLocalDate()] = activity.floorsClimbed ?: 0
        }

    // 按日期排序
    val sortedData = dailyFloors.entries.sortedBy { it.key }

    // 获取最大步数以归一化高度
    val maxValue = sortedData.maxOfOrNull { it.value }?.toFloat() ?: 1f  // 使用最大步数归一化

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 30  // 固定为30天的数据宽度

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            sortedData.forEachIndexed { index, entry ->
                val (date, floors) = entry
                val barHeight =
                    (floors.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f  // 为日期和步数留出空间

                val x = index * (barWidthPx + barSpacingPx)

                // 绘制条形
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
                )

                // 绘制步数
                drawContext.canvas.nativeCanvas.drawText(
                    floors.toString(),
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()
                    }
                )

                // 绘制日期
                val dateString = date.format(DateTimeFormatter.ofPattern("M/d"))
                drawContext.canvas.nativeCanvas.drawText(
                    dateString,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(), // 日期显示在下方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()
                    }
                )
            }
        }
    }
}


@Composable
fun FloorYearlyChartView(data: List<DailyActivity>) {
    // 初始化一个Map，其中键是月份，值是步数列表
    val activitiesByMonth = (1..12).associateWith { mutableListOf<Int>() }

    // 填充步数列表
    for (activity in data) {
        val month = activity.date.monthValue
        activitiesByMonth[month]?.add(activity.floorsClimbed ?: 0)
    }

    // 计算每个月的平均步数
    val monthlyAverages = activitiesByMonth.map {
        //month是键，stepsList是值
            (month, stepsList) ->
        val averageSteps = if (stepsList.isNotEmpty()) stepsList.average().toInt() else 0
        // 返回的新map，任然是键 值结构
        month to averageSteps  // 将月份和平均步数作为键值对存储
    }

    // 找到最大平均步数以归一化高度
    val maxValue = monthlyAverages.maxOfOrNull { it.second }?.toFloat() ?: 1f

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 12  // 总是显示12个月

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            // chartHeight 是表格高度的意思？
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            monthlyAverages.forEachIndexed { index, (month, averageFloors) ->
                // barHeight 代表是条的高度
                val barHeight =
                    (averageFloors.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f
                val x = index * (barWidthPx + barSpacingPx)
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()),
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)
                )
                // 显示月份，格式化为“MMM”（如Jan, Feb, ...）
                val monthLabel =
                    YearMonth.of(2020, month).format(DateTimeFormatter.ofPattern("MMM"))
                drawContext.canvas.nativeCanvas.drawText(
                    monthLabel,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(),  // 增加一点垂直空间以避免文字与条形图重叠
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()  // 调整文本大小
                    }
                )
                // 显示平均步数
                drawContext.canvas.nativeCanvas.drawText(
                    averageFloors.toString(),
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(),  // 在条形图上方留出空间显示步数
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()  // 调整文本大小
                    }
                )
            }
        }
    }
}

@Composable
fun EnergyWeekChartView(data: List<DailyActivity>) {
    val maxValue = data.maxOfOrNull { it.energyExpenditure ?: 0.0 }?.toFloat() ?: 1f // 获取最大步数以归一化高度
    val barWidth = 40.dp
    val barSpacing = 8.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(start = 4.dp, end = 4.dp)
    ) {
        val chartHeight = size.height
        val barWidthPx = barWidth.toPx()
        val barSpacingPx = barSpacing.toPx()

        data.forEachIndexed { index, activity ->
            // 根据data的数据，逐个生成条形图
            val energyExpenditure = activity.energyExpenditure ?: 0.0
            val date = activity.date.format(DateTimeFormatter.ofPattern("M/d")) // 格式化日期
            val barHeight =
                (energyExpenditure / maxValue).toFloat() * (chartHeight - 50.dp.toPx()) * 0.95f // 留出空间显示日期和步数

            val x = index * (barWidthPx + barSpacingPx)

            // 绘制条形
            drawRoundRect(
                color = Color.Red,
                topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                size = Size(barWidthPx, barHeight),
                cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
            )

            // 绘制步数
            drawContext.canvas.nativeCanvas.drawText(
                energyExpenditure.toString(),
                x + barWidthPx / 2,
                chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 14.sp.toPx()
                }
            )

            // 绘制日期
            drawContext.canvas.nativeCanvas.drawText(
                date,
                x + barWidthPx / 2,
                chartHeight - 10.dp.toPx(), // 日期显示在下方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 12.sp.toPx()
                }
            )
        }
    }
}


@Composable
fun EnergyMonthChartView(data: List<DailyActivity>) {
    // 确定展示日期范围：从今天向前数30天
    val today = LocalDateTime.now()
    val startOfPeriod = today.minusDays(29).toLocalDate()  // 向前数30天（包含今天）

    // 初始化数据映射，每天的步数默认为0
    val dailyEnergyExpenditure = mutableMapOf<LocalDate, Double>()
    for (i in 0 until 30) {  // 循环30天
        dailyEnergyExpenditure[startOfPeriod.plusDays(i.toLong())] = 0.0
    }

    // 填充实际数据
    data.filter { it.date.toLocalDate() in startOfPeriod..today.toLocalDate() }
        .forEach { activity ->
            dailyEnergyExpenditure[activity.date.toLocalDate()] = activity.energyExpenditure ?: 0.0
        }

    // 按日期排序
    val sortedData = dailyEnergyExpenditure.entries.sortedBy { it.key }

    // 获取最大步数以归一化高度
    val maxValue = sortedData.maxOfOrNull { it.value }?.toFloat() ?: 1f  // 使用最大步数归一化

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 30  // 固定为30天的数据宽度

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
                .padding(start = 4.dp, end = 4.dp)
        ) {
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            sortedData.forEachIndexed { index, entry ->
                val (date, value) = entry
                val barHeight =
                    (value.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f  // 为日期和步数留出空间

                val x = index * (barWidthPx + barSpacingPx)

                // 绘制条形
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
                )

                // 绘制步数
                drawContext.canvas.nativeCanvas.drawText(
                    value.toString(),
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()
                    }
                )

                // 绘制日期
                val dateString = date.format(DateTimeFormatter.ofPattern("M/d"))
                drawContext.canvas.nativeCanvas.drawText(
                    dateString,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(), // 日期显示在下方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()
                    }
                )
            }
        }
    }
}


@Composable
fun EnergyYearlyChartView(data: List<DailyActivity>) {
    // 初始化一个Map，其中键是月份，值是步数列表
    val activitiesByMonth = (1..12).associateWith { mutableListOf<Double>() }

    // 填充步数列表
    for (activity in data) {
        val month = activity.date.monthValue
        activitiesByMonth[month]?.add(activity.energyExpenditure ?: 0.0)
    }

    // 计算每个月的平均步数
    val monthlyAverages = activitiesByMonth.map {
        //month是键，valuesList
            (month, valuesList) ->
        val averageValues = if (valuesList.isNotEmpty()) valuesList.average().toDouble() else 0.0
        // 返回的新map，任然是键 值结构
        month to averageValues  // 将月份和平均步数作为键值对存储
    }

    // 找到最大平均步数以归一化高度
    val maxValue = monthlyAverages.maxOfOrNull { it.second }?.toFloat() ?: 1f

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 12  // 总是显示12个月

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
                .padding(start = 4.dp, end = 4.dp)
        ) {
            // chartHeight 是表格高度的意思？
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            monthlyAverages.forEachIndexed { index, (month, averageValues) ->
                // barHeight 代表是条的高度
                val barHeight =
                    (averageValues.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f
                val x = index * (barWidthPx + barSpacingPx)
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()),
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)
                )
                // 显示月份，格式化为“MMM”（如Jan, Feb, ...）
                val monthLabel =
                    YearMonth.of(2020, month).format(DateTimeFormatter.ofPattern("MMM"))
                drawContext.canvas.nativeCanvas.drawText(
                    monthLabel,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(),  // 增加一点垂直空间以避免文字与条形图重叠
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()  // 调整文本大小
                    }
                )
                // 显示平均步数
                drawContext.canvas.nativeCanvas.drawText(
                    String.format("%.2f", averageValues),
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(),  // 在条形图上方留出空间显示步数
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()  // 调整文本大小
                    }
                )
            }
        }
    }
}

@Composable
fun RunningDistanceWeekChartView(data: List<DailyActivity>) {
    val maxValue = data.maxOfOrNull { it.runningDistance ?: 0.0 }?.toFloat() ?: 1f // 获取最大步数以归一化高度
    val barWidth = 40.dp
    val barSpacing = 8.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
        // .padding(16.dp)
    ) {
        val chartHeight = size.height
        val barWidthPx = barWidth.toPx()
        val barSpacingPx = barSpacing.toPx()

        data.forEachIndexed { index, activity ->
            // 根据data的数据，逐个生成条形图
            val theValue = activity.runningDistance ?: 0.0
            val date = activity.date.format(DateTimeFormatter.ofPattern("M/d")) // 格式化日期
            val barHeight =
                (theValue / maxValue).toFloat() * (chartHeight - 50.dp.toPx()) * 0.95f // 留出空间显示日期和步数

            val x = index * (barWidthPx + barSpacingPx)

            // 绘制条形
            drawRoundRect(
                color = Color.Red,
                topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                size = Size(barWidthPx, barHeight),
                cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
            )

            // 绘制步数
            drawContext.canvas.nativeCanvas.drawText(
                theValue.toString(),
                x + barWidthPx / 2,
                chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 14.sp.toPx()
                }
            )

            // 绘制日期
            drawContext.canvas.nativeCanvas.drawText(
                date,
                x + barWidthPx / 2,
                chartHeight - 10.dp.toPx(), // 日期显示在下方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 12.sp.toPx()
                }
            )
        }
    }
}


@Composable
fun RunningDistanceMonthChartView(data: List<DailyActivity>) {
    // 确定展示日期范围：从今天向前数30天
    val today = LocalDateTime.now()
    val startOfPeriod = today.minusDays(29).toLocalDate()  // 向前数30天（包含今天）

    // 初始化数据映射，每天的步数默认为0
    val valueMap = mutableMapOf<LocalDate, Double>()
    for (i in 0 until 30) {  // 循环30天
        valueMap[startOfPeriod.plusDays(i.toLong())] = 0.0
    }

    // 填充实际数据
    data.filter { it.date.toLocalDate() in startOfPeriod..today.toLocalDate() }
        .forEach { activity ->
            valueMap[activity.date.toLocalDate()] = activity.runningDistance ?: 0.0
        }

    // 按日期排序
    val sortedData = valueMap.entries.sortedBy { it.key }

    // 获取最大步数以归一化高度
    val maxValue = sortedData.maxOfOrNull { it.value }?.toFloat() ?: 1f  // 使用最大步数归一化

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 30  // 固定为30天的数据宽度

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            sortedData.forEachIndexed { index, entry ->
                val (date, value) = entry
                val barHeight =
                    (value.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f  // 为日期和步数留出空间

                val x = index * (barWidthPx + barSpacingPx)

                // 绘制条形
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
                )

                // 绘制步数
                drawContext.canvas.nativeCanvas.drawText(
                    value.toString(),
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()
                    }
                )

                // 绘制日期
                val dateString = date.format(DateTimeFormatter.ofPattern("M/d"))
                drawContext.canvas.nativeCanvas.drawText(
                    dateString,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(), // 日期显示在下方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()
                    }
                )
            }
        }
    }
}


@Composable
fun RunningDistanceYearlyChartView(data: List<DailyActivity>) {
    // 初始化一个Map，其中键是月份，值是步数列表
    val activitiesByMonth = (1..12).associateWith { mutableListOf<Double>() }

    // 填充步数列表
    for (activity in data) {
        val month = activity.date.monthValue
        activitiesByMonth[month]?.add(activity.runningDistance ?: 0.0)
    }

    // 计算每个月的平均步数
    val monthlyAverages = activitiesByMonth.map {
        //month是键，valuesList
            (month, valuesList) ->
        val averageValues = if (valuesList.isNotEmpty()) valuesList.average().toDouble() else 0.0
        // 返回的新map，任然是键 值结构
        month to averageValues  // 将月份和平均步数作为键值对存储
    }

    // 找到最大平均步数以归一化高度
    val maxValue = monthlyAverages.maxOfOrNull { it.second }?.toFloat() ?: 1f

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 12  // 总是显示12个月

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            // chartHeight 是表格高度的意思？
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            monthlyAverages.forEachIndexed { index, (month, averageValues) ->
                // barHeight 代表是条的高度
                val barHeight =
                    (averageValues.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f
                val x = index * (barWidthPx + barSpacingPx)
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()),
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)
                )
                // 显示月份，格式化为“MMM”（如Jan, Feb, ...）
                val monthLabel =
                    YearMonth.of(2020, month).format(DateTimeFormatter.ofPattern("MMM"))
                drawContext.canvas.nativeCanvas.drawText(
                    monthLabel,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(),  // 增加一点垂直空间以避免文字与条形图重叠
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()  // 调整文本大小
                    }
                )
                // 显示平均步数
                drawContext.canvas.nativeCanvas.drawText(
                    String.format("%.2f", averageValues),
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(),  // 在条形图上方留出空间显示步数
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()  // 调整文本大小
                    }
                )
            }
        }
    }
}

@Composable
fun WalkingDistanceWeekChartView(data: List<DailyActivity>) {
    val maxValue = data.maxOfOrNull { it.walkingDistance ?: 0.0 }?.toFloat() ?: 1f // 获取最大步数以归一化高度
    val barWidth = 40.dp
    val barSpacing = 8.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
        // .padding(16.dp)
    ) {
        val chartHeight = size.height
        val barWidthPx = barWidth.toPx()
        val barSpacingPx = barSpacing.toPx()

        data.forEachIndexed { index, activity ->
            // 根据data的数据，逐个生成条形图
            val theValue = activity.walkingDistance ?: 0.0
            val date = activity.date.format(DateTimeFormatter.ofPattern("M/d")) // 格式化日期
            val barHeight =
                (theValue / maxValue).toFloat() * (chartHeight - 50.dp.toPx()) * 0.95f // 留出空间显示日期和步数

            val x = index * (barWidthPx + barSpacingPx)

            // 绘制条形
            drawRoundRect(
                color = Color.Red,
                topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                size = Size(barWidthPx, barHeight),
                cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
            )

            // 绘制步数
            drawContext.canvas.nativeCanvas.drawText(
                theValue.toString(),
                x + barWidthPx / 2,
                chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 14.sp.toPx()
                }
            )

            // 绘制日期
            drawContext.canvas.nativeCanvas.drawText(
                date,
                x + barWidthPx / 2,
                chartHeight - 10.dp.toPx(), // 日期显示在下方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 12.sp.toPx()
                }
            )
        }
    }
}


@Composable
fun WalkingDistanceMonthChartView(data: List<DailyActivity>) {
    // 确定展示日期范围：从今天向前数30天
    val today = LocalDateTime.now()
    val startOfPeriod = today.minusDays(29).toLocalDate()  // 向前数30天（包含今天）

    // 初始化数据映射，每天的步数默认为0
    val valueMap = mutableMapOf<LocalDate, Double>()
    for (i in 0 until 30) {  // 循环30天
        valueMap[startOfPeriod.plusDays(i.toLong())] = 0.0
    }

    // 填充实际数据
    data.filter { it.date.toLocalDate() in startOfPeriod..today.toLocalDate() }
        .forEach { activity ->
            valueMap[activity.date.toLocalDate()] = activity.walkingDistance ?: 0.0
        }

    // 按日期排序
    val sortedData = valueMap.entries.sortedBy { it.key }

    // 获取最大步数以归一化高度
    val maxValue = sortedData.maxOfOrNull { it.value }?.toFloat() ?: 1f  // 使用最大步数归一化

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 30  // 固定为30天的数据宽度

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            sortedData.forEachIndexed { index, entry ->
                val (date, value) = entry
                val barHeight =
                    (value.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f  // 为日期和步数留出空间

                val x = index * (barWidthPx + barSpacingPx)

                // 绘制条形
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
                )

                // 绘制步数
                drawContext.canvas.nativeCanvas.drawText(
                    value.toString(),
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()
                    }
                )

                // 绘制日期
                val dateString = date.format(DateTimeFormatter.ofPattern("M/d"))
                drawContext.canvas.nativeCanvas.drawText(
                    dateString,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(), // 日期显示在下方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()
                    }
                )
            }
        }
    }
}


@Composable
fun WalkingDistanceYearlyChartView(data: List<DailyActivity>) {
    // 初始化一个Map，其中键是月份，值是步数列表
    val activitiesByMonth = (1..12).associateWith { mutableListOf<Double>() }

    // 填充步数列表
    for (activity in data) {
        val month = activity.date.monthValue
        activitiesByMonth[month]?.add(activity.walkingDistance ?: 0.0)
    }

    // 计算每个月的平均步数
    val monthlyAverages = activitiesByMonth.map {
        //month是键，valuesList
            (month, valuesList) ->
        val averageValues = if (valuesList.isNotEmpty()) valuesList.average().toDouble() else 0.0
        // 返回的新map，任然是键 值结构
        month to averageValues  // 将月份和平均步数作为键值对存储
    }

    // 找到最大平均步数以归一化高度
    val maxValue = monthlyAverages.maxOfOrNull { it.second }?.toFloat() ?: 1f

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 12  // 总是显示12个月

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            // chartHeight 是表格高度的意思？
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            monthlyAverages.forEachIndexed { index, (month, averageValues) ->
                // barHeight 代表是条的高度
                val barHeight =
                    (averageValues.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f
                val x = index * (barWidthPx + barSpacingPx)
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()),
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)
                )
                // 显示月份，格式化为“MMM”（如Jan, Feb, ...）
                val monthLabel =
                    YearMonth.of(2020, month).format(DateTimeFormatter.ofPattern("MMM"))
                drawContext.canvas.nativeCanvas.drawText(
                    monthLabel,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(),  // 增加一点垂直空间以避免文字与条形图重叠
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()  // 调整文本大小
                    }
                )
                // 显示平均步数
                drawContext.canvas.nativeCanvas.drawText(
                    String.format("%.2f", averageValues),
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(),  // 在条形图上方留出空间显示步数
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()  // 调整文本大小
                    }
                )
            }
        }
    }
}

@Composable
fun ExerciseDurationWeekChartView(data: List<DailyActivity>) {
    val maxValue =
        data.maxOfOrNull { convertTimeToHours(time = it.exerciseDuration ?: "0:0:0") }?.toFloat()
            ?: 1f // 获取最大步数以归一化高度
    val barWidth = 40.dp
    val barSpacing = 8.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
        // .padding(16.dp)
    ) {
        val chartHeight = size.height
        val barWidthPx = barWidth.toPx()
        val barSpacingPx = barSpacing.toPx()

        data.forEachIndexed { index, activity ->
            // 根据data的数据，逐个生成条形图
            val theValue = convertTimeToHours(time = activity.exerciseDuration ?: "0:0:0")
            val date = activity.date.format(DateTimeFormatter.ofPattern("M/d")) // 格式化日期
            val barHeight =
                (theValue / maxValue).toFloat() * (chartHeight - 50.dp.toPx()) * 0.95f // 留出空间显示日期和步数

            val x = index * (barWidthPx + barSpacingPx)

            // 绘制条形
            drawRoundRect(
                color = Color.Red,
                topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                size = Size(barWidthPx, barHeight),
                cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
            )

            // 绘制步数
            drawContext.canvas.nativeCanvas.drawText(
                theValue.toString() + "H",
                x + barWidthPx / 2,
                chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 14.sp.toPx()
                }
            )

            // 绘制日期
            drawContext.canvas.nativeCanvas.drawText(
                date,
                x + barWidthPx / 2,
                chartHeight - 10.dp.toPx(), // 日期显示在下方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 12.sp.toPx()
                }
            )
        }
    }
}


@Composable
fun ExerciseDurationMonthChartView(data: List<DailyActivity>) {
    // 确定展示日期范围：从今天向前数30天
    val today = LocalDateTime.now()
    val startOfPeriod = today.minusDays(29).toLocalDate()  // 向前数30天（包含今天）

    // 初始化数据映射，每天的步数默认为0
    val valueMap = mutableMapOf<LocalDate, Double>()
    for (i in 0 until 30) {  // 循环30天
        valueMap[startOfPeriod.plusDays(i.toLong())] = 0.0
    }

    // 填充实际数据
    data.filter { it.date.toLocalDate() in startOfPeriod..today.toLocalDate() }
        .forEach { activity ->
            valueMap[activity.date.toLocalDate()] =
                convertTimeToHours(activity.exerciseDuration ?: "0:0:0")
        }

    // 按日期排序
    val sortedData = valueMap.entries.sortedBy { it.key }

    // 获取最大步数以归一化高度
    val maxValue = sortedData.maxOfOrNull { it.value }?.toFloat() ?: 1f  // 使用最大步数归一化

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 30  // 固定为30天的数据宽度

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            sortedData.forEachIndexed { index, entry ->
                val (date, value) = entry
                val barHeight =
                    (value.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f  // 为日期和步数留出空间

                val x = index * (barWidthPx + barSpacingPx)

                // 绘制条形
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
                )

                // 绘制步数
                drawContext.canvas.nativeCanvas.drawText(
                    value.toString() + "H",
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()
                    }
                )

                // 绘制日期
                val dateString = date.format(DateTimeFormatter.ofPattern("M/d"))
                drawContext.canvas.nativeCanvas.drawText(
                    dateString,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(), // 日期显示在下方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()
                    }
                )
            }
        }
    }
}


@Composable
fun ExerciseDurationYearlyChartView(data: List<DailyActivity>) {
    // 初始化一个Map，其中键是月份，值是步数列表
    val activitiesByMonth = (1..12).associateWith { mutableListOf<Double>() }

    // 填充步数列表
    for (activity in data) {
        val month = activity.date.monthValue
        activitiesByMonth[month]?.add(convertTimeToHours(activity.exerciseDuration ?: "0:0:0"))
    }

    // 计算每个月的平均步数
    val monthlyAverages = activitiesByMonth.map {
        //month是键，valuesList
            (month, valuesList) ->
        val averageValues = if (valuesList.isNotEmpty()) valuesList.average().toDouble() else 0.0
        // 返回的新map，任然是键 值结构
        month to averageValues  // 将月份和平均步数作为键值对存储
    }

    // 找到最大平均步数以归一化高度
    val maxValue = monthlyAverages.maxOfOrNull { it.second }?.toFloat() ?: 1f

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 12  // 总是显示12个月

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            // chartHeight 是表格高度的意思？
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            monthlyAverages.forEachIndexed { index, (month, averageValues) ->
                // barHeight 代表是条的高度
                val barHeight =
                    (averageValues.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f
                val x = index * (barWidthPx + barSpacingPx)
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()),
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)
                )
                // 显示月份，格式化为“MMM”（如Jan, Feb, ...）
                val monthLabel =
                    YearMonth.of(2020, month).format(DateTimeFormatter.ofPattern("MMM"))
                drawContext.canvas.nativeCanvas.drawText(
                    monthLabel,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(),  // 增加一点垂直空间以避免文字与条形图重叠
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()  // 调整文本大小
                    }
                )
                // 显示平均步数
                drawContext.canvas.nativeCanvas.drawText(
                    String.format("%.2f", averageValues) + "H",
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(),  // 在条形图上方留出空间显示步数
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()  // 调整文本大小
                    }
                )
            }
        }
    }
}

@Composable
fun SleepRecordWeekChartView(data: List<SleepRecord>) {
    val maxValue =
        data.maxOfOrNull { convertTimeToHours(time = it.totalDuration ?: "0:0:0") }?.toFloat()
            ?: 1f // 获取最大步数以归一化高度
    val barWidth = 40.dp
    val barSpacing = 8.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
        // .padding(16.dp)
    ) {
        val chartHeight = size.height
        val barWidthPx = barWidth.toPx()
        val barSpacingPx = barSpacing.toPx()

        data.forEachIndexed { index, record ->
            // 根据data的数据，逐个生成条形图
            val theValue = convertTimeToHours(time = record.totalDuration ?: "0:0:0")
            val date = record.date.format(DateTimeFormatter.ofPattern("M/d")) // 格式化日期
            val barHeight =
                (theValue / maxValue).toFloat() * (chartHeight - 50.dp.toPx()) * 0.95f // 留出空间显示日期和步数

            val x = index * (barWidthPx + barSpacingPx)

            // 绘制条形
            drawRoundRect(
                color = Color.Red,
                topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                size = Size(barWidthPx, barHeight),
                cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
            )

            // 绘制步数
            drawContext.canvas.nativeCanvas.drawText(
                theValue.toString() + "H",
                x + barWidthPx / 2,
                chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 14.sp.toPx()
                }
            )

            // 绘制日期
            drawContext.canvas.nativeCanvas.drawText(
                date,
                x + barWidthPx / 2,
                chartHeight - 10.dp.toPx(), // 日期显示在下方
                Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = Paint.Align.CENTER
                    textSize = 12.sp.toPx()
                }
            )
        }
    }
}


@Composable
fun SleepRecordMonthChartView(data: List<SleepRecord>) {
    // 确定展示日期范围：从今天向前数30天
    val today = LocalDateTime.now()
    val startOfPeriod = today.minusDays(29).toLocalDate()  // 向前数30天（包含今天）

    // 初始化数据映射，每天的步数默认为0
    val valueMap = mutableMapOf<LocalDate, Double>()
    for (i in 0 until 30) {  // 循环30天
        valueMap[startOfPeriod.plusDays(i.toLong())] = 0.0
    }

    // 填充实际数据
    data.filter { it.date.toLocalDate() in startOfPeriod..today.toLocalDate() }
        .forEach { record ->
            valueMap[record.date.toLocalDate()] =
                convertTimeToHours(record.totalDuration ?: "0:0:0")
        }

    // 按日期排序
    val sortedData = valueMap.entries.sortedBy { it.key }

    // 获取最大步数以归一化高度
    val maxValue = sortedData.maxOfOrNull { it.value }?.toFloat() ?: 1f  // 使用最大步数归一化

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 30  // 固定为30天的数据宽度

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            sortedData.forEachIndexed { index, entry ->
                val (date, value) = entry
                val barHeight =
                    (value.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f  // 为日期和步数留出空间

                val x = index * (barWidthPx + barSpacingPx)

                // 绘制条形
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()), // 为底部日期留出空间
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)  // 应用圆角
                )

                // 绘制步数
                drawContext.canvas.nativeCanvas.drawText(
                    value.toString() + "H",
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(), // 步数显示在条形上方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()
                    }
                )

                // 绘制日期
                val dateString = date.format(DateTimeFormatter.ofPattern("M/d"))
                drawContext.canvas.nativeCanvas.drawText(
                    dateString,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(), // 日期显示在下方
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()
                    }
                )
            }
        }
    }
}


@Composable
fun SleepRecordYearlyChartView(data: List<SleepRecord>) {
    // 初始化一个Map，其中键是月份，值是步数列表
    val activitiesByMonth = (1..12).associateWith { mutableListOf<Double>() }

    // 填充步数列表
    for (record in data) {
        val month = record.date.monthValue
        activitiesByMonth[month]?.add(convertTimeToHours(record.totalDuration ?: "0:0:0"))
    }

    // 计算每个月的平均步数
    val monthlyAverages = activitiesByMonth.map {
        //month是键，valuesList
            (month, valuesList) ->
        val averageValues = if (valuesList.isNotEmpty()) valuesList.average().toDouble() else 0.0
        // 返回的新map，任然是键 值结构
        month to averageValues  // 将月份和平均步数作为键值对存储
    }

    // 找到最大平均步数以归一化高度
    val maxValue = monthlyAverages.maxOfOrNull { it.second }?.toFloat() ?: 1f

    val barWidth = 40.dp
    val barSpacing = 8.dp
    val chartWidth = (barWidth + barSpacing) * 12  // 总是显示12个月

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width(chartWidth)
                .height(300.dp)
        ) {
            // chartHeight 是表格高度的意思？
            val chartHeight = size.height
            val barWidthPx = barWidth.toPx()
            val barSpacingPx = barSpacing.toPx()

            monthlyAverages.forEachIndexed { index, (month, averageValues) ->
                // barHeight 代表是条的高度
                val barHeight =
                    (averageValues.toFloat() / maxValue) * (chartHeight - 50.dp.toPx()) * 0.95f
                val x = index * (barWidthPx + barSpacingPx)
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(x, chartHeight - barHeight - 40.dp.toPx()),
                    size = Size(barWidthPx, barHeight),
                    cornerRadius = CornerRadius(20f, 20f)
                )
                // 显示月份，格式化为“MMM”（如Jan, Feb, ...）
                val monthLabel =
                    YearMonth.of(2020, month).format(DateTimeFormatter.ofPattern("MMM"))
                drawContext.canvas.nativeCanvas.drawText(
                    monthLabel,
                    x + barWidthPx / 2,
                    chartHeight - 10.dp.toPx(),  // 增加一点垂直空间以避免文字与条形图重叠
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 12.sp.toPx()  // 调整文本大小
                    }
                )
                // 显示平均步数
                drawContext.canvas.nativeCanvas.drawText(
                    String.format("%.2f", averageValues) + "H",
                    x + barWidthPx / 2,
                    chartHeight - barHeight - 45.dp.toPx(),  // 在条形图上方留出空间显示步数
                    Paint().apply {
                        color = android.graphics.Color.BLACK
                        textAlign = Paint.Align.CENTER
                        textSize = 14.sp.toPx()  // 调整文本大小
                    }
                )
            }
        }
    }
}


