package com.example.healthapp.ui.ToolClass

// 将时间转换为Double
fun timeToDouble(timeStr: String): Double {
    // 将字符串按冒号分割成小时、分钟、秒
    val parts = timeStr.split(":")
    if (parts.size != 3)
        return 0.0
    val hours = parts[0].toInt() // 将小时部分转换为整数
    val minutes = parts[1].toInt() // 将分钟部分转换为整数
    val seconds = parts[2].toInt() // 将秒部分转换为整数

    // 计算总小时数：小时 + 分钟/60 + 秒/3600
    return hours + minutes / 60.0 + seconds / 3600.0
}