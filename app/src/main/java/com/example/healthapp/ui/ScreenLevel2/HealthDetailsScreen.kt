package com.example.healthapp.ui.ScreenLevel2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.R
import com.example.healthapp.data.entity.PhysicalProfile
import com.example.healthapp.data.entity.User
import com.example.healthapp.data.viewmodel.PhysicalProfileViewModel
import com.example.healthapp.data.viewmodel.UserViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

// 该类主要是显示我的主页中的详情界面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthDetailsScreen(
    userId: Int,
    userViewModel: UserViewModel,
    physicalProfileViewModel: PhysicalProfileViewModel,
    onBackClicked: () -> Unit, // 假设这是返回上一级界面的逻辑
    navController: NavHostController
) {
    // 初始化需要的数据
    val user by userViewModel.getUserById(userId)
        .collectAsState(initial = null)
    val physicalProfile by physicalProfileViewModel.getPhysicalProfileById(userId)
        .collectAsState(initial = null)

    var isEditMode by remember { mutableStateOf(false) }

    // 编辑模式下的保存逻辑
    fun saveUser(updates: User, updatedPhysicalProfile: PhysicalProfile) {
        userViewModel.updateUser(updates)
        physicalProfileViewModel.updatePhysicalProfile(updatedPhysicalProfile)
        isEditMode = false // 确保这一行在数据更新后执行
    }

    // 编辑模式下的取消逻辑
    fun cancelEdit() {
        isEditMode = false // 取消编辑并不保存数据
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(id = R.string.health_detail_info_text))
                    }
                },
                navigationIcon = {
                    Button(
                        onClick = {
                            if (isEditMode) {
                                cancelEdit() // 编辑模式下点击取消
                            } else {
                                navController.popBackStack()
                                onBackClicked() // 非编辑模式下执行返回操作
                            }
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
                        if (!isEditMode) Row(
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
                                text = "设置",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.Blue
                                )
                            )
                        } else Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.cancel_text),
                                tint = Color.Blue
                            )
                            Text(
                                text = "取消",
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
                    IconButton(
                        onClick = {
                            // 如果想要在这里就实现数据的保存，需要使用状态提升
                            if (!isEditMode) {
                                isEditMode = !isEditMode
                            }
                        },
                        modifier = Modifier.width(80.dp)
                    ) {
                        Icon(
                            if (isEditMode) Icons.Filled.Check else Icons.Filled.Edit,
                            contentDescription = if (isEditMode) stringResource(id = R.string.check_text) else stringResource(
                                id = R.string.edit_text
                            ),
                            tint = if (isEditMode) Color.Gray else Color.Blue // 在编辑模式下使用灰色图标表示禁用

                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // 头像居中显示
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.ab5_hiit),
                    contentDescription = stringResource(id = R.string.avatar_text),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        //定义120dp的圆形，clip
                        .size(140.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(Modifier.height(32.dp))

            // 头像居中显示的逻辑不变
            if (!isEditMode) {
                ShowDetailContent(user, physicalProfile)
            } else {
                EditDetailContent(user, physicalProfile) { updatedUser, updatePhysicalProfile ->
                    saveUser(updatedUser, updatePhysicalProfile)
                }
            }
        }
    }
}

fun bmiCalculation(heightCm: Double, weightKg: Double): Double {
    val heightM = heightCm / 100;
    return weightKg / (heightM * heightM)
}

@Composable
fun ShowDetailContent(user: User?, physicalProfile: PhysicalProfile?) {
    if (user != null) {
        // 使用UserProfile中的数据
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Card() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.name_text),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${user.username}",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                }
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp)
                )

                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val dateOfBirthFormatted = user.dateOfBirth?.format(dateFormatter)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.birthdate_text),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "$dateOfBirthFormatted",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                }
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.gender_text),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${user.gender}",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                }
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.age_text),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${calculateAgeFromLocalDateTime(user.dateOfBirth)}",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                }
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.blood_type_text),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${user.bloodType}",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                }
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.skin_type_text),
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${user.skinType}",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Card() {
                Row(
                    modifier = Modifier.background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.height_text),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${
                            if (physicalProfile == null) stringResource(id = R.string.nothing_text)
                            else String.format("%.2f", physicalProfile.height)
                        }" + stringResource(id = R.string.cm_text),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
                Divider(
                    color = Color.Gray, // 设置分隔线的颜色
                    thickness = 1.dp, // 设置分隔线的厚度
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                )
                // 体重
                Row(
                    modifier = Modifier.background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.weight_text),
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${
                            if (physicalProfile == null) stringResource(id = R.string.nothing_text)
                            else String.format("%.2f", physicalProfile.weight)
                        }" + stringResource(id = R.string.kg_text),
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }

                Divider(
                    color = Color.Gray, // 设置分隔线的颜色
                    thickness = 1.dp, // 设置分隔线的厚度
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                )
                Row(
                    modifier = Modifier.background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.bmi_text),
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${
                            if (physicalProfile == null) stringResource(id = R.string.nothing_text)
                            else String.format(
                                "%.2f",
                                bmiCalculation(physicalProfile.height, physicalProfile.weight)
                            )
                        }",
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }

                Divider(
                    color = Color.Gray, // 设置分隔线的颜色
                    thickness = 1.dp, // 设置分隔线的厚度
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                )
                Row(
                    modifier = Modifier.background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.shoeSize_text),
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${
                            if (physicalProfile == null) stringResource(id = R.string.nothing_text)
                            else physicalProfile.shoeSize
                        }" + stringResource(id = R.string.eurSize_text),
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
                Divider(
                    color = Color.Gray, // 设置分隔线的颜色
                    thickness = 1.dp, // 设置分隔线的厚度
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                )
                Row(
                    modifier = Modifier.background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.wearsGlasses_text),
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = " ${
                            if (physicalProfile == null) stringResource(id = R.string.nothing_text)
                            else if (physicalProfile.wearsGlasses == true) stringResource(
                                id = R.string.yes_text
                            ) else stringResource(id = R.string.no_text)
                        }",
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }

                Divider(
                    color = Color.Gray, // 设置分隔线的颜色
                    thickness = 1.dp, // 设置分隔线的厚度
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                )
                // 是否坐轮椅
                Row(
                    modifier = Modifier.background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.wheelchair_text),
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = " ${
                            if (user.isWheelchairUser == true) stringResource(
                                id = R.string.yes_text
                            ) else stringResource(id = R.string.no_text)
                        }",
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // 影响心率的药物，在估算有氧适能的时候，可能会纳入考量
            Card() {
                val heartRateMedicationText =
                    " ${
                        user.heartRateAffectingDrugs?.let { drugs ->
                            // 当user.heartRateAffectingDrugs不为null时执行这里的代码
                            if (drugs.isBlank()) {
                                // 如果字符串是空的
                                stringResource(id = R.string.nothing_text)
                            } else {
                                // 如果字符串不为空
                                drugs
                            }
                        } ?: run {
                            // 当user.heartRateAffectingDrugs为null时执行这里的代码
                            stringResource(id = R.string.nothing_text)
                        }
                    }"
                Row(
                    modifier = Modifier.background(color = Color.White)
                ) {
                    Text(
                        text = stringResource(id = R.string.heart_rate_medication_text),
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = heartRateMedicationText,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                }
            }
            Text(
                text = "β受体阻断药或钙通道抑制剂可限制心率。系统在估算你的有氧适能时，可将此纳入考量。" +
                        "更改此设置不会影响现有的数据，但可能会更改你将来的有氧适能预测。\n" +
                        "主要包括：\n" +
                        "  钠离子通道阻断剂（如普罗帕酮、弗尼卡）\n" +
                        "  β受体阻断剂（如普萘洛尔、美托洛尔）\n" +
                        "  钾通道阻断剂（如胺碘酮）\n" +
                        "  钙通道阻断剂（如维拉帕米、地尔硫卓）",
                style = TextStyle(
                    color = Color.Gray
                ),
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )
            // 如果有影响心率的药物信息，假设这是用户药物信息的一部分，可以添加相应的逻辑来显示
            // 例如: Text("影响心率的药物: ${userMedication.aboutHeart}", modifier = Modifier.fillMaxWidth())
        }
    } else {
        // 如果userProfile为null，显示加载中或者空数据提示
        Text(
            text = stringResource(id = R.string.loading_text),
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
    }
}

// 用于计算当前用户的年龄
fun calculateAgeFromLocalDateTime(birthday: LocalDateTime?): Int {
    val currentDate = LocalDate.now() // 获取当前的日期
    val birthDate = birthday ?: LocalDateTime.now()
    val age = birthDate?.toLocalDate() // 将LocalDateTime转换为LocalDate
    return Period.between(age, currentDate).years // 使用Period.between计算两个日期之间的年数
}

// 用于构建一个性别编辑的下拉表
@Composable
fun GenderDropdownMenu(selectedGender: String, onGenderSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf(
        stringResource(id = R.string.male_text),
        stringResource(id = R.string.female_text),
        stringResource(id = R.string.other_text)
    )

    Column(
        modifier = Modifier
            .padding(8.dp)
//            .fillMaxWidth()
    )
    {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min), // 根据内容高度调整
        )
        {
            Text(text = selectedGender)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genderOptions.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(text = gender, color = Color.Black) },
                    onClick = {
                        onGenderSelected(gender)
                        expanded = false
                    })


            }
        }
    }
}

// 用于构建一个性别编辑的下拉表
@Composable
fun BloodTypeDropdownMenu(selectedBloodType: String, onBloodTypeSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val bloodTypeOptions = listOf(
        stringResource(id = R.string.blood_A),
        stringResource(id = R.string.blood_B),
        stringResource(id = R.string.blood_AB),
        stringResource(id = R.string.blood_O)
    )

    Column(
        modifier = Modifier
            .padding(8.dp)
//            .fillMaxWidth()
    )
    {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min), // 根据内容高度调整
        )
        {
            Text(text = selectedBloodType)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            bloodTypeOptions.forEach { bloodType ->
                DropdownMenuItem(
                    text = { Text(text = bloodType, color = Color.Black) },
                    onClick = {
                        onBloodTypeSelected(bloodType)
                        expanded = false
                    })


            }
        }
    }
}

// 用于构建一个日光反应皮肤类型编辑的下拉表
@Composable
fun SkinTypeDropDownMenu(selectedSkinType: String, onSkinTypeSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val skinTypeOptions = listOf(
        stringResource(id = R.string.skin_type_I),
        stringResource(id = R.string.skin_type_II),
        stringResource(id = R.string.skin_type_III),
        stringResource(id = R.string.skin_type_IV),
        stringResource(id = R.string.skin_type_V),
        stringResource(id = R.string.skin_type_VI)
    )

    Column(
        modifier = Modifier
            .padding(8.dp)
//            .fillMaxWidth()
    ) {

        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min), // 根据内容高度调整
        ) {
            Text(text = selectedSkinType)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            skinTypeOptions.forEach { skinType ->
                DropdownMenuItem(
                    text = { Text(text = skinType, color = Color.Black) },
                    onClick = {
                        onSkinTypeSelected(skinType)
                        expanded = false
                    }
                )

            }
        }
    }
}


@Composable
fun EditDetailContent(
    user: User?,
    physicalProfile: PhysicalProfile?,
    saveUser: (User, PhysicalProfile) -> Unit // 更新回调以同时处理User和PhysicalProfile
) {
    // 默认值设置
    val defaultBirthday = LocalDateTime.now()
    val birthday = user?.dateOfBirth ?: defaultBirthday

    // 记住编辑状态
    var name by remember { mutableStateOf(user?.username ?: "") }
    // 使用DateTimeFormatter格式化LocalDateTime为字符串
    var birthDate by remember {
        mutableStateOf(
            user?.dateOfBirth?.format(DateTimeFormatter.ISO_DATE) ?: ""
        )
    }
    val (initialYear, initialMonth, initialDay) = birthDate.split("-").let {
        Triple(it[0], it[1], it[2])
    }
    var gender by remember { mutableStateOf(user?.gender ?: "") }
    var bloodType by remember { mutableStateOf(user?.bloodType ?: "") }
    var skinType by remember { mutableStateOf(user?.skinType ?: "") }
    var height by remember { mutableDoubleStateOf(physicalProfile?.height ?: 0.0) }
    var weight by remember { mutableDoubleStateOf(physicalProfile?.weight ?: 0.0) }
    var usesWheelchair by remember { mutableStateOf(user?.isWheelchairUser ?: false) }
    var bmi by remember { mutableStateOf(physicalProfile?.bmi ?: 0.0) }
    var wearsGlasses by remember { mutableStateOf(physicalProfile?.wearsGlasses ?: false) }
    var shoeSize by remember { mutableStateOf(physicalProfile?.shoeSize ?: 0.0) }
    var heartRateAffectingDrugs by remember { mutableStateOf(user?.heartRateAffectingDrugs ?: "") }

    Scaffold(
        // 定义底部的保存按钮
        bottomBar = {
            Button(
                onClick = {
                    // 解析字符串为LocalDateTime
                    val parsedBirthDate =
                        LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE).atStartOfDay()
                    // 准备更新后的User和PhysicalProfile对象
                    val updatedUser = user?.copy(
                        username = name,
                        dateOfBirth = parsedBirthDate,
                        gender = gender,
                        bloodType = bloodType,
                        skinType = skinType,
                        isWheelchairUser = usesWheelchair,
                        heartRateAffectingDrugs = heartRateAffectingDrugs
                    )
                    val updatedPhysicalProfile = physicalProfile?.copy(
                        userID = user?.userId ?: 1,
                        height = height,
                        weight = weight,
                        bmi = bmi,
                        wearsGlasses = wearsGlasses,
                        bloodType = bloodType,
                        shoeSize = shoeSize
                    )
                    // 仅当updatedUser和updatedPhysicalProfile非空时调用回调
                    if (updatedUser != null && updatedPhysicalProfile != null) {
                        saveUser(updatedUser, updatedPhysicalProfile)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "保存",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    ) { innerPadding ->
        // 这里因为项数较少，所以直接使用可滑动列，如果多的话，应该是使用 LazyColumn
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            item {
                Card() {
                    // 编辑姓名
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically // 确保行内元素垂直居中
                    ) {
                        Text(
                            text = stringResource(id = R.string.name_text),
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.width(72.dp) // 根据内容设置最小宽度
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            // label = { Text(stringResource(R.string.name_text)) },
                            textStyle = TextStyle(fontSize = 16.sp), // 设置输入框内文本的大小
                            modifier = Modifier
                                .weight(1f) // 填充剩余空间
                                .height(IntrinsicSize.Min), // 根据内容高度调整
                        )
                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                    )
                    // 编辑出生年月
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically // 确保行内元素垂直居中
                    ) {
                        Text(
                            text = stringResource(R.string.birthdate_text),
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.width(72.dp) // 根据内容设置最小宽度
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Row {
                            val (year, setYear) = remember { mutableStateOf(initialYear) }
                            val (month, setMonth) = remember { mutableStateOf(initialMonth) }
                            val (day, setDay) = remember { mutableStateOf(initialDay) }

                            // 更新整个日期状态
                            LaunchedEffect(year, month, day) {
                                birthDate =
                                    "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
                            }
                            OutlinedTextField(
                                value = year,
                                onValueChange = { newValue ->
                                    if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                                        setYear(newValue)
                                    }
                                },
                                label = { Text(text = "年份") },
                                textStyle = TextStyle(fontSize = 16.sp), // 设置输入框内文本的大小
                                modifier = Modifier
                                    .weight(1f)
                                    .height(IntrinsicSize.Min), // 根据内容高度调整
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = month,
                                onValueChange = { newValue ->
                                    if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                                        setMonth(newValue)
                                    }
                                },
                                label = { Text(text = "月") },
                                textStyle = TextStyle(fontSize = 16.sp), // 设置输入框内文本的大小
                                modifier = Modifier
                                    .weight(1f)
                                    .height(IntrinsicSize.Min), // 根据内容高度调整
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = day,
                                onValueChange = { newValue ->
                                    if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                                        setDay(newValue)
                                    }
                                },
                                label = { Text(text = "日") },
                                textStyle = TextStyle(fontSize = 16.sp), // 设置输入框内文本的大小
                                modifier = Modifier
                                    .weight(1f)
                                    .height(IntrinsicSize.Min), // 根据内容高度调整
                            )
                        }

                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                    )
                    // 编辑身高
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically // 确保行内元素垂直居中
                    ) {
                        Text(
                            text = stringResource(R.string.height_text),
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.width(72.dp) // 根据内容设置最小宽度
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedTextField(
                            value = height.toString(),
                            onValueChange = {
                                height = it.toDouble()
                                bmi = bmiCalculation(it.toDouble(), weight)
                            },
                            // label = { Text(stringResource(R.string.birthdate_text)) },
                            textStyle = TextStyle(fontSize = 16.sp), // 设置输入框内文本的大小
                            modifier = Modifier
                                .weight(1f)
                                .height(IntrinsicSize.Min), // 根据内容高度调整
                        )
                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                    )
                    // 编辑体重
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically // 确保行内元素垂直居中
                    ) {
                        Text(
                            text = stringResource(R.string.weight_text),
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.width(72.dp) // 根据内容设置最小宽度
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedTextField(
                            value = weight.toString(),
                            onValueChange = {
                                weight = it.toDouble()
                                bmi = bmiCalculation(height, it.toDouble())
                            },
                            // label = { Text(stringResource(R.string.birthdate_text)) },
                            textStyle = TextStyle(fontSize = 16.sp), // 设置输入框内文本的大小
                            modifier = Modifier
                                .weight(1f)
                                .height(IntrinsicSize.Min), // 根据内容高度调整
                        )
                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                    )
                    // 编辑双脚尺码
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically // 确保行内元素垂直居中
                    ) {
                        Text(
                            text = stringResource(R.string.shoeSize_text),
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.width(72.dp) // 根据内容设置最小宽度
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedTextField(
                            value = shoeSize.toString(),
                            onValueChange = { shoeSize = it.toDouble() },
                            // label = { Text(stringResource(R.string.birthdate_text)) },
                            textStyle = TextStyle(fontSize = 16.sp), // 设置输入框内文本的大小
                            modifier = Modifier
                                .weight(1f)
                                .height(IntrinsicSize.Min), // 根据内容高度调整
                        )
                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                    )
                    // 设置性别
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp, vertical = 0.dp),
                        verticalAlignment = Alignment.CenterVertically // 确保行内元素垂直居中
                    ) {
                        Text(
                            text = stringResource(R.string.gender_text),
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.width(72.dp) // 根据内容设置最小宽度
                        )
                        GenderDropdownMenu(selectedGender = gender, onGenderSelected = { selected ->
                            gender = selected
                        })
                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                    )
                    // 设置血型
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp, vertical = 0.dp),
                        verticalAlignment = Alignment.CenterVertically // 确保行内元素垂直居中
                    ) {
                        Text(
                            text = stringResource(R.string.blood_type_text),
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.width(72.dp) // 根据内容设置最小宽度
                        )
                        BloodTypeDropdownMenu(
                            selectedBloodType = bloodType,
                            onBloodTypeSelected = { selected ->
                                bloodType = selected
                            })
                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                    )
                    // 设置皮肤类型
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp, vertical = 0.dp),
                        verticalAlignment = Alignment.CenterVertically // 确保行内元素垂直居中
                    ) {
                        Text(
                            text = stringResource(R.string.skin_type_text),
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.width(72.dp) // 根据内容设置最小宽度
                        )
                        SkinTypeDropDownMenu(selectedSkinType = skinType,
                            onSkinTypeSelected = { selected ->
                                skinType = selected
                            })
                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                    )
                    // 编辑影响心率的药物
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically // 确保行内元素垂直居中
                    ) {
                        Text(
                            text = stringResource(R.string.heart_rate_medication_text),
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.width(72.dp) // 根据内容设置最小宽度
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        OutlinedTextField(
                            value = heartRateAffectingDrugs,
                            onValueChange = { heartRateAffectingDrugs = it },
                            // label = { Text(stringResource(R.string.birthdate_text)) },
                            textStyle = TextStyle(fontSize = 16.sp), // 设置输入框内文本的大小
                            modifier = Modifier
                                .weight(1f)
                                .height(IntrinsicSize.Min), // 根据内容高度调整
                        )
                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                    )
                    // 编辑是否坐轮椅
                    Row(
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.wheelchair_text),
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Checkbox(
                            checked = usesWheelchair,
                            onCheckedChange = { usesWheelchair = it }
                        )
                    }
                    Divider(
                        color = Color.Gray, // 设置分隔线的颜色
                        thickness = 1.dp, // 设置分隔线的厚度
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(horizontal = 16.dp) // 设置分隔线两侧的间距
                    )
                    // 编辑是否戴眼镜
                    Row(
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(
                                top = 8.dp,
                                bottom = 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(R.string.wearsGlasses_text),
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        wearsGlasses?.let {
                            Checkbox(
                                checked = it,
                                onCheckedChange = { wearsGlasses = it }
                            )
                        }
                    }
                }
            }
//                Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHealthDetailsScreen() {

//    HealthDetailsScreen(onBackClicked = {}, onEditClicked = {})
}
