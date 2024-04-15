package com.example.healthapp.Navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        modifier = Modifier.height(80.dp), // 控制NavigationBar的高度
    ) {
        HealthBottomList.forEach { destination ->
            NavigationBarItem(
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp), // 只设置图标大小，不填充最大高度
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                // label = { Text(destination.route) }, // 如果你想显示标签
                selected = navController.currentBackStackEntryAsState().value?.destination?.route == destination.route,
                onClick = {
                    // 导航逻辑
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                // 如果没有特殊的对齐需求，不需要设置padding
            )
        }
    }

}

