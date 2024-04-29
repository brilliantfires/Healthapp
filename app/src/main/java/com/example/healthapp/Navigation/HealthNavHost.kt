package com.example.healthapp.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.healthapp.data.viewmodel.ArticleMediaViewModel
import com.example.healthapp.data.viewmodel.ArticleTagRelationViewModel
import com.example.healthapp.data.viewmodel.ArticleTagsViewModel
import com.example.healthapp.data.viewmodel.ArticlesViewModel
import com.example.healthapp.data.viewmodel.AuthorsViewModel
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.DisplayCardViewModel
import com.example.healthapp.data.viewmodel.ExerciseRecordViewModel
import com.example.healthapp.data.viewmodel.HealthIndicatorViewModel
import com.example.healthapp.data.viewmodel.MedicationRecordViewModel
import com.example.healthapp.data.viewmodel.NutritionRecordViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.SleepRecordViewModel
import com.example.healthapp.data.viewmodel.SyncViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.ui.ScreenLevel1.AbstractScreen
import com.example.healthapp.ui.ScreenLevel1.AdminScreen
import com.example.healthapp.ui.ScreenLevel1.BrowseScreen
import com.example.healthapp.ui.ScreenLevel1.LoginScreen
import com.example.healthapp.ui.ScreenLevel1.PasswordRecoveryScreen
import com.example.healthapp.ui.ScreenLevel1.RegisterScreen
import com.example.healthapp.ui.ScreenLevel1.SettingScreen
import com.example.healthapp.ui.ScreenLevel2.AboutAppScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfEnergyDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfExerciseDurationDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfFloorsClimbedDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfRunningDistanceDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfSleepRecordDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfStepDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfWalkDistanceDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AllAbstractScreen
import com.example.healthapp.ui.ScreenLevel2.AllUserInformationScreen
import com.example.healthapp.ui.ScreenLevel2.ArticleDetailScreen
import com.example.healthapp.ui.ScreenLevel2.DataSynchronizationScreen
import com.example.healthapp.ui.ScreenLevel2.FeedbackScreen
import com.example.healthapp.ui.ScreenLevel2.HealthDetailsScreen
import com.example.healthapp.ui.ScreenLevel2.PrivacyPolicyScreen
import com.example.healthapp.ui.ScreenLevel2.SourceAndVisitScreen
import com.example.healthapp.ui.ScreenLevel3.ActivityEnergyConsumptionScreen
import com.example.healthapp.ui.ScreenLevel3.ExerciseDurationDetailsScreen
import com.example.healthapp.ui.ScreenLevel3.FloorsClimbedDetailsScreen
import com.example.healthapp.ui.ScreenLevel3.RunningDistanceDetailsScreen
import com.example.healthapp.ui.ScreenLevel3.SleepRecordDetailsScreen
import com.example.healthapp.ui.ScreenLevel3.StepDetailsScreen
import com.example.healthapp.ui.ScreenLevel3.WalkingDistanceDetailsScreen

@Composable
fun HealthNavHost(
    navController: NavHostController,
    articleMediaViewModel: ArticleMediaViewModel,
    articlesViewModel: ArticlesViewModel,
    articleTagsViewModel: ArticleTagsViewModel,
    authorsViewModel: AuthorsViewModel,
    exerciseRecordViewModel: ExerciseRecordViewModel,
    healthIndicationViewModel: HealthIndicatorViewModel,
    medicationRecordViewModel: MedicationRecordViewModel,
    nutritionRecordViewModel: NutritionRecordViewModel,
    physicalProfileViewModel: PhysicalProfileViewModel,
    sleepRecordViewModel: SleepRecordViewModel,
    articleTagRelationViewModel: ArticleTagRelationViewModel,
    userViewModel: UserViewModel,
    dailyActivityViewModel: DailyActivityViewModel,
    displayCardViewModel: DisplayCardViewModel,
    syncViewModel: SyncViewModel,
    contentPadding: PaddingValues
) {
    val userId by userViewModel.currentUserId.observeAsState()
    userViewModel.loadUserId()
    val startRoute =
        if (userId == -1) LoginScreenDestination.route else BrowseScreenDestination.route

    // 这里事先规定好了起始界面 startDestination
    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        composable(
            SettingDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            SettingScreen(
                userId = userId,
                userViewModel = userViewModel,
                sleepRecordViewModel = sleepRecordViewModel,
                onBackClicked = { navController.popBackStack() },
                navController = navController
            )
        }
        composable(
            HealthDetailsScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            HealthDetailsScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                onBackClicked = {},
                navController = navController
            )
        }
        composable(
            AbstractScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            AbstractScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                displayCardViewModel = displayCardViewModel,
                sleepRecordViewModel = sleepRecordViewModel,
                navController
            )
        }
        composable(
            AbstractItemOfStepDetailScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            AbstractItemOfStepDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController
            )
        }
        composable(
            AbstractItemOfEnergyDetailScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            AbstractItemOfEnergyDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController
            )
        }
        // 历史步数数据界面
        composable(
            StepDetailsScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            StepDetailsScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 历史能量消耗界面
        composable(
            ActivityEnergyConsumptionScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            ActivityEnergyConsumptionScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 步行距离详情界面
        composable(
            WalkDistanceDetailScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            WalkingDistanceDetailsScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 跑步距离详情界面
        composable(
            RunningDistanceDetailScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            RunningDistanceDetailsScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 爬楼层数详情界面
        composable(
            FloorsClimbedDetailsScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            FloorsClimbedDetailsScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 运动时间详情界面
        composable(
            ExerciseDurationDetailsScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            ExerciseDurationDetailsScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 步行距离细节项目界面
        composable(
            AbstractItemOfWalkDistanceDetailScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            AbstractItemOfWalkDistanceDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 跑步距离细节项目界面
        composable(
            AbstractItemOfRunningDistanceDetailScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            AbstractItemOfRunningDistanceDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 爬楼层数细节项目界面
        composable(
            AbstractItemOfFloorsClimbedDetailScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            AbstractItemOfFloorsClimbedDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 运动时间细节项目界面
        composable(
            AbstractItemOfExerciseDurationDetailScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            AbstractItemOfExerciseDurationDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 对于AbstractItemOfSleepRecordDetailScreen界面
        composable(
            AbstractItemOfSleepRecordDetailScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            AbstractItemOfSleepRecordDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                sleepRecordViewModel = sleepRecordViewModel,
                navController = navController
            )
        }

        // 对于SleepRecordDetailsScreen界面
        composable(
            SleepRecordDetailsScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            SleepRecordDetailsScreen(
                userId = userId,
                sleepRecordViewModel = sleepRecordViewModel,
                navController = navController
            )
        }

        // 展示多有摘要卡片界面
        composable(
            AllAbstractScreenDestination.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            AllAbstractScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                displayCardViewModel = displayCardViewModel,
                sleepRecordViewModel = sleepRecordViewModel,
                navController
            )
        }

        // 展示浏览区主界面的屏幕界面
        composable(BrowseScreenDestination.route) {
            BrowseScreen(
                syncViewModel = syncViewModel,
                userViewModel = userViewModel,
                articlesViewModel = articlesViewModel,
                articleMediaViewModel = articleMediaViewModel,
                articleTagRelationViewModel = articleTagRelationViewModel,
                articleTagsViewModel = articleTagsViewModel,
                authorsViewModel = authorsViewModel,
                navController = navController
            )
        }

        // 展示文章详情界面
        composable(
            ArticleDetailScreenDestination.route,
            arguments = listOf(navArgument("articleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt("articleId") ?: -1
            ArticleDetailScreen(
                articleId = articleId,
                authorsViewModel = authorsViewModel,
                articleMediaViewModel = articleMediaViewModel,
                articlesViewModel = articlesViewModel,
                navController = navController
            )
        }

        // 数据同步设置界面
        composable(DataSynchronizationScreenDestination.route) {
            DataSynchronizationScreen(
                navController = navController,
                articleMediaViewModel = articleMediaViewModel,
                articlesViewModel = articlesViewModel,
                articleTagsViewModel = articleTagsViewModel,
                authorsViewModel = authorsViewModel,
                exerciseRecordViewModel = exerciseRecordViewModel,
                healthIndicationViewModel = healthIndicationViewModel,
                medicationRecordViewModel = medicationRecordViewModel,
                nutritionRecordViewModel = nutritionRecordViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                sleepRecordViewModel = sleepRecordViewModel,
                articleTagRelationViewModel = articleTagRelationViewModel,
                userViewModel = userViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                displayCardViewModel = displayCardViewModel,
                syncViewModel = syncViewModel
            )
        }

        // 隐私协议界面
        composable(PrivacyPolicyScreenDestination.route) {
            PrivacyPolicyScreen(navController = navController)
        }
        // 反馈界面
        composable(FeedbackScreenDestination.route) {
            FeedbackScreen(navController = navController)
        }
        // 关于应用界面
        composable(AboutAppScreenDestination.route) {
            AboutAppScreen(navController = navController)
        }
        // 登录界面
        composable(LoginScreenDestination.route) {
            LoginScreen(userViewModel = userViewModel, navController = navController)
        }
        // 注册界面
        composable(RegisterScreenDestination.route) {
            RegisterScreen(syncViewModel = syncViewModel, navController = navController)
        }
        // 密码找回界面
        composable(PasswordRecoveryScreenDestination.route) {
            PasswordRecoveryScreen(navController = navController, userViewModel = userViewModel)
        }
        // 数据源与访问界面
        composable(
            SourceAndVisitScreenDestination.route,
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                },
                navArgument("userId") { // 添加userId作为一个参数
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "加载中. . ."
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1 // 获取userId
            SourceAndVisitScreen(userId = userId, name = name, navController = navController)
        }


        // 管理员管理账户界面
        composable(AdminScreenDestination.route) {
            AdminScreen(syncViewModel = syncViewModel, navController = navController)
        }
        // 管理员，用户所有信息界面
        composable(
            AllUserInformationScreenDestination.route,
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 1
            AllUserInformationScreen(
                userId = userId,
                navController = navController,
                syncViewModel = syncViewModel
            )
        }
    }
}