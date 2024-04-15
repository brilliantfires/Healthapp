@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.healthapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.rememberNavController
import com.example.healthapp.Navigation.HealthNavHost
import com.example.healthapp.data.viewmodel.ArticleMediaModelFactory
import com.example.healthapp.data.viewmodel.ArticleMediaViewModel
import com.example.healthapp.data.viewmodel.ArticleTagRelationModelFactory
import com.example.healthapp.data.viewmodel.ArticleTagRelationViewModel
import com.example.healthapp.data.viewmodel.ArticleTagsModelFactory
import com.example.healthapp.data.viewmodel.ArticleTagsViewModel
import com.example.healthapp.data.viewmodel.ArticlesModelFactory
import com.example.healthapp.data.viewmodel.ArticlesViewModel
import com.example.healthapp.data.viewmodel.AuthorsModelFactory
import com.example.healthapp.data.viewmodel.AuthorsViewModel
import com.example.healthapp.data.viewmodel.DailyActivityViewModel
import com.example.healthapp.data.viewmodel.DailyActivityViewModelFactory
import com.example.healthapp.data.viewmodel.DisplayCardViewModel
import com.example.healthapp.data.viewmodel.DisplayCardViewModelFactory
import com.example.healthapp.data.viewmodel.ExerciseRecordModelFactory
import com.example.healthapp.data.viewmodel.ExerciseRecordViewModel
import com.example.healthapp.data.viewmodel.HealthIndicatorViewModel
import com.example.healthapp.data.viewmodel.HealthIndicatorViewModelFactory
import com.example.healthapp.data.viewmodel.MedicationRecordViewModel
import com.example.healthapp.data.viewmodel.MedicationRecordViewModelFactory
import com.example.healthapp.data.viewmodel.NutritionRecordViewModel
import com.example.healthapp.data.viewmodel.NutritionRecordViewModelFactory
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModelFactory
import com.example.healthapp.data.viewmodel.SleepRecordViewModel
import com.example.healthapp.data.viewmodel.SleepRecordViewModelFactory
import com.example.healthapp.data.viewmodel.UserViewModel
import com.example.healthapp.data.viewmodel.UserViewModelFactory
import com.example.healthapp.ui.theme.HealthappTheme


class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as HealthApplication).userRepository)
    }
    private val physicalProfileViewModel: PhysicalProfileViewModel by viewModels {
        PhysicalProfileViewModelFactory((application as HealthApplication).physicalProfileRepository)
    }
    private val sleepRecordViewModel: SleepRecordViewModel by viewModels {
        SleepRecordViewModelFactory((application as HealthApplication).sleepRecordRepository)
    }
    private val nutritionRecordViewModel: NutritionRecordViewModel by viewModels {
        NutritionRecordViewModelFactory((application as HealthApplication).nutritionRecordRepository)
    }
    private val medicationRecordViewModel: MedicationRecordViewModel by viewModels {
        MedicationRecordViewModelFactory((application as HealthApplication).medicationRecordRepository)
    }
    private val healthIndicatorViewModel: HealthIndicatorViewModel by viewModels {
        HealthIndicatorViewModelFactory((application as HealthApplication).healthIndicatorRepository)
    }
    private val exerciseRecordViewModel: ExerciseRecordViewModel by viewModels {
        ExerciseRecordModelFactory((application as HealthApplication).exerciseRecordRepository)
    }
    private val dailyActivityViewModel: DailyActivityViewModel by viewModels {
        DailyActivityViewModelFactory((application as HealthApplication).dailyActivityRepository)
    }
    private val authorsViewModel: AuthorsViewModel by viewModels {
        AuthorsModelFactory((application as HealthApplication).authorRepository)
    }
    private val articleTagsViewModel: ArticleTagsViewModel by viewModels {
        ArticleTagsModelFactory((application as HealthApplication).articleTagsRepository)
    }
    private val articleTagRelationViewModel: ArticleTagRelationViewModel by viewModels {
        ArticleTagRelationModelFactory((application as HealthApplication).articleTagRelationRepository)
    }
    private val articlesViewModel: ArticlesViewModel by viewModels {
        ArticlesModelFactory((application as HealthApplication).articlesRepository)
    }
    private val articleMediaViewModel: ArticleMediaViewModel by viewModels {
        ArticleMediaModelFactory((application as HealthApplication).articleMediaRepository)
    }
    private val displayCardViewModel: DisplayCardViewModel by viewModels {
        DisplayCardViewModelFactory((application as HealthApplication).displayCardRepository)
    }


    // 在Activity的onCreate方法中设置布局和行为
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // 设置Activity的内容
        setContent {
            // 应用的主题设置，可能包括颜色、字体等
            HealthappTheme {
                // rememberNavController用于创建和记住一个导航控制器，管理导航图中的导航和状态
                val navController = rememberNavController()
                // 使用Column布局来垂直排列子组件
                // HealthNavHost是一个自定义的Composable函数，负责管理健康应用的导航
                HealthNavHost(
                    navController = navController,
                    articleMediaViewModel = articleMediaViewModel,
                    articlesViewModel = articlesViewModel,
                    articleTagsViewModel = articleTagsViewModel,
                    authorsViewModel = authorsViewModel,
                    exerciseRecordViewModel = exerciseRecordViewModel,
                    healthIndicationViewModel = healthIndicatorViewModel,
                    medicationRecordViewModel = medicationRecordViewModel,
                    nutritionRecordViewModel = nutritionRecordViewModel,
                    physicalProfileViewModel = physicalProfileViewModel,
                    sleepRecordViewModel = sleepRecordViewModel,
                    articleTagRelationViewModel = articleTagRelationViewModel,
                    userViewModel = userViewModel,
                    dailyActivityViewModel = dailyActivityViewModel,
                    displayCardViewModel = displayCardViewModel,
                    contentPadding = PaddingValues() // PaddingValues设置内边距，这里设置为默认值
                )

                // TestScreen()
            }
        }

    }


}
