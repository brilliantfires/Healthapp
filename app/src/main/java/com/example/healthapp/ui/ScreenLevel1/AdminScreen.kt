package com.example.healthapp.ui.ScreenLevel1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.healthapp.Navigation.AllUserInformationScreenDestination
import com.example.healthapp.data.AdminSection
import com.example.healthapp.data.entity.Article
import com.example.healthapp.data.entity.Author
import com.example.healthapp.data.entity.DisplayCard
import com.example.healthapp.data.entity.User
import com.example.healthapp.data.viewmodel.SyncViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    syncViewModel: SyncViewModel,
    navController: NavHostController
) {
    val users by syncViewModel.allUser.observeAsState(initial = emptyList())
    val userDisplayCards by syncViewModel.userDisplayCards.observeAsState(initial = emptyList())
    val userAuthors by syncViewModel.userAuthors.observeAsState(initial = emptyList())
    val userArticleTagRelations by syncViewModel.userArticleTagRelations.observeAsState(initial = emptyList())
    val userArticleTags by syncViewModel.userArticleTags.observeAsState(initial = emptyList())
    val userArticles by syncViewModel.userArticles.observeAsState(initial = emptyList())
    val userArticleMedia by syncViewModel.userArticleMedia.observeAsState(initial = emptyList())

    var selectedTag by remember { mutableStateOf(AdminSection.USERS) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "系统管理",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                )
                // 标签选择栏
                ScrollableTabRow(
                    selectedTabIndex = selectedTag.ordinal,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTag.ordinal])
                        )
                    }
                ) {
                    AdminSection.values().forEach { section ->
                        Tab(
                            selected = section == selectedTag,
                            onClick = { selectedTag = section },
                            text = { Text(text = section.name.replace("_", " ").capitalize()) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
            //.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            when (selectedTag) {
                AdminSection.ARTICLES -> ArticlesList(userArticles, syncViewModel)
                AdminSection.AUTHORS -> AuthorsList(userAuthors, syncViewModel)
                AdminSection.DISPLAY_CARDS -> DisplayCardsList(userDisplayCards, syncViewModel)
                AdminSection.USERS -> UserList(users, navController)
            }
        }
    }
}

@Composable
fun UserList(
    users: List<User>?,
    navController: NavHostController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        item {
            Text(
                text = "用户管理",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
        items(users ?: emptyList()) {
            UserCard(it, navController)
        }
    }
}

@Composable
fun UserCard(user: User, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = {
            // 显示用户详情
            navController.navigate(route = AllUserInformationScreenDestination.createRoute(user.userId))
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text("ID: ${user.userId}")
            Text("用户名: ${user.username}")
            Text("邮箱: ${user.email}")
        }
    }
}

@Composable
fun DisplayCardsList(displayCards: List<DisplayCard>?, syncViewModel: SyncViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        item {
            Text(
                text = "活动展示卡片管理",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
        items(displayCards ?: emptyList()) {
            DisplayCardItem(it, syncViewModel)
        }
    }
}

@Composable
fun DisplayCardItem(card: DisplayCard, syncViewModel: SyncViewModel) {
    var isDisplayed by remember { mutableStateOf(card.isDisplayed) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = card.cardName,
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = isDisplayed,
                onCheckedChange = { isChecked ->
                    isDisplayed = isChecked
                    // 在这里调用 ViewModel 来更新数据
                    val updateCard = card.copy(isDisplayed = isChecked)
                    syncViewModel.updateDisplayCard(updateCard)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.secondary,
                    uncheckedThumbColor = Color.Gray
                )
            )
        }
    }
}

@Composable
fun AuthorsList(authors: List<Author>?, syncViewModel: SyncViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        item {
            Text(
                text = "作者列表",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
        items(authors ?: emptyList()) { author ->
            AuthorCard(author, syncViewModel)
        }
    }
}


@Composable
fun AuthorCard(author: Author, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: ${author.authorID} - 名称: ${author.name}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { showEditDialog = true }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "编辑",
                        color = Color.Blue,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(2.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { expanded = !expanded }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (expanded) "收起" else "展开",
                        color = Color.Blue,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("简介: ${author.bio}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "作者头像: ${author.profilePicture}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    if (showEditDialog) {
        Dialog(onDismissRequest = { }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("编辑作者信息", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(20.dp))

                    // 状态变量，为每个字段记忆
                    val nameState = remember { mutableStateOf(author.name) }
                    val bioState = remember { mutableStateOf(author.bio) }
                    val profilePictureState = remember { mutableStateOf(author.profilePicture) }

                    // 输入字段
                    TextField(
                        value = nameState.value,
                        onValueChange = { nameState.value = it },
                        label = { Text("姓名") })
                    TextField(
                        value = bioState.value,
                        onValueChange = { bioState.value = it },
                        label = { Text("简介") })
                    TextField(
                        value = profilePictureState.value,
                        onValueChange = { profilePictureState.value = it },
                        label = { Text("头像链接") })

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val updatedAuthor = author.copy(
                                    name = nameState.value,
                                    bio = bioState.value,
                                    profilePicture = profilePictureState.value
                                )
                                syncViewModel.updateAuthor(updatedAuthor)
                                showEditDialog = false
                            }
                        ) {
                            Text("更新")
                        }
                        Button(
                            onClick = { showEditDialog = false }
                        ) {
                            Text("取消")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArticlesList(articles: List<Article>?, syncViewModel: SyncViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        item {
            Text(
                text = "文章列表",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
        items(articles ?: emptyList()) {
            ArticleCard(it, syncViewModel)
        }
    }
}

@Composable
fun ArticleCard(article: Article, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: ${article.articleID} - 标题: ${article.title}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(end = 0.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            showEditDialog = true
                        }
                ) {
                    Text(
                        text = "编辑",
                        color = Color.Blue,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(end = 0.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            expanded = !expanded
                        }
                ) {
                    Text(
                        text = if (expanded) "收起" else "展开",
                        color = Color.Blue,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            if (expanded) {
                Text("分类: ${article.category}", style = MaterialTheme.typography.titleMedium)
                Text("字数: ${article.wordCount}", style = MaterialTheme.typography.titleMedium)
                article.publishDate?.let {
                    Text(
                        "发布日期: ${it.format(DateTimeFormatter.ISO_DATE)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                article.lastUpdated?.let {
                    Text(
                        "最后更新: ${it.format(DateTimeFormatter.ISO_DATE)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text("内容:", style = MaterialTheme.typography.titleMedium)
                Text(text = article.content, style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    if (showEditDialog) {
        Dialog(onDismissRequest = { }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("编辑文章", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(20.dp))

                    // 状态变量，为每个字段记忆
                    val titleState = remember { mutableStateOf(article.title) }
                    val categoryState = remember { mutableStateOf(article.category) }
                    val contentState = remember { mutableStateOf(article.content) }
                    val wordCountState = remember { mutableStateOf(article.wordCount.toString()) }
                    val publishDateState = remember {
                        mutableStateOf(
                            article.publishDate?.toString() ?: ""
                        )
                    }
                    val lastUpdatedState = remember {
                        mutableStateOf(
                            article.lastUpdated?.toString() ?: ""
                        )
                    }

                    // 输入字段
                    TextField(
                        value = titleState.value,
                        onValueChange = { titleState.value = it },
                        label = { Text("标题") })
                    TextField(
                        value = categoryState.value,
                        onValueChange = { categoryState.value = it },
                        label = { Text("类别") })
                    TextField(
                        value = contentState.value,
                        onValueChange = { contentState.value = it },
                        label = { Text("内容") })
                    TextField(
                        value = wordCountState.value,
                        onValueChange = { wordCountState.value = it },
                        label = { Text("字数") })
                    TextField(
                        value = publishDateState.value,
                        onValueChange = { publishDateState.value = it },
                        label = { Text("发布日期 (YYYY-MM-DD)") })
                    TextField(
                        value = lastUpdatedState.value,
                        onValueChange = { lastUpdatedState.value = it },
                        label = { Text("最后更新日期 (YYYY-MM-DD)") })

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val updatedArticle = article.copy(
                                    title = titleState.value,
                                    category = categoryState.value,
                                    content = contentState.value,
                                    wordCount = wordCountState.value.toInt(),
                                    publishDate = LocalDateTime.parse(publishDateState.value),
                                    lastUpdated = LocalDateTime.parse(lastUpdatedState.value)
                                )
                                syncViewModel.updateArticle(updatedArticle)
                                showEditDialog = false
                            }
                        ) {
                            Text("更新")
                        }
                        Button(
                            onClick = { showEditDialog = false }
                        ) {
                            Text("取消")
                        }
                    }
                }
            }
        }
    }
}


