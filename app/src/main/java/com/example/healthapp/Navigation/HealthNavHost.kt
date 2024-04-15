package com.example.healthapp.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
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
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.ui.AbstractScreen
import com.example.healthapp.ui.AllAbstractScreen
import com.example.healthapp.ui.BrowseScreen
import com.example.healthapp.ui.HealthDetailsScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfEnergyDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfExerciseDurationDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfFloorsClimbedDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfRunningDistanceDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfSleepRecordDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfStepDetailScreen
import com.example.healthapp.ui.ScreenLevel2.AbstractItemOfWalkDistanceDetailScreen
import com.example.healthapp.ui.ScreenLevel2.ArticleDetailScreen
import com.example.healthapp.ui.ScreenLevel3.ActivityEnergyConsumptionScreen
import com.example.healthapp.ui.ScreenLevel3.ExerciseDurationDetailsScreen
import com.example.healthapp.ui.ScreenLevel3.FloorsClimbedDetailsScreen
import com.example.healthapp.ui.ScreenLevel3.RunningDistanceDetailsScreen
import com.example.healthapp.ui.ScreenLevel3.SleepRecordDetailsScreen
import com.example.healthapp.ui.ScreenLevel3.StepDetailsScreen
import com.example.healthapp.ui.ScreenLevel3.WalkingDistanceDetailsScreen
import com.example.healthapp.ui.SettingScreen

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
    contentPadding: PaddingValues
) {
    // 这里事先规定好了起始界面 startDestination
    NavHost(
        navController = navController,
        startDestination = BrowseScreenDestination.route
    ) {
        composable(SettingDestination.route) {
            // 假设你已经有了一个函数来获取用户ID
            val userId = 1 // 临时值，你应该替换为动态获取的用户ID
            SettingScreen(
                userId = userId,
                userViewModel = userViewModel,
                onBackClicked = { navController.popBackStack() },
                navController = navController
            )
        }
        composable(HealthDetailsScreenDestination.route) {
            val userId = 1
            HealthDetailsScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                onBackClicked = {},
                navController = navController
            )
        }
        composable(AbstractScreenDestination.route) {
            val userId = 1
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
        composable(AbstractItemOfStepDetailScreenDestination.route) {
            val userId = 1
            AbstractItemOfStepDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController
            )
        }
        composable(AbstractItemOfEnergyDetailScreenDestination.route) {
            val userId = 1
            AbstractItemOfEnergyDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController
            )
        }
        // 历史步数数据界面
        composable(StepDetailsScreenDestination.route) {
            val userId = 1
            StepDetailsScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 历史能量消耗界面
        composable(ActivityEnergyConsumptionScreenDestination.route) {
            val userId = 1
            ActivityEnergyConsumptionScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 步行距离详情界面
        composable(WalkDistanceDetailScreenDestination.route) {
            val userId = 1 // 假设的用户ID
            WalkingDistanceDetailsScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 跑步距离详情界面
        composable(RunningDistanceDetailScreenDestination.route) {
            val userId = 1 // 假设的用户ID
            RunningDistanceDetailsScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 爬楼层数详情界面
        composable(FloorsClimbedDetailsScreenDestination.route) {
            val userId = 1 // 假设的用户ID
            FloorsClimbedDetailsScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 运动时间详情界面
        composable(ExerciseDurationDetailsScreenDestination.route) {
            val userId = 1 // 假设的用户ID
            ExerciseDurationDetailsScreen(
                userId = userId,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 步行距离细节项目界面
        composable(AbstractItemOfWalkDistanceDetailScreenDestination.route) {
            val userId = 1 // 假设的用户ID
            AbstractItemOfWalkDistanceDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 跑步距离细节项目界面
        composable(AbstractItemOfRunningDistanceDetailScreenDestination.route) {
            val userId = 1 // 假设的用户ID
            AbstractItemOfRunningDistanceDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 爬楼层数细节项目界面
        composable(AbstractItemOfFloorsClimbedDetailScreenDestination.route) {
            val userId = 1 // 假设的用户ID
            AbstractItemOfFloorsClimbedDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 运动时间细节项目界面
        composable(AbstractItemOfExerciseDurationDetailScreenDestination.route) {
            val userId = 1 // 假设的用户ID
            AbstractItemOfExerciseDurationDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                physicalProfileViewModel = physicalProfileViewModel,
                dailyActivityViewModel = dailyActivityViewModel,
                navController = navController
            )
        }
        // 对于AbstractItemOfSleepRecordDetailScreen界面
        composable(AbstractItemOfSleepRecordDetailScreenDestination.route) {
            val userId = 1 // 假设的用户ID
            AbstractItemOfSleepRecordDetailScreen(
                userId = userId,
                userViewModel = userViewModel,
                sleepRecordViewModel = sleepRecordViewModel,
                navController = navController
            )
        }

        // 对于SleepRecordDetailsScreen界面
        composable(SleepRecordDetailsScreenDestination.route) {
            val userId = 1 // 假设的用户ID
            SleepRecordDetailsScreen(
                userId = userId,
                sleepRecordViewModel = sleepRecordViewModel,
                navController = navController
            )
        }

        // 展示多有摘要卡片界面
        composable(AllAbstractScreenDestination.route) {
            val userId = 1
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
    }

}