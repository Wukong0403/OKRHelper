package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.screens.MonthDetailScreen
import com.example.myapplication.ui.screens.OKRDetailScreen
import com.example.myapplication.ui.screens.OKRListScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.OKRViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: OKRViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                OKRApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun OKRApp(
    viewModel: OKRViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.OKRList.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.OKRList.route) {
            val okrs by viewModel.allOKRs.collectAsState()
            OKRListScreen(
                okrs = okrs,
                onOKRClick = { okrId ->
                    navController.navigate(Screen.OKRDetail.createRoute(okrId))
                },
                onAddOKR = { year, quarter, objective, keyResults ->
                    viewModel.insertOKR(year, quarter, objective, keyResults)
                },
                onToggleComplete = { okr ->
                    viewModel.updateOKR(okr.copy(isCompleted = !okr.isCompleted))
                }
            )
        }

        composable(
            route = Screen.OKRDetail.route,
            arguments = listOf(navArgument("okrId") { type = NavType.LongType })
        ) { backStackEntry ->
            val okrId = backStackEntry.arguments?.getLong("okrId") ?: 0L
            val okr by viewModel.getOKRById(okrId).collectAsState(initial = null)
            val monthlyTasks by viewModel.getMonthlyTasksForOKR(okrId)
                .collectAsState(initial = emptyList())

            OKRDetailScreen(
                okr = okr,
                monthlyTasks = monthlyTasks,
                onNavigateBack = { navController.popBackStack() },
                onMonthlyTaskClick = { monthlyTaskId ->
                    navController.navigate(Screen.MonthDetail.createRoute(monthlyTaskId))
                },
                onAddMonthlyTask = { month, title, description ->
                    viewModel.insertMonthlyTask(okrId, month, title, description)
                },
                onToggleTaskComplete = { task ->
                    viewModel.updateMonthlyTask(task.copy(isCompleted = !task.isCompleted))
                }
            )
        }

        composable(
            route = Screen.MonthDetail.route,
            arguments = listOf(navArgument("monthlyTaskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val monthlyTaskId = backStackEntry.arguments?.getLong("monthlyTaskId") ?: 0L
            val monthlyTask by viewModel.getMonthlyTaskById(monthlyTaskId)
                .collectAsState(initial = null)
            val weeklyTasks by viewModel.getWeeklyTasksForMonth(monthlyTaskId)
                .collectAsState(initial = emptyList())

            MonthDetailScreen(
                monthlyTask = monthlyTask,
                weeklyTasks = weeklyTasks,
                onNavigateBack = { navController.popBackStack() },
                onAddWeeklyTask = { week, title, description ->
                    viewModel.insertWeeklyTask(monthlyTaskId, week, title, description)
                },
                onToggleTaskComplete = { task ->
                    viewModel.updateWeeklyTask(task.copy(isCompleted = !task.isCompleted))
                }
            )
        }
    }
}