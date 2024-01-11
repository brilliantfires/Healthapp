package com.example.healthapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthapp.data.entity.WellnessTask
import com.example.healthapp.data.viewmodel.WellnessTaskViewModel
import com.example.healthapp.data.viewmodel.WellnessTaskViewModelFactory
import com.example.healthapp.ui.theme.HealthappTheme

class TasksActivity : ComponentActivity() {
     private val viewModel: WellnessTaskViewModel by viewModels {
        WellnessTaskViewModelFactory((application as HealthApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthappTheme {
                TaskFormScreen(viewModel)
            }
        }
    }
}

// 先来创建一个喝水的模块
@Composable
fun WaterCounter(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        // 注意 remember 是可以在重组后保持数据不会变化
        // ！ rememberSaveable 则会在更改配置之后仍然保持着状态：比如：从横屏变为竖屏时
        // 或者切换黑暗模式以及日间模式
        var count by rememberSaveable { mutableIntStateOf(0) }
        if (count > 0) {
            var showTask by remember { mutableStateOf(true) }
            if (showTask) {
//                WellnessTaskItem(
//                    onClose = { showTask = false },
//                    taskName = "Have you taken your 15 minute walk today?"
//                )
            }
            Text("You've had $count glasses.")
        }

        Row(Modifier.padding(top = 8.dp)) {
            Button(onClick = { count++ }, enabled = count < 10) {
                Text("Add one")
            }
            Button(
                onClick = { count = 0 },
                Modifier.padding(start = 8.dp)
            ) {
                Text("Clear water count")
            }
        }
    }
    /*Row(
        modifier = modifier,
        // 该列的子元素在水平方向上居中对齐
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 使用remember来在修改的时候记住修改结果而不是重新加载之后总是初始化为0
        var count by remember { mutableIntStateOf(0) }
        Text(
            text = "你已经喝了 $count 杯水",
            modifier = modifier
                .padding(16.dp)
        )

        Spacer(Modifier.weight(1f)) // 添加一个Spacer，使其占据剩余空间

        Button(
            onClick = { count++ },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Add one"
            )
        }
    }*/

}

// 这里是在尝试理解什么是状态提升
// 这个无状态 counter 是对上面 WaterCounter 的改写，将一个函数，分为两个函数
// 其中一个是无状态的 StatelessCounter ，下面还有一个有状态的 StatefulCounter ；
// 无状态的 StatelessCounter 可以灵活的进行重用，而有状态的一般不好重用；
// 这就是为什么要状态提升，既需要无状态的函数来重用，还需要有状态的函数来实现操作逻辑
@Composable
fun StatelessCounter(count: Int, onIncrement: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        if (count > 0) {
            Text("You've had $count glasses.")
        }
        Button(onClick = onIncrement, Modifier.padding(top = 8.dp), enabled = count < 10) {
            Text("Add one")
        }
    }
}

@Composable
fun StatefulCounter(modifier: Modifier = Modifier) {
    var count by rememberSaveable { mutableStateOf(0) }
    var count1 by rememberSaveable { mutableStateOf(0) }
    StatelessCounter(count, { count++ }, modifier)
    StatelessCounter(count1, { count1++ }, modifier)
}

// 这个组件就是一个无状态组件，他的变量呢，都是通过下面的有状态组件来设置的，也就是可以提升 checked 的状态
@Composable
fun WellnessTaskItem(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = taskName
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}

// 重载 WellnessTaskItem 函数，它接收任务名称和可选的modifier作为参数
// 使用内部状态（通过remember和mutableStateOf）来跟踪复选框的选中状态，并将这个状态与关闭按钮的回调传递给上面定义的WellnessTaskItem
// 它实现了状态管理和用户界面的分离
@Composable
fun WellnessTaskItem(taskName: String, modifier: Modifier = Modifier) {
    var checkedState by remember { mutableStateOf(false) }

    WellnessTaskItem(
        taskName = taskName,
        checked = checkedState,
        onCheckedChange = { newValue -> checkedState = newValue },
        onClose = {}, // we will implement this later!
        modifier = modifier,
    )
}

@Composable
fun TaskListScreen(viewModel: WellnessTaskViewModel) {
    // 观察 LiveData 并收到更新
    val allTasks by viewModel.allTasks.observeAsState(initial = emptyList())

    // 使用所有任务更新UI
    LazyColumn {
        items(allTasks) { task ->
            WellnessTaskItem(
                taskName = task.taskLabel,
                checked = task.taskChecked,
                onCheckedChange = { checked ->
                    viewModel.updateTask(task.copy(taskChecked = checked))
                },
                onClose = {
                    viewModel.deleteTask(task)
                }
            )
        }
    }
}

@Composable
fun TaskFormScreen(viewModel: WellnessTaskViewModel) {
    // 创建一个可变状态，用于存储用户输入的演员信息
    var taskLabel by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    var isInputValid  by remember { mutableStateOf(true) }

    // 创建一个表单，让用户输入演员信息
    Column {
        TextField(
            value = taskLabel,
            onValueChange = { taskLabel = it },
            label = { Text("Label") }
        )
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it },
        )

        Button(
            onClick = {
                if (taskLabel.isNotBlank()) { // 简单的输入验证
                    isInputValid = true
                    val task = WellnessTask(taskLabel = taskLabel, taskChecked = checked)
                    viewModel.insertTask(task)
                    taskLabel = "" // 重置表单
                    checked = false
                } else {
                    isInputValid = false
                }
            }
        ) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true, name = "喝水预览")
@Composable
fun WaterCounterPreview() {
    HealthappTheme {
        Column {
            WaterCounter()
            StatefulCounter()
//            TaskFormScreen()
        }
    }
}
