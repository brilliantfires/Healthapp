package com.example.healthapp.ui.ScreenLevel2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "用户协议",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    Button(
                        onClick = {
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
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { },
                        modifier = Modifier.width(80.dp)
                    ) {
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            PrivacyPolicyContent()
        }

    }
}

@Composable
fun PrivacyPolicyContent() {
    LazyColumn(
        modifier = Modifier
            .padding(start=16.dp,end=16.dp,bottom=16.dp)
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(8.dp))
    ) {
        item {
            Column(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "健康研究 App 隐私政策",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "智能健康管理系统调查研究 App 隐私政策于 2024 年 4 月 20 日更新\n" + "\n" +
                            "智能健康管理系统非常重视你的隐私。因此，我们制定了一项隐私政策，" +
                            "其中说明了我们如何处理你与智能健康管理系统调查研究应用程序 (以下简称“App”) 相关的信息。" +
                            "请花些时间熟悉我们针对用户隐私的做法，如有任何疑问，请联系我们。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "智能健康管理系统调查研究",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "本隐私政策概述了我们可能收集或接收的与你使用我们任何健康调查研究 App 和参与我们任何健康服务调查研究相关的信息 (以下分别称为一项“研究”，二者统称“研究”)。本隐私政策不包括我们在其他情况下收集或接收到的有关你的信息，例如，当你购买智能健康管理系统产品、浏览智能健康管理系统网站、使用其他智能健康管理系统移动 App 或将你的智能健康管理系统设备 (如手机) 用于一般非研究目的时。有关我们的一般隐私做法，请参阅我们的一般隐私政策。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "知情同意",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "要参加任何研究，你必须首先阅读并签署研究的知情同意和授权书 (如适用) (以下简称“知情同意书”)。使用相关 App，即表示你同意按照本隐私政策和知情同意书中的说明收集、使用和共享你的信息。如果本隐私政策中的任何内容与知情同意书相冲突，将以知情同意书的条款为准。\n\n" +
                            "在本政策中，我们可能会使用“研究小组”一词来指代所有可能通过 App 或作为研究的一部分访问收集的或有关你的数据的实体。对于所有研究，知情同意书将包括关于研究小组成员的具体信息。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "信息的收集和使用",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "个人信息是可用于识别或联系特定个人的数据。某些个人信息和非个人信息 (如下所述) 可能通过 App 或作为研究的一部分收集，此类信息也有可能是关于你的信息。\n\n" +
                            "智能健康管理系统及其关联公司，以及其他研究小组成员，可以相互分享我们通过 App 收集的信息，并根据本隐私政策使用这些信息。\n\n" +
                            "如果你决定参与这项研究，以下是我们可能收集的信息类型和使用方式的示例。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "我们收集哪些信息",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "当你下载其中一个 App 并参与其中一项研究时，以下是可能通过 App 收集的或关于你的信息类别 (可能包括一些个人信息) 的示例 (以下简称“研究数据”)。作为研究的一部分，将收集关于你的特定类别的信息，这些信息将在知情同意书中进行描述。\n" +
                            "● 联系信息，例如你的姓名、电子邮件地址和电话号码。\n" +
                            "● 用户特征信息，例如你的年龄、性别、居住地所在州和种族。\n" +
                            "● 病史和信息，例如你的身高/体重、以前的医疗诊断和测试 (如诊断为心律失常)、目前和以前使用的某些药物 (如血液稀释药物)、某些家族史 (如房颤史) 和健康习惯 (如吸烟)。这些信息可以通过 App 内调查或其他可能要求你完成的健康调查来收集。\n" +
                            "● 传感器信息，如心率和逐次心搏的计算。\n" +
                            "● 技术数据，在很多情况下是非个人信息，即数据形式本身不允许与任何特定的个人直接关联。例如，技术数据包括有关 App 使用情况的信息 (例如，App 首次启动的时间)、App 版本和安装 ID、设备标识符以及有关设备的技术数据，例如操作系统和机型。智能健康管理系统不会随着时间的推移，跨第三方网站跟踪其用户，来提供有针对性的广告，因此不会对“禁止跟踪”(DNT) 信号作出回应。\n" +
                            "● 不良事件信息，例如研究中出现的问题、不良事件或其他可报告事项。\n" +
                            "● 知情同意书中描述的其他信息 (如有)。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "我们如何使用你的信息",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "你的个人信息，例如你的联系信息，如果通过 App 收集，可用于知情同意书中所述的目的，包括：\n" +
                            "● 让你参与本研究，包括确定你参与研究的资格\n" +
                            "● 指导和支持研究\n" +
                            "● 通过 App 内通知、电子邮件或其他方式与你联系，提供与研究相关的调查或其他信息\n" +
                            "你的编码研究数据可用于知情同意书中所述的目的，包括：\n" +
                            "● 指导和支持研究\n" +
                            "● 开发与健康相关的产品和改善活动\n" +
                            "● 其他调查研究 (如知情同意中允许)\n" +
                            "● 发布调查结果和相关报告，但不会透露你的身份\n" +
                            "● 你的技术数据 (如上所述) 也可用于确定你是否有资格进行研究和 App 的一般使用。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "向第三方披露",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "服务提供商",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "智能健康管理系统和其他研究小组成员可能会与为研究提供服务或代表研究提供服务的公司分享你的信息，例如负责审查研究并保护你作为研究参与者的权利的独立第三方机构审查委员会，或受雇为我们提供研究相关服务的第三方 (如代表我们接收和处理研究相关投诉)。这些公司有义务保护你的信息。\n",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding()
                )
                Text(
                    text = "其他",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 18.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                )
                Text(
                    text = "你的个人信息和编码研究数据也可能会披露给以下第三方：\n" +
                            "● 政府和监管机构，例如美国卫生和公共服务部、食品和药物管理局以及其他联邦或州政府机构。\n" +
                            "● 遵循有效法律程序 (例如传票、诉讼或法院命令) 的执法部门或其他第三方。如果我们确定就国家安全或具有公众重要性的其他事宜而言，披露是必须的或适当的，我们也可能会披露关于你的信息。\n" +
                            "● 其他经批准的研究人员，如果获得知情同意，某些经批准的第三方研究人员可以访问有限的研究数据。知情同意书将更详细地描述经批准的研究人员的类别、他们可以访问的研究数据的类型以及他们使用这些数据的目的。\n" +
                            "● 其他人员，如果我们确定披露对于执行我们的条款和条件或保护我们的运作或用户是合理必要的。我们按照知情同意书中的描述披露信息。此外，如果发生重组、合并或出售，则我们可将我们收集的一切个人信息转让给相关第三方。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "信息的保护",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "智能健康管理系统非常重视你的个人信息的安全。我们已经采取了合理的措施来保证它维护的研究数据的安全，并保护你的信息的机密性，包括将其存储在使用物理安全措施的设施中的有限访问权限的系统中。尽管我们采取了安全措施，但无法保证完全保密。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "个人信息的保留",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "你参加任何研究都是自愿的。你可以决定不参加，也可以随时退出任何研究。如果你决定退出，我们可能不会删除我们已经收集的信息，我们可能会继续使用它，但我们将停止接收任何关于你的新研究数据，并将停止就研究与你联系，除非是严重的、可执行的医疗需求。每次研究的知情同意书中都会描述具体的保留政策和你需要采取的退出步骤。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "儿童",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "我们的 App 不适用于相关司法管辖区的 13 周岁以下或同等最低年龄的儿童。如果我们发现我们收集了年龄不满 13 周岁的儿童的个人信息，我们将采取措施尽快删除此等信息。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "全公司对你隐私的承诺",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "为确保你个人信息的安全，我们向全体智能健康管理系统员工传达了公司的隐私和安全准则，并在公司内部严格执行隐私保护措施。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "隐私问题",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "如果你对此隐私政策有任何疑问或顾虑，请联系我们。如果你对研究有任何疑问，请使用知情同意书中列出的联系信息联系研究小组成员。若要退出研究，请遵循知情同意书中的具体说明。请注意，删除 App 不会使你退出研究。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "隐私政策更新",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "智能健康管理系统可随时对此隐私政策加以更新。如果我们以实质性的方式更改了政策，则会在 App 中或通过特定于研究的电子邮件通知你，并随附更新的隐私政策。我们也可能在任何特定于研究的网站上提供通知。",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "联系信息",
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 4.dp, top = 16.dp)
                )
                Text(
                    text = "智能健康管理系统 Inc. 华中科技大学",
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}