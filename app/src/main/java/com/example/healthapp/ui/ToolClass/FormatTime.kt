package com.example.healthapp.ui.ToolClass


// 这里是一个函数，可以将数据库中存储的时间，在输出的时候转换为更加人性化的形式
fun formatTime(timeStr: String): String {
    // 将字符串按照冒号分割
    val parts = timeStr.split(":")
    if (parts.size != 3)
        return timeStr+"数据格式有误!"
    // 分别取出小时、分钟和秒
    val hours = parts[0]
    val minutes = parts[1]
    val seconds = parts[2]

    // 返回格式化后的字符串
    return "${hours}小时${minutes}分钟${seconds}秒"
}

// 该函数的作用是将数据库中的时间转换为保留一位小数的Double格式
fun convertTimeToHours(time: String): Double {
    // 将时间字符串 "HH:mm:ss" 分割为小时、分钟和秒
    val parts = time.split(":")

    // 检查时间格式是否正确
    if (parts.size != 3) {
        throw IllegalArgumentException("时间格式不正确！")
    }

    // 提取小时、分钟和秒
    val hours = parts[0].toInt()
    val minutes = parts[1].toInt()
    val seconds = parts[2].toInt()

    // 将分钟转换为小时的小数部分
    val minutesToHours = minutes / 60.0
    // 将秒转换为小时的小数部分
    val secondsToHours = seconds / 3600.0

    // 计算总小时
    val totalHours = hours + minutesToHours + secondsToHours

    // 返回结果，保留一位小数
    return String.format("%.1f", totalHours).toDouble()
}
