package com.example.healthapp.ui.ScreenLevel2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.R
import com.example.healthapp.data.entity.Article
import com.example.healthapp.data.viewmodel.ArticleMediaViewModel
import com.example.healthapp.data.viewmodel.ArticlesViewModel
import com.example.healthapp.data.viewmodel.AuthorsViewModel
import com.example.healthapp.ui.ScreenLevel1.ShowImageByName
import java.time.LocalDateTime

val defaultArticle = Article(
    articleID = 999,
    title = "",
    category = "",
    content = "",
    wordCount = 9999,
    authorID = 1,
    publishDate = LocalDateTime.now(),
    lastUpdated = LocalDateTime.now()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    articleId: Int,
    authorsViewModel: AuthorsViewModel,
    articleMediaViewModel: ArticleMediaViewModel,
    articlesViewModel: ArticlesViewModel,
    navController: NavHostController
) {

    val test by articlesViewModel.getArticleById(articleId).collectAsState(initial = null)
    val article = test ?: defaultArticle
    val author by authorsViewModel.getAuthorById(article.authorID).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = article.title,
                            fontSize = 20.sp,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                },
                navigationIcon = {
                    Button(
                        onClick = {
                            // 使用最简单的弹出堆栈
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface, // 设置按钮背景颜色与背景一致
                            contentColor = MaterialTheme.colorScheme.onSurface // 设置按钮内容颜色以确保可见性
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .width(80.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIos,
                                contentDescription = stringResource(R.string.browse_text),
                                tint = Color.Blue
                            )
                            Text(
                                text = stringResource(R.string.browse_text),
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
                    Button(
                        onClick = {
                            // 使用最简单的弹出堆栈
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface, // 设置按钮背景颜色与背景一致
                            contentColor = MaterialTheme.colorScheme.onSurface // 设置按钮内容颜色以确保可见性
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .width(80.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(
                                text = "收藏",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.Blue
                                )
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val image by
        articleMediaViewModel.getMediaByArticleId(articleId).collectAsState(initial = null)
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                // 作者与分类
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 14.dp)
                ) {
                    Text(
                        text = "文章分类：" + article.category + "\n"
                                + "作者：" + author?.name,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        ), color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp
                    )
                }
                // 正文
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
                ) {
                    ShowImageByName(
                        imageName = image?.filePath ?: "",
                        modifier = Modifier
                        //.clip(RoundedCornerShape(15.dp))
                    )
                    Text(
                        text = article.content,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                // 日期
                Card(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "文章字数:" + "${article.wordCount}",
                        fontSize = 16.sp,
                        color = Color.Blue,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            bottom = 4.dp,
                            top = 16.dp,
                            end = 16.dp
                        )
                    )
                    Text(
                        text = "发布时间:" + "${article.publishDate}",
                        fontSize = 16.sp,
                        color = Color.Blue,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            bottom = 4.dp,
                            top = 4.dp,
                            end = 16.dp
                        )
                    )
                    Text(
                        text = "更新时间:" + "${article.lastUpdated}",
                        fontSize = 16.sp,
                        color = Color.Blue,
                        modifier = Modifier.padding(
                            start = 16.dp,
                            bottom = 16.dp,
                            top = 4.dp,
                            end = 16.dp
                        )
                    )
                }
            }
        }
    }
}