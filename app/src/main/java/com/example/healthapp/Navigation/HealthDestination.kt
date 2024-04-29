package com.example.healthapp.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DoNotStep
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.Stairs
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.ui.graphics.vector.ImageVector

interface Destination {
    val route: String
    val icon: ImageVector
}

interface DestinationWithUserId : Destination {
    fun createRoute(userId: Int): String
}

object BrowseScreenDestination : Destination {
    override val route = "Browse"
    override val icon = Icons.Filled.Widgets // 示例图标
}

object SettingDestination : DestinationWithUserId {
    override val route = "SettingScreen/{userId}"
    override val icon = Icons.Filled.Settings // 示例图标

    override fun createRoute(userId: Int): String {
        return "SettingScreen/$userId"
    }
}

object AbstractScreenDestination : DestinationWithUserId {
    override val route = "AbstractScreen/{userId}"
    override val icon = Icons.Filled.Star // 示例图标

    override fun createRoute(userId: Int): String {
        return "AbstractScreen/$userId"
    }
}

object AbstractItemOfStepDetailScreenDestination : Destination {
    override val route = "AbstractItemOfStepDetailScreen/{userId}"
    override val icon = Icons.Filled.Details // 示例图标

    fun createRoute(userId: Int): String {
        return "AbstractItemOfStepDetailScreen/$userId"
    }
}

object AbstractItemOfEnergyDetailScreenDestination : Destination {
    override val route = "AbstractItemOfEnergyDetailScreen/{userId}"
    override val icon = Icons.Filled.Details // 示例图标

    fun createRoute(userId: Int): String {
        return "AbstractItemOfEnergyDetailScreen/$userId"
    }
}

object StepDestination : Destination {
    override val route = "Steps"
    override val icon = Icons.Filled.DoNotStep // 示例图标
}

object HealthDetailsScreenDestination : Destination {
    override val route = "HealthDetailScreen/{userId}"
    override val icon = Icons.Filled.Notes // 示例图标

    fun createRoute(userId: Int): String {
        return "HealthDetailScreen/$userId"
    }
}

object HomePageScreenDestination : Destination {
    override val route = "HomePageScreen"
    override val icon = Icons.Filled.Notes // 示例图标
}

// 步数历史数据展示界面
object StepDetailsScreenDestination : Destination {
    override val route = "StepDetailsScreen/{userId}"
    override val icon = Icons.Filled.DirectionsRun

    fun createRoute(userId: Int): String {
        return "StepDetailsScreen/$userId"
    }
}

// 能量消耗历史数据展示界面
object ActivityEnergyConsumptionScreenDestination : Destination {
    override val route = "ActivityEnergyConsumptionScreen/{userId}"
    override val icon = Icons.Filled.DirectionsRun

    fun createRoute(userId: Int): String {
        return "ActivityEnergyConsumptionScreen/$userId"
    }
}

// 步行距离历史数据展示界面
object WalkDistanceDetailScreenDestination : Destination {
    override val route = "WalkDistanceDetailScreen/{userId}"
    override val icon = Icons.Filled.DirectionsWalk

    fun createRoute(userId: Int): String {
        return "WalkDistanceDetailScreen/$userId"
    }
}

// 跑步距离历史数据展示界面
object RunningDistanceDetailScreenDestination : Destination {
    override val route = "RunningDistanceDetailScreen/{userId}"
    override val icon = Icons.Filled.DirectionsRun

    fun createRoute(userId: Int): String {
        return "RunningDistanceDetailScreen/$userId"
    }
}

// 爬楼层数历史数据展示界面
object FloorsClimbedDetailsScreenDestination : Destination {
    override val route = "FloorsClimbedDetailsScreen/{userId}"
    override val icon = Icons.Filled.Stairs

    fun createRoute(userId: Int): String {
        return "FloorsClimbedDetailsScreen/$userId"
    }
}

// 运动时间历史数据展示界面
object ExerciseDurationDetailsScreenDestination : Destination {
    override val route = "ExerciseDurationDetailsScreen/{userId}"
    override val icon = Icons.Filled.Timer

    fun createRoute(userId: Int): String {
        return "ExerciseDurationDetailsScreen/$userId"
    }
}

// 步行距离细节项目界面
object AbstractItemOfWalkDistanceDetailScreenDestination : Destination {
    override val route = "AbstractItemOfWalkDistanceDetailScreen/{userId}"
    override val icon = Icons.Filled.DirectionsWalk

    fun createRoute(userId: Int): String {
        return "AbstractItemOfWalkDistanceDetailScreen/$userId"
    }
}

// 跑步距离细节项目界面
object AbstractItemOfRunningDistanceDetailScreenDestination : Destination {
    override val route = "AbstractItemOfRunningDistanceDetailScreen/{userId}"
    override val icon = Icons.Filled.DirectionsRun

    fun createRoute(userId: Int): String {
        return "AbstractItemOfRunningDistanceDetailScreen/$userId"
    }
}

// 爬楼层数细节项目界面
object AbstractItemOfFloorsClimbedDetailScreenDestination : Destination {
    override val route = "AbstractItemOfFloorsClimbedDetailScreen/{userId}"
    override val icon = Icons.Filled.Stairs

    fun createRoute(userId: Int): String {
        return "AbstractItemOfFloorsClimbedDetailScreen/$userId"
    }
}

// 运动时间细节项目界面
object AbstractItemOfExerciseDurationDetailScreenDestination : Destination {
    override val route = "AbstractItemOfExerciseDurationDetailScreen/{userId}"
    override val icon = Icons.Filled.Timer

    fun createRoute(userId: Int): String {
        return "AbstractItemOfExerciseDurationDetailScreen/$userId"
    }
}

// 睡眠详情界面
object SleepRecordDetailsScreenDestination : Destination {
    override val route = "SleepRecordDetailsScreen/{userId}"
    override val icon = Icons.Filled.Bed // 假设用于表示睡眠记录详情的图标

    fun createRoute(userId: Int): String {
        return "SleepRecordDetailsScreen/$userId"
    }
}

// 睡眠细节项目界面
object AbstractItemOfSleepRecordDetailScreenDestination : Destination {
    override val route = "AbstractItemOfSleepRecordDetailScreen/{userId}"
    override val icon = Icons.Filled.Bedtime // 假设用于表示抽象的睡眠记录项目详情的图标

    fun createRoute(userId: Int): String {
        return "AbstractItemOfSleepRecordDetailScreen/$userId"
    }
}

// 展示所有摘要卡片界面
// 睡眠细节项目界面
object AllAbstractScreenDestination : Destination {
    override val route = "AllAbstractScreenDestination/{userId}"
    override val icon = Icons.Filled.AllInbox // 假设用于表示抽象的睡眠记录项目详情的图标

    fun createRoute(userId: Int): String {
        return "AllAbstractScreenDestination/$userId"
    }
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

object DataSynchronizationScreenDestination : Destination {
    override val route = "DataSynchronizationScreen"
    override val icon = Icons.Filled.Sync
}

//显示隐私协议界面的Destination
object PrivacyPolicyScreenDestination : Destination {
    override val route = "PrivacyPolicyScreen"
    override val icon = Icons.Filled.PrivacyTip
}

//显示反馈界面的Destination
object FeedbackScreenDestination : Destination {
    override val route = "FeedbackScreen"
    override val icon = Icons.Filled.Feedback
}

//显示关于应用界面的Destination
object AboutAppScreenDestination : Destination {
    override val route = "AboutAppScreen"
    override val icon = Icons.Filled.Info
}

//显示关于应用界面的Destination
object LoginScreenDestination : Destination {
    override val route = "LoginScreen"
    override val icon = Icons.Filled.Login
}

//显示关于应用界面的Destination
object RegisterScreenDestination : Destination {
    override val route = "RegisterScreen"
    override val icon = Icons.Filled.HowToReg
}

//显示密码找回界面的Destination
object PasswordRecoveryScreenDestination : Destination {
    override val route = "PasswordRecoveryScreen"
    override val icon = Icons.Filled.Password
}

// SourceAndVisitScreen 界面
object SourceAndVisitScreenDestination : Destination {
    override val route = "SourceAndVisitScreen/{name}/{userId}" // 添加了userId
    override val icon = Icons.Filled.Source

    fun createRoute(name: String, userId: Int): String {
        return "SourceAndVisitScreen/$name/$userId"
    }
}


//管理员界面显示所有的账户卡片的Destination
object AdminScreenDestination : Destination {
    override val route = "AdminScreen"
    override val icon = Icons.Filled.AdminPanelSettings
}

//管理员界面显示对应user的所有信息的Destination
object AllUserInformationScreenDestination : Destination {
    override val route = "AllUserInformationScreen/{userId}"
    override val icon = Icons.Filled.Info

    fun createRoute(userId: Int): String {
        return "AllUserInformationScreen/$userId"
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