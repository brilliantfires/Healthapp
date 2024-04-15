package com.example.healthapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.Navigation.ArticleDetailScreenDestination
import com.example.healthapp.Navigation.BottomNavigationBar
import com.example.healthapp.R
import com.example.healthapp.data.entity.Article
import com.example.healthapp.data.viewmodel.ArticleMediaViewModel
import com.example.healthapp.data.viewmodel.ArticleTagRelationViewModel
import com.example.healthapp.data.viewmodel.ArticleTagsViewModel
import com.example.healthapp.data.viewmodel.ArticlesViewModel
import com.example.healthapp.data.viewmodel.AuthorsViewModel
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    articlesViewModel: ArticlesViewModel,
    articleMediaViewModel: ArticleMediaViewModel,
    articleTagRelationViewModel: ArticleTagRelationViewModel,
    articleTagsViewModel: ArticleTagsViewModel,
    authorsViewModel: AuthorsViewModel,
    navController: NavHostController
) {
    // 控制是否展示弹出的选择框
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "浏览",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    Button(
                        onClick = {
                            showDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface, // 设置按钮背景颜色与背景一致
                            contentColor = MaterialTheme.colorScheme.onSurface // 设置按钮内容颜色以确保可见性
                        ),
                        modifier = Modifier.padding(start = 8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp),
                        ) {
                            Text(
                                text = "编辑",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.Blue
                                )
                            )
                        }
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .width(80.dp),
                        contentAlignment = Alignment.Center

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ab5_hiit),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable(
                                    onClick = {
                                        navController.navigate("HealthDetailScreen") {
                                            // 清除HomePageScreen（包含）之上的堆栈
                                            popUpTo("HealthDetailScreen") {
                                                inclusive = true // HomePageScreen 也被清除
                                            }
                                            launchSingleTop = true    // 避免重复创建HomePageScreen的实例
                                            restoreState = true       // 如果可能，恢复之前的状态
                                        }
                                    },
                                    indication = rememberRipple(
                                        bounded = true,
                                        color = Color.Gray
                                    ), // 这里正确应用rememberRipple
                                    interactionSource = remember { MutableInteractionSource() } // 为点击提供一个交互源
                                ),
                            contentDescription = stringResource(id = R.string.avatar_text)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }


    ) { innerPadding ->
        // 这里不知道为什么，总是会出现重复的articleTags,所以使用map来去重复
        val selectedTags by articleTagRelationViewModel.getDisplayTag.map { tags -> tags.distinctBy { it.tagID } }
            .collectAsState(initial = null)
        val allTags by articleTagRelationViewModel.allArticleTagRelations.collectAsState(initial = null)
        val articles by articlesViewModel.allArticles.collectAsState(initial = emptyList())

        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.padding(16.dp)
        ) {
            selectedTags?.forEach { selectedTag ->
                item {
                    CategoryNameDisplay(
                        tagId = selectedTag.tagID,
                        articleTagsViewModel = articleTagsViewModel
                    )
                    val relatedArticles = articles.filter { it.category == selectedTag.tagName }
                    relatedArticles.forEach { article ->
                        ArticleCategory(
                            article = article,
                            articleMediaViewModel = articleMediaViewModel,
                            navController = navController
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        if (showDialog) {
            // 弹出窗口逻辑，用于选择需要显示的卡片视图
            ChooseCategoryDialog(
                articleTagRelationViewModel,
                articleTagsViewModel,
                onDismiss = { showDialog = false })
        }
    }
}

@Composable
fun CategoryNameDisplay(
    tagId: Int,
    articleTagsViewModel: ArticleTagsViewModel
) {
    val articleTag by articleTagsViewModel.getTagById(tagId).collectAsState(initial = null)
    Text(
        text = articleTag?.tagName ?: "",
        fontSize = 20.sp,
        color = Color.Black,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ArticleCategory(
    article: Article,
    articleMediaViewModel: ArticleMediaViewModel,
    navController: NavHostController
) {
    val image by articleMediaViewModel.getMediaByArticleId(article.articleID)
        .collectAsState(initial = null)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    route = ArticleDetailScreenDestination.createRoute(article.articleID)
                )

            }
    ) {
        Column {
            ShowImageByName(imageName = image?.filePath ?: "", modifier = Modifier)

            Text(
                text = article.title,
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ShowImageByName(
    imageName: String,
    modifier: Modifier
) {
    val imageResId = when (imageName) {
        "ab2_quick_yoga" -> R.drawable.ab2_quick_yoga
        "fc1_short_mantras" -> R.drawable.fc1_short_mantras
        "ab5_hiit" -> R.drawable.ab5_hiit
        "ab1_inversions" -> R.drawable.ab1_inversions
        "ab3_stretching" -> R.drawable.ab3_stretching
        "fc2_nature_meditations" -> R.drawable.fc2_nature_meditations
        "fc6_nightly_wind_down" -> R.drawable.fc6_nightly_wind_down
        "ab4_tabata" -> R.drawable.ab4_tabata
        "fc3_stress_and_anxiety" -> R.drawable.fc3_stress_and_anxiety
        "ab6_pre_natal_yoga" -> R.drawable.ab6_pre_natal_yoga
        "fc4_self_massage" -> R.drawable.fc4_self_massage
        "fc5_overwhelmed" -> R.drawable.fc5_overwhelmed
        else -> null // 如果没有匹配的图片，可以设为null或默认图片
    }

    imageResId?.let {
        Image(
            painter = painterResource(id = it),
            contentDescription = "Displaying image for $imageName",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
            //.height(120.dp)

        )
    }
}

@Composable
fun ChooseCategoryDialog(
    articleTagRelationViewModel: ArticleTagRelationViewModel,
    articleTagsViewModel: ArticleTagsViewModel,
    onDismiss: () -> Unit
) {
    val articleTags by articleTagsViewModel.allTags.collectAsState(
        initial = emptyList()
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp)
            ) {
                Text(
                    text = "选择展示的知识分组",
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }, text = {
            // 使用LazyColumn显示卡片和复选框
            LazyColumn {
                items(articleTags) { articleTag ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        val tagRelation by articleTagRelationViewModel.getArticleTagRelationByTagId(
                            articleTag.tagID
                        ).collectAsState(initial = emptyList())
                        val isDisplay =
                            tagRelation.firstOrNull()?.isDisplayed ?: false  // 如果没有关联关系，默认不显示
                        Checkbox(
                            checked = isDisplay,
                            onCheckedChange = { isChecked ->
                                // 更新卡片显示状态
                                articleTagRelationViewModel.updateCategoryStatus(
                                    articleTag.tagID,
                                    isChecked
                                )
                            }
                        )
                        Text(
                            text = articleTag.tagName,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }, confirmButton = {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .width(120.dp)
                ) {
                    Text(
                        text = "保存设置", fontSize = 16.sp
                    )
                }
            }

        }
    )
}

