package com.example.healthapp.ui.ToolClass


// 这里是一个函数，可以将数据库中存储的时间，在输出的时候转换为更加人性化的形式
fun formatTime(timeStr: String): String {
    // 将字符串按照冒号分割
    val parts = timeStr.split(":")
    // 分别取出小时、分钟和秒
    val hours = parts[0]
    val minutes = parts[1]
    val seconds = parts[2]

    // 返回格式化后的字符串
    return "${hours}小时${minutes}分钟${seconds}秒"
}