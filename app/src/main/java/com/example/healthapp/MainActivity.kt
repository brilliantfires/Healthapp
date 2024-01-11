@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.healthapp

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesomeMosaic
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthapp.data.viewmodel.WellnessTaskViewModel
import com.example.healthapp.data.viewmodel.WellnessTaskViewModelFactory
import com.example.healthapp.ui.theme.HealthappTheme


class MainActivity : ComponentActivity() {
    private val viewModel: WellnessTaskViewModel by viewModels {
        WellnessTaskViewModelFactory((application as HealthApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthappTheme {
//                //调用下面的可组合函数，Modifier.fillMaxSize()可以确保占满整个屏幕
//                MyApp(modifier = Modifier.fillMaxSize())

                TaskFormScreen(viewModel)
            }
        }
    }
}

data class TestHealthData(
    val title: String,
    val value: Int
)

val TestHealthDataList = listOf(
    TestHealthData("步数", 5000),
    TestHealthData("睡眠时间", 7),
    TestHealthData("心率", 72),
    TestHealthData("血压", 120 / 80),
    TestHealthData("体重", 65),
    TestHealthData("卡路里", 2000)
    // 后续添加
)

@Composable
//这里接受一个modifier来作为参数，modifier是一个用于修改组件外观的量
fun MyApp(modifier: Modifier = Modifier) {

    //表示一个状态，是否显示该引导界面
    //rememberSaveable 用于在配置更改时保存状态
    var shouldShowOnboarding by remember { mutableStateOf(true) }
    var isLoggedIn by remember { mutableStateOf(false) } // 新增状态

    if (!isLoggedIn) {
        // 显示登录界面
        LoginScreen(onLoginClicked = {
            isLoggedIn = it // 如果登录成功，则更新状态
        })
    } else {
        // 用户已登录，显示主界面
        Surface(modifier) {
            if (shouldShowOnboarding) {
                OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
            } else {
                Greetings()
            }
        }
    }
}

@Composable
//显示引导界面
fun OnboardingScreen(
    //一个 lambda 表达式，当用户点击“继续”按钮时被调用
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.first_screen_test))
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text(stringResource(R.string._continue))
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    healthData: List<TestHealthData> = listOf(
        TestHealthData("步数", 5000),
        TestHealthData("睡眠时间", 7),
        TestHealthData("心率", 72),
        TestHealthData("血压", 120 / 80),
        TestHealthData("体重", 65),
        TestHealthData("卡路里", 2000)
    )
) {
    //使用 LazyColumn 来高效地显示可滚动列表
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = healthData) { healthData ->
            Greeting(healthData = healthData)
        }
    }
}


//应当使用字符串资源，而不是使用硬编码字符串(也就是不要直接命名，统一在res中来定义)
@Composable
private fun Greeting(healthData: TestHealthData) {
    //是一个卡片组件，显示内容
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(healthData)
    }
}

@Composable
private fun CardContent(healthData: TestHealthData) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = healthData.title)
            Text(
                text = healthData.value.toString(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (expanded) {
                Text(
                    text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }
        //是一个可组合函数，用于创建一个图标按钮
        //expanded 用于控制某些内容的展开和折叠
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                //属性决定了要显示哪个图标,这里使用了条件表达式来根据 expanded 的值选择图标
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                //提供了对图标的文字描述,描述也是根据 expanded 的状态动态选择的
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}

//创建一个搜索栏，用来搜索活动
//搜索之后会展示什么呢........
@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        //这里使用的最新的material3的colors特性
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

// 对齐组件,也就是展示一个块，这个块的上半部分是图片，下半部分是标题，可以在后面继续重用
//考虑展示运动方式？健身方式
@Composable
fun AlignYourBodyElement(
    //图像资源
    @DrawableRes drawable: Int,
    //字符资源
    @StringRes text: Int,
    //UI修改器
    modifier: Modifier = Modifier
) {
    //表示一列，该列上面是image，下面是Text
    Column(
        modifier = modifier,
        //该列的子元素在水平方向上居中对齐
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            //从资源ID加载图像
            painter = painterResource(drawable),
            contentDescription = null,
            //定义图像如何适应分配的空间,Crop只是其中一种形式，这里是将重心区域缩放到容器大小
            //FillBounds 缩放以适应容器大小，不保持图像的原始宽高比
            //Fit 不裁切，完全适应容器大小
            contentScale = ContentScale.Crop,
            modifier = Modifier
                //定义88dp的圆形，clip
                .size(88.dp)
                .clip(CircleShape)
        )
        Text(
            //从资源ID加载string类型的数据
            text = stringResource(text),
            modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}


// 考虑以后使用该部分实现卡片展示的内容，收藏的资料的展示？
@Composable
fun FavoriteCollectionCard(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    //基础的卡片样式容器
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Row(
            //子元素在垂直方向上居中
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(255.dp)
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = stringResource(text),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}


// 创建一个水平滚动的行，来展示AlignYourBodyElement的元素
@Composable
fun AlignYourBodyRow(
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
    ) {
        //这里的alignYourBodyData是需要在后面定义的输入数据，需要定义输入的图片资源以及文字信息
        items(alignYourBodyData) { item ->
            AlignYourBodyElement(item.drawable, item.text)
        }
    }
}

// Step: Favorite collections grid - LazyGrid
@Composable
fun FavoriteCollectionsGrid(
    modifier: Modifier = Modifier
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.height(168.dp)
    ) {
        //待输入的卡片图像资源以及文字字符串资源
        items(favoriteCollectionsData) { item ->
            FavoriteCollectionCard(item.drawable, item.text, Modifier.height(80.dp))
        }
    }
}

// 具有标题和自定义内容区域的 UI 部分
@Composable
fun HomeSection(
    //需要字符串资源
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    //返回Composable 的内容块
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(title),
            //指定标题文本的样式
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
        )
        //允许调用 HomeSection 的地方插入任何自定义的 Composable 内容
        content()
    }
}

// 创建一个可滚动的主屏幕布局，包含多个UI组件和部分
// 适合于应用程序的主页或仪表板
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        //这里的资源都是要在上面导入的，而且IDE可能都无法识别，还需要自己手动导入
        //创建一个列布局，用于垂直排列子元素,而且这个列是比较特别，在显示不全的时候，可以滚动查看，确保可以完整展示
        modifier.verticalScroll(rememberScrollState())
    ) {
        //创建额外的垂直间隔
        Spacer(Modifier.height(16.dp))
        //添加上搜索栏
        SearchBar(Modifier.padding(horizontal = 16.dp))
        //第一个水平滚动的 带标题的内容区域
        HomeSection(title = R.string.align_your_body) {
            //之前定义的水平的 Composable
            AlignYourBodyRow()
        }
        //第二个水平滚动的 带标题的内容区域
        HomeSection(title = R.string.favorite_collections) {
            //垂直 两行 可水平滚动的 Composable
            FavoriteCollectionsGrid()
        }
        //创建额外的垂直间隔
        Spacer(Modifier.height(16.dp))
    }
}

// 创建一个底部导航栏 导航栏：Navigation
@Composable
private fun HealthBottomNavigation(modifier: Modifier = Modifier) {
    // 使用 NavigationBar 创建一个容器，用于放置导航项
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier // 应用外部传入的 UI 修改器
    ) {
        // 添加一个 NavigationBarItem 作为导航的一个选项
        NavigationBarItem(
            icon = {
                // 为这个导航项设置一个图标
                Icon(
                    imageVector = Icons.Default.AutoAwesomeMosaic,    // 设置图标为 Spa 图标
                    contentDescription = null   // 图标描述为空，因为只是装饰性的而已
                )
            },
            // 为导航项设置标签文本
            label = {
                Text(stringResource(R.string.bottom_summary_text))
            },
            selected = false,    // 设置这个导航项为选中状态
            onClick = {}        // 点击事件的处理逻辑，当前为空，以后来实现
        )
        // 添加一个 NavigationBarItem 作为导航的一个选项
        NavigationBarItem(
            icon = {
                // 为这个导航项设置一个图标
                Icon(
                    imageVector = Icons.Default.Spa,    // 设置图标为 Spa 图标
                    contentDescription = null   // 图标描述为空，因为只是装饰性的而已
                )
            },
            // 为导航项设置标签文本
            label = {
                Text(stringResource(R.string.bottom_navigation_browse))
            },
            selected = true,    // 设置这个导航项为选中状态
            onClick = {}        // 点击事件的处理逻辑，当前为空，以后来实现
        )
        // 添加第二个 NavigationBarItem 作为另一个导航选项
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.bottom_navigation_home))
            },
            selected = false,
            onClick = {}
        )
    }
}

// 创建一个垂直的导航栏,因为在横屏或者是平板上时，我们的应用不能还是和竖屏一样的布局，那样对用户的体验会不好
@Composable
private fun HealthNavigationRail(modifier: Modifier = Modifier) {
    // 使用 NavigationRail 创建一个容器，用于放置导航项
    // 这里和上面的 NavigationBar 是有区别的，Bar是一般用于水平的导航栏，而rail是用于垂直的导航栏
    NavigationRail(
        modifier = modifier.padding(start = 8.dp, end = 8.dp),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = modifier.fillMaxHeight(),    // 设置列的高度填充其父容器
            verticalArrangement = Arrangement.Center,   // 垂直居中排列子元素
            horizontalAlignment = Alignment.CenterHorizontally  // 水平居中对齐子元素
        ) {
            // 添加一个 NavigationRailItem 作为导航的一个选项
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.AutoAwesomeMosaic,
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.bottom_summary_text))
                },
                selected = false,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            // 添加一个 NavigationRailItem 作为导航的一个选项
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.bottom_navigation_browse))
                },
                selected = true,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.bottom_navigation_home))
                },
                selected = false,
                onClick = {}
            )
        }
    }
}

// 好的应用应该能够根据窗口大小调整应用布局
// 适用与适应不同的尺寸
@Composable
fun MyHealthApp(windowSize: WindowSizeClass) {
    // 根据窗口宽度大小类别决定使用的布局
    when (windowSize.widthSizeClass) {
        // 当窗口宽度为 Compact（紧凑）时，使用纵向版布局
        WindowWidthSizeClass.Compact -> {
            MyHealthAppPortrait()
        }
        // 当窗口宽度为 Expanded（扩展）时，使用横向版布局
        //一般就是只使用紧凑和扩展而不适用 medium
        WindowWidthSizeClass.Expanded -> {
            MyHealthAppLandscape()
        }
    }
}

// 创建一个纵向版（Portrait）的布局，专门适用于手机或竖屏设备
// 使用上面包装的 HealthBottomNavigation 接口
@Composable
fun MyHealthAppPortrait() {
    // 应用主题样式
    HealthappTheme {
        // Scaffold 结构提供了一个基本的屏幕布局
        Scaffold(
            // 定义底部导航栏
            bottomBar = { HealthBottomNavigation() }
        ) { padding ->
            HomeScreen(Modifier.padding(padding))
        }
    }
}

// 创建了一个适用于横屏设备或宽屏幕的应用布局
// 使用上面包装的接口 HealthNavigationRail
@Composable
fun MyHealthAppLandscape() {
    HealthappTheme {
        // 使用 Surface 组件为屏幕提供一个背景颜色
        // 这里为什么不使用 scaffold？ 因为 scaffold提供了一个更加标准的导航栏组件
        // 而 surface 组件呢？更加自由，可以更好的调节布局，在横版布局需要更大的自由度来定制和优化横向布局
        Surface(color = MaterialTheme.colorScheme.background) {
            Row {
                HealthNavigationRail()
                HomeScreen()
            }
        }
    }
}

private val alignYourBodyData = listOf(
    R.drawable.ab1_inversions to R.string.ab1_inversions,
    R.drawable.ab2_quick_yoga to R.string.ab2_quick_yoga,
    R.drawable.ab3_stretching to R.string.ab3_stretching,
    R.drawable.ab4_tabata to R.string.ab4_tabata,
    R.drawable.ab5_hiit to R.string.ab5_hiit,
    R.drawable.ab6_pre_natal_yoga to R.string.ab6_pre_natal_yoga
).map { DrawableStringPair(it.first, it.second) }

// 定义一个列表，包含“最爱收藏”部分的数据
private val favoriteCollectionsData = listOf(
    // 每个元素是一个资源对：图像资源ID和对应的字符串资源ID
    R.drawable.fc1_short_mantras to R.string.fc1_short_mantras,
    R.drawable.fc2_nature_meditations to R.string.fc2_nature_meditations,
    R.drawable.fc3_stress_and_anxiety to R.string.fc3_stress_and_anxiety,
    R.drawable.fc4_self_massage to R.string.fc4_self_massage,
    R.drawable.fc5_overwhelmed to R.string.fc5_overwhelmed,
    R.drawable.fc6_nightly_wind_down to R.string.fc6_nightly_wind_down
    // 将每个资源对映射成 DrawableStringPair 对象
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val drawable: Int,
    @StringRes val text: Int
)

@Composable
fun LoginScreen(onLoginClicked: (Boolean) -> Unit) {
    // 定义用户名和密码的状态
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 定义登录按钮的点击事件
    val onLoginClicked = {
        // 验证用户名和密码是否正确
        if (username == "admin" && password == "123456") {
            // 登录成功，跳转到主界面
            // TODO: 实现跳转逻辑
        } else {
            // 登录失败，弹出提示
            // TODO: 实现提示逻辑
        }
    }

    // 使用Column布局来垂直排列组件
    Column(
        modifier = Modifier
            .fillMaxSize() // 填充满屏幕
            .padding(16.dp), // 设置内边距为16dp
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 使用Spacer组件来添加间隙
        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分

        // 创建用户名输入框
        TextField(
            value = username, // 绑定用户名状态
            onValueChange = { username = it }, // 当文本变化时，更新用户名状态
            label = { Text(stringResource(R.string.username_hint)) }, // 设置标签文本为“用户名”
            singleLine = true, // 设置为单行输入
            colors = TextFieldDefaults.colors( // 设置颜色
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
            )
        )

        // 添加16dp的间隙
        Spacer(modifier = Modifier.height(16.dp))

        // 创建密码输入框
        TextField(
            value = password, // 绑定密码状态
            onValueChange = { password = it }, // 当文本变化时，更新密码状态
            label = { Text(stringResource(R.string.password_hint)) }, // 设置标签文本为“密码”
            singleLine = true, // 设置为单行输入
            visualTransformation = PasswordVisualTransformation(), // 设置文本的可视化转换为密码形式
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // 设置键盘类型为密码键盘
            colors = TextFieldDefaults.colors( // 设置颜色
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
            )
        )

        // 添加16dp的间隙
        Spacer(modifier = Modifier.height(16.dp))

        // 创建登录按钮
        Button(
            onClick = {
                // 假设用户名和密码验证逻辑
                val isSuccess = username == "admin" && password == "123456"
                onLoginClicked(isSuccess)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally) // 设置对齐方式为右对齐
        ) {
            Text("登录") // 设置按钮文本为“登录”
        }

        // 使用Spacer组件来添加间隙
        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分
    }
}

// 通过正则表达式来判断邮箱的格式是否为合法
// 使用 matches() 方法来检查邮箱地址是否与正则表达式匹配
fun isValidEmail(email: String): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return email.matches(emailRegex)
}

@Composable
fun RegisterScreen() {
    // 定义用户名、密码、确认密码和邮箱的状态
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // 定义注册按钮的点击事件
    val onRegisterClicked = {
        // 验证用户名、密码、确认密码和邮箱是否合法
        if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword == password && isValidEmail(
                email
            )
        ) {
            // 注册成功，跳转到主界面
            // TODO: 实现跳转逻辑
        } else {
            // 注册失败，弹出提示
            // TODO: 实现提示逻辑
        }
    }

    // 使用Column布局来垂直排列组件
    Column(
        modifier = Modifier
            .fillMaxSize() // 填充满屏幕
            .padding(16.dp), // 设置内边距为16dp
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 使用Spacer组件来添加间隙
        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分

        // 创建用户名输入框
        TextField(
            value = username, // 绑定用户名状态
            onValueChange = { username = it }, // 当文本变化时，更新用户名状态
            label = { Text(stringResource(R.string.username_hint)) }, // 设置标签文本为“用户名”
            singleLine = true, // 设置为单行输入
            colors = TextFieldDefaults.colors( // 设置颜色
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
            )
        )

        // 添加16dp的间隙
        Spacer(modifier = Modifier.height(16.dp))

        // 创建密码输入框
        TextField(
            value = password, // 绑定密码状态
            onValueChange = { password = it }, // 当文本变化时，更新密码状态
            label = { Text(stringResource(R.string.password_hint)) }, // 设置标签文本为“密码”
            singleLine = true, // 设置为单行输入
            visualTransformation = PasswordVisualTransformation(), // 设置文本的可视化转换为密码形式
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // 设置键盘类型为密码键盘
            colors = TextFieldDefaults.colors( // 设置颜色
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
            )
        )

        // 添加16dp的间隙
        Spacer(modifier = Modifier.height(16.dp))

        // 创建确认密码输入框
        TextField(
            value = confirmPassword, // 绑定确认密码状态
            onValueChange = { confirmPassword = it }, // 当文本变化时，更新确认密码状态
            label = { Text(stringResource(R.string.confirm_password_hint)) }, // 设置标签文本为“确认密码”
            singleLine = true, // 设置为单行输入
            visualTransformation = PasswordVisualTransformation(), // 设置文本的可视化转换为密码形式
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // 设置键盘类型为密码键盘
            colors = TextFieldDefaults.colors( // 设置颜色
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
            )
        )

        // 添加16dp的间隙
        Spacer(modifier = Modifier.height(16.dp))

        // 创建邮箱输入框
        TextField(
            value = email, // 绑定邮箱状态
            onValueChange = { email = it }, // 当文本变化时，更新邮箱状态
            label = { Text(stringResource(R.string.email_hint)) }, // 设置标签文本为“邮箱”
            singleLine = true, // 设置为单行输入
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // 设置键盘类型为邮箱键盘
            colors = TextFieldDefaults.colors( // 设置颜色
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
            )
        )

        // 添加16dp的间隙
        Spacer(modifier = Modifier.height(16.dp))

        // 创建注册按钮
        Button(
            onClick = onRegisterClicked, // 设置点击事件为注册按钮的点击事件
            modifier = Modifier.align(Alignment.CenterHorizontally) // 设置对齐方式为右对齐
        ) {
            Text(stringResource(R.string.register_button_text)) // 设置按钮文本为“注册”
        }

        // 使用Spacer组件来添加间隙
        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分
    }
}

fun isValidPhone(phone: String): Boolean {
    val phoneRegex = "1[3-9]\\d{9}".toRegex()
    return phone.matches(phoneRegex)
}

@Composable
fun ResetPasswordScreen() {
    // 定义手机号、验证码、新密码和确认新密码的状态
    var phone by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    // 定义发送验证码和重置密码的点击事件
    val onSendCodeClicked = {
        // 验证手机号是否合法
        if (isValidPhone(phone)) {
            // 发送验证码到手机号
            // TODO: 实现发送逻辑
        } else {
            // 手机号不合法，弹出提示
            // TODO: 实现提示逻辑
        }
    }

    val onResetPasswordClicked = {
        // 验证验证码、新密码和确认新密码是否合法
        if (code.isNotEmpty() && newPassword.isNotEmpty() && confirmNewPassword == newPassword) {
            // 重置密码成功，跳转到登录界面
            // TODO: 实现跳转逻辑
        } else {
            // 重置密码失败，弹出提示
            // TODO: 实现提示逻辑
        }
    }

    // 使用Column布局来垂直排列组件
    Column(
        modifier = Modifier
            .fillMaxSize() // 填充满屏幕
            .padding(16.dp), // 设置内边距为16dp
        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 使用Spacer组件来添加间隙
        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分

        // 创建手机号输入框
        TextField(
            value = phone, // 绑定手机号状态
            onValueChange = { phone = it }, // 当文本变化时，更新手机号状态
            label = { Text("手机号") }, // 设置标签文本为“手机号”
            singleLine = true, // 设置为单行输入
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // 设置键盘类型为手机号键盘
            colors = TextFieldDefaults.colors( // 设置颜色
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
            )
        )

        // 添加16dp的间隙
        Spacer(modifier = Modifier.height(16.dp))

        // 创建验证码输入框和发送验证码按钮,这两个恰好是横着的
        Row(
            modifier = Modifier.fillMaxWidth(), // 填充满宽度
            horizontalArrangement = Arrangement.SpaceBetween // 水平排列方式为两端对齐
        ) {
            // 创建验证码输入框
            TextField(
                value = code, // 绑定验证码状态
                onValueChange = { code = it }, // 当文本变化时，更新验证码状态
                label = { Text("验证码") }, // 设置标签文本为“验证码”
                singleLine = true, // 设置为单行输入
                modifier = Modifier.weight(1f)
            )

            //添加 8dp 的间隔空间
            Spacer(modifier = Modifier.width(8.dp))

            //创建发送验证码按钮
            Button(
                onClick = onSendCodeClicked,
                modifier = Modifier.width(80.dp)
            ) {
                Text(stringResource(R.string.send_verification_code_button_text))
            }
        }// end Row

        // 同一列中继续添加 16 dp的空间
        Spacer(modifier = Modifier.height(16.dp))

        // 创建新密码输入框
        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text(stringResource(R.string.new_password_text)) }, // 设置标签文本为“新密码”
            singleLine = true, // 设置为单行输入
            visualTransformation = PasswordVisualTransformation(), // 设置文本的可视化转换为密码形式
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // 设置键盘类型为密码键盘
            colors = TextFieldDefaults.colors( // 设置颜色
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
            )
        )

        // 添加16dp的间隙
        Spacer(modifier = Modifier.height(16.dp))

        // 创建确认新密码输入框
        TextField(
            value = confirmNewPassword, // 绑定确认新密码状态
            onValueChange = { confirmNewPassword = it }, // 当文本变化时，更新确认新密码状态
            label = { Text(stringResource(R.string.confirm_new_password_text)) }, // 设置标签文本为“确认新密码”
            singleLine = true, // 设置为单行输入
            visualTransformation = PasswordVisualTransformation(), // 设置文本的可视化转换为密码形式
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // 设置键盘类型为密码键盘
            colors = TextFieldDefaults.colors( // 设置颜色
                focusedIndicatorColor = Color.Blue, // 聚焦时的指示器颜色为蓝色
                unfocusedIndicatorColor = Color.Gray // 失焦时的指示器颜色为灰色
            )
        )

        // 添加16dp的间隙
        Spacer(modifier = Modifier.height(16.dp))

        // 创建重置密码按钮
        Button(
            onClick = onResetPasswordClicked, // 设置点击事件为重置密码的点击事件
            modifier = Modifier.align(Alignment.CenterHorizontally) // 设置对齐方式为右对齐
        ) {
            Text(stringResource(R.string.reset_password_text)) // 设置按钮文本为“重置密码”
        }

        // 使用Spacer组件来添加间隙
        Spacer(modifier = Modifier.weight(1f)) // 权重为1，表示占用剩余空间的一部分
    }
}// end Column


@Preview(name = "搜索栏", showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun SearchBarPreview() {
    HealthappTheme { SearchBar(Modifier.padding(8.dp)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun AlignYourBodyElementPreview() {
    HealthappTheme {
        AlignYourBodyElement(
            text = R.string.ab1_inversions,
            drawable = R.drawable.ab1_inversions,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun FavoriteCollectionCardPreview() {
    HealthappTheme {
        FavoriteCollectionCard(
            text = R.string.fc2_nature_meditations,
            drawable = R.drawable.fc2_nature_meditations,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun FavoriteCollectionsGridPreview() {
    HealthappTheme { FavoriteCollectionsGrid() }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun AlignYourBodyRowPreview() {
    HealthappTheme { AlignYourBodyRow() }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun HomeSectionPreview() {
    HealthappTheme {
        HomeSection(R.string.align_your_body) {
            AlignYourBodyRow()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE, heightDp = 180)
@Composable
fun ScreenContentPreview() {
    HealthappTheme { HomeScreen() }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun BottomNavigationPreview() {
    HealthappTheme { HealthBottomNavigation(Modifier.padding(top = 24.dp)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun NavigationRailPreview() {
    HealthappTheme { HealthNavigationRail() }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun MyHealthPortraitPreview() {
    MyHealthAppPortrait()
}

@Preview(widthDp = 640, heightDp = 360)
@Composable
fun MyHealthLandscapePreview() {
    MyHealthAppLandscape()
}

@Preview(
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "Dark",
    showBackground = true
)
@Composable
fun DefaultPreview() {
    HealthappTheme {
        Greetings()
    }
}
@Preview(
    uiMode = UI_MODE_NIGHT_YES,
    name = "Dark",
    showBackground = true, device = "id:Nexus 10"
)
@Composable
fun MyHealthLandscapeDarkPreview()
{
    HealthappTheme {
        MyHealthLandscapePreview()
    }
}


@Preview
@Composable
fun MyAppPreview() {
    HealthappTheme {
        MyApp(Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    HealthappTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}

@Preview
@Composable
fun RegisterPreview(){
    HealthappTheme {
        RegisterScreen()
    }
}

@Preview
@Composable
fun ResetPasswordPreview(){
    HealthappTheme {
        ResetPasswordScreen()
    }
}