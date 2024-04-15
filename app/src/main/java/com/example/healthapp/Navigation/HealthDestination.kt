package com.example.healthapp.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DoNotStep
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.ui.graphics.vector.ImageVector

interface Destination {
    val route: String
    val icon: ImageVector
}

object BrowseScreenDestination : Destination {
    override val route = "Browse"
    override val icon = Icons.Filled.Widgets // 示例图标
}

object SettingDestination : Destination {
    override val route = "SettingScreen"
    override val icon = Icons.Filled.Settings // 示例图标
}

object AbstractScreenDestination : Destination {
    override val route = "AbstractScreen"
    override val icon = Icons.Filled.Star // 示例图标
}

object AbstractItemOfStepDetailScreenDestination : Destination {
    override val route = "AbstractItemOfStepDetailScreen"
    override val icon = Icons.Filled.Details // 示例图标
}

object AbstractItemOfEnergyDetailScreenDestination : Destination {
    override val route = "AbstractItemOfEnergyDetailScreen"
    override val icon = Icons.Filled.Details // 示例图标
}

object StepDestination : Destination {
    override val route = "Steps"
    override val icon = Icons.Filled.DoNotStep // 示例图标
}

object HealthDetailsScreenDestination : Destination {
    override val route = "HealthDetailScreen"
    override val icon = Icons.Filled.Notes // 示例图标
}

object HomePageScreenDestination : Destination {
    override val route = "HomePageScreen"
    override val icon = Icons.Filled.Notes // 示例图标
}

// 步数历史数据展示界面
object StepDetailsScreenDestination : Destination {
    override val route = "StepDetailsScreen"
    override val icon = Icons.Filled.DirectionsRun
}

// 能量消耗历史数据展示界面
object ActivityEnergyConsumptionScreenDestination : Destination {
    override val route = "ActivityEnergyConsumptionScreen"
    override val icon = Icons.Filled.DirectionsRun
}

// 步行距离历史数据展示界面
object WalkDistanceDetailScreenDestination : Destination {
    override val route = "WalkDistanceDetailScreen"
    override val icon = Icons.Filled.DirectionsWalk
}

// 跑步距离历史数据展示界面
object RunningDistanceDetailScreenDestination : Destination {
    override val route = "RunningDistanceDetailScreen"
    override val icon = Icons.Filled.DirectionsRun
}

// 爬楼层数历史数据展示界面
object FloorsClimbedDetailsScreenDestination : Destination {
    override val route = "FloorsClimbedDetailsScreen"
    override val icon = Icons.Filled.Stairs
}

// 运动时间历史数据展示界面
object ExerciseDurationDetailsScreenDestination : Destination {
    override val route = "ExerciseDurationDetailsScreen"
    override val icon = Icons.Filled.Timer
}

// 步行距离细节项目界面
object AbstractItemOfWalkDistanceDetailScreenDestination : Destination {
    override val route = "AbstractItemOfWalkDistanceDetailScreen"
    override val icon = Icons.Filled.DirectionsWalk
}

// 跑步距离细节项目界面
object AbstractItemOfRunningDistanceDetailScreenDestination : Destination {
    override val route = "AbstractItemOfRunningDistanceDetailScreen"
    override val icon = Icons.Filled.DirectionsRun
}

// 爬楼层数细节项目界面
object AbstractItemOfFloorsClimbedDetailScreenDestination : Destination {
    override val route = "AbstractItemOfFloorsClimbedDetailScreen"
    override val icon = Icons.Filled.Stairs
}

// 运动时间细节项目界面
object AbstractItemOfExerciseDurationDetailScreenDestination : Destination {
    override val route = "AbstractItemOfExerciseDurationDetailScreen"
    override val icon = Icons.Filled.Timer
}

// 睡眠详情界面
object SleepRecordDetailsScreenDestination : Destination {
    override val route = "SleepRecordDetailsScreen"
    override val icon = Icons.Filled.Bed // 假设用于表示睡眠记录详情的图标
}

// 睡眠细节项目界面
object AbstractItemOfSleepRecordDetailScreenDestination : Destination {
    override val route = "AbstractItemOfSleepRecordDetailScreen"
    override val icon = Icons.Filled.Bedtime // 假设用于表示抽象的睡眠记录项目详情的图标
}

// 展示所有摘要卡片界面
// 睡眠细节项目界面
object AllAbstractScreenDestination : Destination {
    override val route = "AllAbstractScreenDestination"
    override val icon = Icons.Filled.AllInbox // 假设用于表示抽象的睡眠记录项目详情的图标
}

// 展示文章的界面
object ArticleDetailScreenDestination : Destination {
    override val route = "ArticleDetailScreen/{articleId}"
    override val icon = Icons.Filled.Article

    // 提供一个创建路由的方法
    fun createRoute(articleId: Int): String {
        return "ArticleDetailScreen/$articleId"
    }
}

//object DetailDestination : Destination {
//    override val route = "detail"
//    override val icon = Icons.Filled.More // 示例图标
//    const val detailTypeArg = "detailType"
//    val routeWithArgs = "$route/{$detailTypeArg}"
//    val arguments = listOf(
//        navArgument(detailTypeArg) { type = NavType.StringType }
//    )
//
//    // TODO:如果需要深层链接
//    val deepLinks = listOf(
//        navDeepLink { uriPattern = "HealthApp://$route/{$detailTypeArg}" }
//    )
//}


// 定义一个列表来显示底部导航栏的列表
val HealthBottomList =
    listOf(AbstractScreenDestination, BrowseScreenDestination, SettingDestination)