package com.example.healthapp.ui.ScreenLevel2

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.healthapp.R
import com.example.healthapp.data.entity.DailyActivity
import com.example.healthapp.data.entity.ExerciseRecord
import com.example.healthapp.data.entity.HealthIndicator
import com.example.healthapp.data.entity.MedicationRecord
import com.example.healthapp.data.entity.NutritionRecord
import com.example.healthapp.data.entity.PhysicalProfile
import com.example.healthapp.data.entity.SleepRecord
import com.example.healthapp.data.entity.User
import com.example.healthapp.data.viewmodel.SyncViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllUserInformationScreen(
    userId: Int,
    syncViewModel: SyncViewModel,
    navController: NavHostController
) {
    val user by syncViewModel.userById.observeAsState(initial = null)
    syncViewModel.getUserById(userId)
    val userPhysicalProfile by syncViewModel.userPhysicalProfile.observeAsState(initial = null)
    syncViewModel.getPhysicalProfileByUserId(userId)
    val userSleepRecords by syncViewModel.userSleepRecords.observeAsState(initial = emptyList())
    syncViewModel.getSleepRecordByUserId(userId)
    val userNutritionRecords by syncViewModel.userNutritionRecords.observeAsState(initial = emptyList())
    syncViewModel.getNutritionRecordsByUserId(userId)
    val userMedicationRecords by syncViewModel.userMedicationRecords.observeAsState(initial = emptyList())
    syncViewModel.getMedicationRecordsByUserId(userId)
    val userHealthIndicators by syncViewModel.userHealthIndicators.observeAsState(initial = emptyList())
    syncViewModel.getHealthIndicatorsByUserId(userId)
    val userExerciseRecords by syncViewModel.userExerciseRecords.observeAsState(initial = emptyList())
    syncViewModel.getExerciseRecordsByUserId(userId)
    val userDailyActivities by syncViewModel.userDailyActivities.observeAsState(initial = emptyList())
    syncViewModel.getDailyActivitiesByUserId(userId)



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "用户管理",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    Button(
                        onClick = {
                            // showDialog = true
                            navController.popBackStack()
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
                            Icon(
                                imageVector = Icons.Filled.ArrowBackIos,
                                contentDescription = stringResource(id = R.string.back_text),
                                tint = Color.Blue
                            )
                            Text(
                                text = "返回",
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

                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                user?.let {
                    UserCard(it, syncViewModel)
                }
                userPhysicalProfile?.let {
                    PhysicalProfileCard(it, syncViewModel)
                }
                SleepRecordsList(userSleepRecords, syncViewModel)
                NutritionRecordsList(userNutritionRecords, syncViewModel)
                MedicationRecordsList(userMedicationRecords, syncViewModel)
                HealthIndicatorsList(userHealthIndicators, syncViewModel)
                ExerciseRecordsList(userExerciseRecords, syncViewModel)
                DailyActivityRecordsList(userDailyActivities, syncViewModel)
            }
        }
    }
}

@Composable
fun UserCard(user: User, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }  // 记录是否展开的状态
    var showEdit by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier
            .clickable { expanded = !expanded }
            .animateContentSize()  // 添加动画效果使内容展开收缩平滑
            .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "用户ID: ${user.userId}   -   用户名: ${user.username}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding()
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(end = 0.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            showEdit = true
                        }
                ) {
                    Text(
                        text = "编辑",
                        color = Color.Blue,
                        fontSize = 20.sp,
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
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            Text(
                text = "邮箱: ${user.email}",
                style = MaterialTheme.typography.titleMedium
            )

            // 展开部分显示更多信息
            if (expanded) {
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    "手机: ${user.phoneNumber ?: "Not available"}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text("角色: ${user.role}", style = MaterialTheme.typography.titleMedium)
                user.bloodType?.let {
                    Text(
                        "血型: $it",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                user.gender?.let {
                    Text(
                        "性别: $it",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                user.dateOfBirth?.let {
                    Text(
                        "生日: ${it.format(DateTimeFormatter.ISO_DATE)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    "头像: ${user.profilePicture}",
                    style = MaterialTheme.typography.titleMedium
                )
                user.lastLogin?.let {
                    Text(
                        "上次登陆: ${it.format(DateTimeFormatter.ISO_DATE_TIME)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                user.isWheelchairUser?.let {
                    Text(
                        "是否乘坐轮椅: ${if (it) "Yes" else "No"}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                user.skinType?.let {
                    Text(
                        "日光反应皮肤类型: $it",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                user.heartRateAffectingDrugs?.let {
                    Text(
                        "心率相关药物: $it",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }

    if (showEdit) {
        Dialog(
            onDismissRequest = {
                showEdit = false
            }
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Edit User", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(20.dp))
                    val usernameState = remember { mutableStateOf(user.username) }
                    val emailState = remember { mutableStateOf(user.email) }
                    val phoneNumberState = remember { mutableStateOf(user.phoneNumber ?: "") }
                    val passwordHashState = remember { mutableStateOf(user.passwordHash) }
                    val profilePictureState = remember { mutableStateOf(user.profilePicture) }
                    val roleState = remember { mutableStateOf(user.role) }
                    val bloodTypeState = remember { mutableStateOf(user.bloodType ?: "") }
                    val genderState = remember { mutableStateOf(user.gender ?: "") }
                    val dateOfBirthState =
                        remember { mutableStateOf(user.dateOfBirth?.toString() ?: "") }
                    val isWheelchairUserState =
                        remember { mutableStateOf(user.isWheelchairUser ?: false) }
                    val skinTypeState = remember { mutableStateOf(user.skinType ?: "") }
                    val heartRateAffectingDrugsState =
                        remember { mutableStateOf(user.heartRateAffectingDrugs ?: "") }

                    // Fields
                    TextField(
                        value = usernameState.value,
                        onValueChange = { usernameState.value = it },
                        label = { Text("Username") })
                    TextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        label = { Text("Email") })
                    TextField(
                        value = phoneNumberState.value,
                        onValueChange = { phoneNumberState.value = it },
                        label = { Text("Phone Number") })
                    TextField(
                        value = passwordHashState.value,
                        onValueChange = { passwordHashState.value = it },
                        label = { Text("Password Hash") })
                    TextField(
                        value = profilePictureState.value,
                        onValueChange = { profilePictureState.value = it },
                        label = { Text("Profile Picture") })
                    TextField(
                        value = roleState.value,
                        onValueChange = { roleState.value = it },
                        label = { Text("Role") })
                    TextField(
                        value = bloodTypeState.value,
                        onValueChange = { bloodTypeState.value = it },
                        label = { Text("Blood Type") })
                    TextField(
                        value = genderState.value,
                        onValueChange = { genderState.value = it },
                        label = { Text("Gender") })
                    TextField(
                        value = dateOfBirthState.value,
                        onValueChange = { dateOfBirthState.value = it },
                        label = { Text("Date of Birth") })
                    TextField(
                        value = isWheelchairUserState.value.toString(),
                        onValueChange = { isWheelchairUserState.value = it.toBoolean() },
                        label = { Text("Wheelchair User") })
                    TextField(
                        value = skinTypeState.value,
                        onValueChange = { skinTypeState.value = it },
                        label = { Text("Skin Type") })
                    TextField(
                        value = heartRateAffectingDrugsState.value,
                        onValueChange = { heartRateAffectingDrugsState.value = it },
                        label = { Text("Heart Rate Affecting Drugs") })

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                // 正确使用 copy 方法来创建新的 User 对象，并从状态中动态获取值
                                val updatedUser = user.copy(
                                    username = usernameState.value,
                                    email = emailState.value,
                                    phoneNumber = phoneNumberState.value,
                                    passwordHash = passwordHashState.value,
                                    profilePicture = profilePictureState.value,
                                    role = roleState.value,
                                    bloodType = bloodTypeState.value,
                                    gender = genderState.value,
                                    dateOfBirth = LocalDateTime.parse(dateOfBirthState.value),
                                    isWheelchairUser = isWheelchairUserState.value,
                                    skinType = skinTypeState.value,
                                    heartRateAffectingDrugs = heartRateAffectingDrugsState.value
                                )

                                // 使用正确的对象来调用更新函数
                                syncViewModel.updateUser(updatedUser)
                                showEdit = false
                            }
                        ) {
                            Text("更新")
                        }
                        Button(
                            onClick = { showEdit = false }
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
fun PhysicalProfileCard(physicalProfile: PhysicalProfile?, syncViewModel: SyncViewModel) {
    if (physicalProfile == null) {
        Text("No physical profile data available", style = MaterialTheme.typography.titleMedium)
        return
    }

    var expanded by remember { mutableStateOf(false) }  // 记录是否展开的状态
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .animateContentSize()  // 添加动画效果使内容展开收缩平滑
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "身体信息  -  用户ID: ${physicalProfile.userID}",
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
                        fontSize = 20.sp,
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
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            // 展开部分显示更多信息
            if (expanded) {
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    "身高: ${physicalProfile.height} cm",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "体重: ${physicalProfile.weight} kg",
                    style = MaterialTheme.typography.titleMedium
                )
                Text("BMI: ${physicalProfile.bmi}", style = MaterialTheme.typography.titleMedium)
                physicalProfile.myopiaDegree?.let {
                    Text("近视度数: $it 度", style = MaterialTheme.typography.titleMedium)
                }
                Text(
                    "是否戴眼镜: ${if (physicalProfile.wearsGlasses) "是" else "否"}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "血型: ${physicalProfile.bloodType}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "鞋码: ${physicalProfile.shoeSize}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
    if (showEditDialog) {
        Dialog(onDismissRequest = {}) {
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
                ) {
                    Text(
                        "Edit Physical Profile",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // State variables for each field
                    val heightState = remember { mutableStateOf(physicalProfile.height.toString()) }
                    val weightState = remember { mutableStateOf(physicalProfile.weight.toString()) }
                    val bmiState = remember { mutableStateOf(physicalProfile.bmi.toString()) }
                    val myopiaDegreeState =
                        remember { mutableStateOf(physicalProfile.myopiaDegree?.toString() ?: "") }
                    val wearsGlassesState =
                        remember { mutableStateOf(physicalProfile.wearsGlasses) }
                    val bloodTypeState = remember { mutableStateOf(physicalProfile.bloodType) }
                    val shoeSizeState =
                        remember { mutableStateOf(physicalProfile.shoeSize.toString()) }

                    // Text fields for each attribute
                    TextField(
                        value = heightState.value,
                        onValueChange = { heightState.value = it },
                        label = { Text("Height (cm)") })
                    TextField(
                        value = weightState.value,
                        onValueChange = { weightState.value = it },
                        label = { Text("Weight (kg)") })
                    TextField(
                        value = bmiState.value,
                        onValueChange = { bmiState.value = it },
                        label = { Text("BMI") })
                    TextField(
                        value = myopiaDegreeState.value,
                        onValueChange = { myopiaDegreeState.value = it },
                        label = { Text("Myopia Degree") })
                    Switch(
                        checked = wearsGlassesState.value,
                        onCheckedChange = { wearsGlassesState.value = it },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(if (wearsGlassesState.value) "Wears Glasses: Yes" else "Wears Glasses: No")
                    TextField(
                        value = bloodTypeState.value,
                        onValueChange = { bloodTypeState.value = it },
                        label = { Text("Blood Type") })
                    TextField(
                        value = shoeSizeState.value,
                        onValueChange = { shoeSizeState.value = it },
                        label = { Text("Shoe Size (EU)") })

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val updatedProfile = physicalProfile.copy(
                                    height = heightState.value.toDouble(),
                                    weight = weightState.value.toDouble(),
                                    bmi = bmiState.value.toDouble(),
                                    myopiaDegree = myopiaDegreeState.value.toDoubleOrNull(),
                                    wearsGlasses = wearsGlassesState.value,
                                    bloodType = bloodTypeState.value,
                                    shoeSize = shoeSizeState.value.toDouble()
                                )
                                syncViewModel.updatePhysicalProfile(updatedProfile)
                                showEditDialog = false
                            }
                        ) {
                            Text("更新")
                        }
                        Button(
                            onClick = {
                                showEditDialog = false
                            }
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
fun SleepRecordsList(sleepRecords: List<SleepRecord>?, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .animateContentSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("睡眠记录", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
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
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            if (expanded) {
                sleepRecords?.forEach { sleepRecord ->
                    SleepRecordCard(sleepRecord, syncViewModel)
                }
            }
        }
    }
}

@Composable
fun SleepRecordCard(sleepRecord: SleepRecord, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { expanded = !expanded }
            .animateContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "用户ID: ${sleepRecord.userID} - 日期: ${sleepRecord.date.format(DateTimeFormatter.ISO_DATE)}",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "编辑"
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "收起" else "展开"
                )
            }
        }

        if (expanded) {
            Text(
                "总睡眠时间: ${sleepRecord.totalDuration}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "深度睡眠时间: ${sleepRecord.deepSleep}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "浅睡时间: ${sleepRecord.lightSleep}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "REM睡眠时间: ${sleepRecord.remSleep}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "清醒时间: ${sleepRecord.awakeDuration}",
                style = MaterialTheme.typography.titleMedium
            )
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
                ) {
                    Text("Edit Sleep Record", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(20.dp))

                    // State variables for each field
                    val dateState =
                        remember { mutableStateOf(sleepRecord.date.toString() ?: "") }
                    val totalDurationState =
                        remember { mutableStateOf(sleepRecord.totalDuration ?: "") }
                    val deepSleepState = remember { mutableStateOf(sleepRecord.deepSleep ?: "") }
                    val lightSleepState = remember { mutableStateOf(sleepRecord.lightSleep ?: "") }
                    val remSleepState = remember { mutableStateOf(sleepRecord.remSleep ?: "") }
                    val awakeDurationState =
                        remember { mutableStateOf(sleepRecord.awakeDuration ?: "") }

                    // Text fields for each attribute
                    TextField(
                        value = dateState.value,
                        onValueChange = { dateState.value = it },
                        label = { Text("Date (YYYY-MM-DD)") }
                    )
                    TextField(
                        value = totalDurationState.value,
                        onValueChange = { totalDurationState.value = it },
                        label = { Text("Total Duration (HH:MM:SS)") }
                    )
                    TextField(
                        value = deepSleepState.value,
                        onValueChange = { deepSleepState.value = it },
                        label = { Text("Deep Sleep (HH:MM:SS)") }
                    )
                    TextField(
                        value = lightSleepState.value,
                        onValueChange = { lightSleepState.value = it },
                        label = { Text("Light Sleep (HH:MM:SS)") }
                    )
                    TextField(
                        value = remSleepState.value,
                        onValueChange = { remSleepState.value = it },
                        label = { Text("REM Sleep (HH:MM:SS)") }
                    )
                    TextField(
                        value = awakeDurationState.value,
                        onValueChange = { awakeDurationState.value = it },
                        label = { Text("Awake Duration (HH:MM:SS)") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val updatedRecord = sleepRecord.copy(
                                    date = LocalDateTime.parse(dateState.value),
                                    totalDuration = totalDurationState.value,
                                    deepSleep = deepSleepState.value,
                                    lightSleep = lightSleepState.value,
                                    remSleep = remSleepState.value,
                                    awakeDuration = awakeDurationState.value
                                )
                                syncViewModel.updateSleepRecord(updatedRecord)
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
fun NutritionRecordsList(nutritionRecords: List<NutritionRecord>?, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .animateContentSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("营养记录", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
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
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            if (expanded) {
                nutritionRecords?.forEach { nutritionRecord ->
                    NutritionRecordCard(nutritionRecord, syncViewModel)
                }
            }
        }
    }
}

@Composable
fun NutritionRecordCard(nutritionRecord: NutritionRecord, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { expanded = !expanded }
            .animateContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "用户ID: ${nutritionRecord.userID} - 日期: ${
                    nutritionRecord.date?.format(
                        DateTimeFormatter.ISO_DATE
                    )
                }",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "编辑"
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "收起" else "展开"
                )
            }
        }

        if (expanded) {
            Text(
                "餐类型: ${nutritionRecord.mealType}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "卡路里: ${nutritionRecord.calories} kcal",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "蛋白质: ${nutritionRecord.protein} g",
                style = MaterialTheme.typography.titleMedium
            )
            Text("脂肪: ${nutritionRecord.fat} g", style = MaterialTheme.typography.titleMedium)
            Text(
                "碳水化合物: ${nutritionRecord.carbohydrates} g",
                style = MaterialTheme.typography.titleMedium
            )
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
                ) {
                    Text("Edit Nutrition Record", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(20.dp))

                    // State variables for each field
                    val dateState = remember {
                        mutableStateOf(
                            nutritionRecord.date?.toString() ?: ""
                        )
                    }
                    val mealTypeState = remember { mutableStateOf(nutritionRecord.mealType) }
                    val caloriesState =
                        remember { mutableStateOf(nutritionRecord.calories.toString()) }
                    val proteinState =
                        remember { mutableStateOf(nutritionRecord.protein.toString()) }
                    val fatState = remember { mutableStateOf(nutritionRecord.fat.toString()) }
                    val carbohydratesState =
                        remember { mutableStateOf(nutritionRecord.carbohydrates.toString()) }

                    // Text fields for each attribute
                    TextField(
                        value = dateState.value,
                        onValueChange = { dateState.value = it },
                        label = { Text("Date (YYYY-MM-DD)") }
                    )
                    TextField(
                        value = mealTypeState.value,
                        onValueChange = { mealTypeState.value = it },
                        label = { Text("Meal Type") }
                    )
                    TextField(
                        value = caloriesState.value,
                        onValueChange = { caloriesState.value = it },
                        label = { Text("Calories") }
                    )
                    TextField(
                        value = proteinState.value,
                        onValueChange = { proteinState.value = it },
                        label = { Text("Protein (g)") }
                    )
                    TextField(
                        value = fatState.value,
                        onValueChange = { fatState.value = it },
                        label = { Text("Fat (g)") }
                    )
                    TextField(
                        value = carbohydratesState.value,
                        onValueChange = { carbohydratesState.value = it },
                        label = { Text("Carbohydrates (g)") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val updatedRecord = nutritionRecord.copy(
                                    date = LocalDateTime.parse(dateState.value),
                                    mealType = mealTypeState.value,
                                    calories = caloriesState.value.toDouble(),
                                    protein = proteinState.value.toDouble(),
                                    fat = fatState.value.toDouble(),
                                    carbohydrates = carbohydratesState.value.toDouble()
                                )
                                syncViewModel.updateNutritionRecord(updatedRecord)
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
fun MedicationRecordsList(
    medicationRecords: List<MedicationRecord>?,
    syncViewModel: SyncViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .animateContentSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("用药记录", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
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
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            if (expanded) {
                medicationRecords?.forEach { medicationRecord ->
                    MedicationRecordCard(medicationRecord, syncViewModel)
                }
            }
        }
    }
}

@Composable
fun MedicationRecordCard(medicationRecord: MedicationRecord, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { expanded = !expanded }
            .animateContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "用户ID: ${medicationRecord.userID} - 日期: ${
                    medicationRecord.date?.format(
                        DateTimeFormatter.ISO_DATE
                    )
                }",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "编辑"
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "收起" else "展开"
                )
            }
        }

        if (expanded) {
            Text(
                "药品名称: ${medicationRecord.drugName}",
                style = MaterialTheme.typography.titleMedium
            )
            Text("剂量: ${medicationRecord.dosage}", style = MaterialTheme.typography.titleMedium)
            Text(
                "频率: ${medicationRecord.frequency}",
                style = MaterialTheme.typography.titleMedium
            )
            medicationRecord.purpose?.let {
                Text("用途: $it", style = MaterialTheme.typography.titleMedium)
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
                ) {
                    Text("Edit Medication Record", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(20.dp))

                    // State variables for each field
                    val dateState = remember {
                        mutableStateOf(
                            medicationRecord.date?.toString() ?: ""
                        )
                    }
                    val drugNameState = remember { mutableStateOf(medicationRecord.drugName) }
                    val dosageState = remember { mutableStateOf(medicationRecord.dosage) }
                    val frequencyState = remember { mutableStateOf(medicationRecord.frequency) }
                    val purposeState = remember { mutableStateOf(medicationRecord.purpose ?: "") }

                    // Text fields for each attribute
                    TextField(
                        value = dateState.value,
                        onValueChange = { dateState.value = it },
                        label = { Text("Date (YYYY-MM-DD)") }
                    )
                    TextField(
                        value = drugNameState.value,
                        onValueChange = { drugNameState.value = it },
                        label = { Text("Drug Name") }
                    )
                    TextField(
                        value = dosageState.value,
                        onValueChange = { dosageState.value = it },
                        label = { Text("Dosage") }
                    )
                    TextField(
                        value = frequencyState.value,
                        onValueChange = { frequencyState.value = it },
                        label = { Text("Frequency") }
                    )
                    TextField(
                        value = purposeState.value,
                        onValueChange = { purposeState.value = it },
                        label = { Text("Purpose") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val updatedRecord = medicationRecord.copy(
                                    date = LocalDateTime.parse(dateState.value),
                                    drugName = drugNameState.value,
                                    dosage = dosageState.value,
                                    frequency = frequencyState.value,
                                    purpose = purposeState.value
                                )
                                syncViewModel.updateMedicationRecord(updatedRecord)
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
fun HealthIndicatorsList(healthIndicators: List<HealthIndicator>?, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .animateContentSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("健康指标", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
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
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            if (expanded) {
                healthIndicators?.forEach { healthIndicator ->
                    HealthIndicatorCard(healthIndicator, syncViewModel)
                }
            }
        }
    }
}

@Composable
fun HealthIndicatorCard(healthIndicator: HealthIndicator, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { expanded = !expanded }
            .animateContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "用户ID: ${healthIndicator.userID} - 日期: ${
                    healthIndicator.date?.format(
                        DateTimeFormatter.ISO_DATE
                    )
                }",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "编辑"
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "收起" else "展开"
                )
            }
        }

        if (expanded) {
            Text(
                "血压: ${healthIndicator.bloodPressure}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "胆固醇: ${healthIndicator.cholesterol} mg/dL",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "血糖: ${healthIndicator.glucoseLevel} mg/dL",
                style = MaterialTheme.typography.titleMedium
            )
            healthIndicator.otherIndicators?.let {
                Text("其他指标: $it", style = MaterialTheme.typography.titleMedium)
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
                ) {
                    Text("Edit Health Indicator", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(20.dp))

                    val dateState = remember {
                        mutableStateOf(
                            healthIndicator.date?.toString() ?: ""
                        )
                    }
                    val bloodPressureState =
                        remember { mutableStateOf(healthIndicator.bloodPressure) }
                    val cholesterolState =
                        remember { mutableStateOf(healthIndicator.cholesterol.toString()) }
                    val glucoseLevelState =
                        remember { mutableStateOf(healthIndicator.glucoseLevel.toString()) }
                    val otherIndicatorsState =
                        remember { mutableStateOf(healthIndicator.otherIndicators ?: "") }

                    // Text fields for each attribute
                    TextField(
                        value = dateState.value,
                        onValueChange = { dateState.value = it },
                        label = { Text("Date (YYYY-MM-DD)") }
                    )
                    TextField(
                        value = bloodPressureState.value,
                        onValueChange = { bloodPressureState.value = it },
                        label = { Text("Blood Pressure") }
                    )
                    TextField(
                        value = cholesterolState.value,
                        onValueChange = { cholesterolState.value = it },
                        label = { Text("Cholesterol (mg/dL)") }
                    )
                    TextField(
                        value = glucoseLevelState.value,
                        onValueChange = { glucoseLevelState.value = it },
                        label = { Text("Glucose Level (mg/dL)") }
                    )
                    TextField(
                        value = otherIndicatorsState.value,
                        onValueChange = { otherIndicatorsState.value = it },
                        label = { Text("Other Indicators") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val updatedRecord = healthIndicator.copy(
                                    date = LocalDateTime.parse(dateState.value),
                                    bloodPressure = bloodPressureState.value,
                                    cholesterol = cholesterolState.value.toDouble(),
                                    glucoseLevel = glucoseLevelState.value.toDouble(),
                                    otherIndicators = otherIndicatorsState.value
                                )
                                syncViewModel.updateHealthIndicator(updatedRecord)
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
fun ExerciseRecordsList(exerciseRecords: List<ExerciseRecord>?, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .animateContentSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("运动记录", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
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
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            if (expanded) {
                exerciseRecords?.forEach { exerciseRecord ->
                    ExerciseRecordCard(exerciseRecord, syncViewModel)
                }
            }
        }
    }
}

@Composable
fun ExerciseRecordCard(exerciseRecord: ExerciseRecord, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { expanded = !expanded }
            .animateContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "用户ID: ${exerciseRecord.userID}  - 日期: ${
                    exerciseRecord.date?.format(
                        DateTimeFormatter.ISO_DATE
                    )
                }",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "编辑"
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "收起" else "展开"
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "类型: ${exerciseRecord.exerciseType} ",
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (expanded) {
            Text(
                "持续时间: ${exerciseRecord.duration}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "距离: ${exerciseRecord.distance} km",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "能量消耗: ${exerciseRecord.energyExpenditure} kcal",
                style = MaterialTheme.typography.titleMedium
            )
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
                ) {
                    Text("Edit Exercise Record", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(20.dp))

                    // State variables for each field
                    val exerciseTypeState = remember { mutableStateOf(exerciseRecord.exerciseType) }
                    val durationState = remember { mutableStateOf(exerciseRecord.duration) }
                    val dateState = remember {
                        mutableStateOf(
                            exerciseRecord.date?.toString() ?: ""
                        )
                    }
                    val distanceState =
                        remember { mutableStateOf(exerciseRecord.distance.toString()) }
                    val energyExpenditureState =
                        remember { mutableStateOf(exerciseRecord.energyExpenditure.toString()) }

                    // Text fields for each attribute
                    TextField(
                        value = exerciseTypeState.value,
                        onValueChange = { exerciseTypeState.value = it },
                        label = { Text("Exercise Type") }
                    )
                    TextField(
                        value = durationState.value,
                        onValueChange = { durationState.value = it },
                        label = { Text("Duration (e.g., 30min or 1h 15min)") }
                    )
                    TextField(
                        value = dateState.value,
                        onValueChange = { dateState.value = it },
                        label = { Text("Date (YYYY-MM-DD)") }
                    )
                    TextField(
                        value = distanceState.value,
                        onValueChange = { distanceState.value = it },
                        label = { Text("Distance (km)") }
                    )
                    TextField(
                        value = energyExpenditureState.value,
                        onValueChange = { energyExpenditureState.value = it },
                        label = { Text("Energy Expenditure (kcal)") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val updatedRecord = exerciseRecord.copy(
                                    exerciseType = exerciseTypeState.value,
                                    duration = durationState.value,
                                    date = LocalDateTime.parse(dateState.value),
                                    distance = distanceState.value.toDouble(),
                                    energyExpenditure = energyExpenditureState.value.toDouble()
                                )
                                syncViewModel.updateExerciseRecord(updatedRecord)
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
fun DailyActivityRecordsList(dailyActivities: List<DailyActivity>?, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .animateContentSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "每日活动",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
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
                        fontSize = 20.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            if (expanded) {
                dailyActivities?.forEach { dailyActivity ->
                    DailyActivityCard(dailyActivity, syncViewModel)
                }
            }
        }
    }
}

@Composable
fun DailyActivityCard(dailyActivity: DailyActivity, syncViewModel: SyncViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { expanded = !expanded }
            .animateContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "用户ID: ${dailyActivity.userID} - 日期: ${
                    dailyActivity.date.format(
                        DateTimeFormatter.ISO_DATE
                    )
                }",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { showEditDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "编辑"
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "收起" else "展开"
                )
            }
        }

        if (expanded) {
            dailyActivity.steps?.let {
                Text(
                    "步数: $it",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            dailyActivity.walkingDistance?.let {
                Text(
                    "步行距离: ${it}km",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            dailyActivity.exerciseDuration?.let {
                Text(
                    "运动时间: $it",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            dailyActivity.heartRate?.let {
                Text(
                    "心率: $it bpm",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            dailyActivity.floorsClimbed?.let {
                Text(
                    "爬楼层数: $it",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            dailyActivity.runningDistance?.let {
                Text(
                    "跑步距离: ${it}km",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            dailyActivity.spO2?.let {
                Text(
                    "血氧饱和度: $it%",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            dailyActivity.energyExpenditure?.let {
                Text(
                    "能量消耗: $it kcal",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            dailyActivity.vitalCapacity?.let {
                Text(
                    "肺活量: $it ml",
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
                ) {
                    Text("Edit Daily Activity", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(20.dp))

                    // State variables for each field
                    val dateState =
                        remember { mutableStateOf(dailyActivity.date.toString()) }
                    val stepsState =
                        remember { mutableStateOf(dailyActivity.steps?.toString() ?: "") }
                    val walkingDistanceState =
                        remember { mutableStateOf(dailyActivity.walkingDistance?.toString() ?: "") }
                    val exerciseDurationState =
                        remember { mutableStateOf(dailyActivity.exerciseDuration ?: "") }
                    val heartRateState =
                        remember { mutableStateOf(dailyActivity.heartRate?.toString() ?: "") }
                    val floorsClimbedState =
                        remember { mutableStateOf(dailyActivity.floorsClimbed?.toString() ?: "") }
                    val runningDistanceState =
                        remember { mutableStateOf(dailyActivity.runningDistance?.toString() ?: "") }
                    val spO2State =
                        remember { mutableStateOf(dailyActivity.spO2?.toString() ?: "") }
                    val energyExpenditureState = remember {
                        mutableStateOf(
                            dailyActivity.energyExpenditure?.toString() ?: ""
                        )
                    }
                    val vitalCapacityState =
                        remember { mutableStateOf(dailyActivity.vitalCapacity?.toString() ?: "") }

                    // Text fields for each attribute
                    TextField(
                        value = dateState.value,
                        onValueChange = { dateState.value = it },
                        label = { Text("Date (YYYY-MM-DD)") })
                    TextField(
                        value = stepsState.value,
                        onValueChange = { stepsState.value = it },
                        label = { Text("Steps") })
                    TextField(
                        value = walkingDistanceState.value,
                        onValueChange = { walkingDistanceState.value = it },
                        label = { Text("Walking Distance (km)") })
                    TextField(
                        value = exerciseDurationState.value,
                        onValueChange = { exerciseDurationState.value = it },
                        label = { Text("Exercise Duration (HH:MM:SS)") })
                    TextField(
                        value = heartRateState.value,
                        onValueChange = { heartRateState.value = it },
                        label = { Text("Heart Rate (bpm)") })
                    TextField(
                        value = floorsClimbedState.value,
                        onValueChange = { floorsClimbedState.value = it },
                        label = { Text("Floors Climbed") })
                    TextField(
                        value = runningDistanceState.value,
                        onValueChange = { runningDistanceState.value = it },
                        label = { Text("Running Distance (km)") })
                    TextField(
                        value = spO2State.value,
                        onValueChange = { spO2State.value = it },
                        label = { Text("SpO2 (%)") })
                    TextField(
                        value = energyExpenditureState.value,
                        onValueChange = { energyExpenditureState.value = it },
                        label = { Text("Energy Expenditure (kcal)") })
                    TextField(
                        value = vitalCapacityState.value,
                        onValueChange = { vitalCapacityState.value = it },
                        label = { Text("Vital Capacity (ml)") })

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val updatedActivity = dailyActivity.copy(
                                    date = LocalDateTime.parse(dateState.value),
                                    steps = stepsState.value.toIntOrNull(),
                                    walkingDistance = walkingDistanceState.value.toDoubleOrNull(),
                                    exerciseDuration = exerciseDurationState.value,
                                    heartRate = heartRateState.value.toIntOrNull(),
                                    floorsClimbed = floorsClimbedState.value.toIntOrNull(),
                                    runningDistance = runningDistanceState.value.toDoubleOrNull(),
                                    spO2 = spO2State.value.toDoubleOrNull(),
                                    energyExpenditure = energyExpenditureState.value.toDoubleOrNull(),
                                    vitalCapacity = vitalCapacityState.value.toDoubleOrNull()
                                )
                                syncViewModel.updateDailyActivity(updatedActivity)
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
