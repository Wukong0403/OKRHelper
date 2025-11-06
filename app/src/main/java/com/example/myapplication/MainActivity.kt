package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    viewModel: OKRViewModel
) {
    val okrs by viewModel.allOKRs.collectAsState()

    // 缓存月度任务和周任务
    val monthlyTasksMap = remember { mutableStateMapOf<Long, List<com.example.myapplication.data.MonthlyTask>>() }
    val weeklyTasksMap = remember { mutableStateMapOf<Long, List<com.example.myapplication.data.WeeklyTask>>() }

    // 为每个OKR加载月度任务
    okrs.forEach { okr ->
        val monthlyTasks by viewModel.getMonthlyTasksForOKR(okr.id).collectAsState(initial = emptyList())
        LaunchedEffect(monthlyTasks) {
            monthlyTasksMap[okr.id] = monthlyTasks
        }

        // 为每个月度任务加载周任务
        monthlyTasks.forEach { monthlyTask ->
            val weeklyTasks by viewModel.getWeeklyTasksForMonth(monthlyTask.id).collectAsState(initial = emptyList())
            LaunchedEffect(weeklyTasks) {
                weeklyTasksMap[monthlyTask.id] = weeklyTasks
            }
        }
    }

    OKRListScreen(
        okrs = okrs,
        getMonthlyTasks = { okrId -> monthlyTasksMap[okrId] ?: emptyList() },
        getWeeklyTasks = { monthlyTaskId -> weeklyTasksMap[monthlyTaskId] ?: emptyList() },
        onAddOKR = { year, quarter, objective, keyResults ->
            viewModel.insertOKR(year, quarter, objective, keyResults)
        },
        onToggleOKRComplete = { okr ->
            viewModel.updateOKR(okr.copy(isCompleted = !okr.isCompleted))
        },
        onAddMonthlyTask = { okrId, month, title, description ->
            viewModel.insertMonthlyTask(okrId, month, title, description)
        },
        onToggleMonthlyTaskComplete = { task ->
            viewModel.updateMonthlyTask(task.copy(isCompleted = !task.isCompleted))
        },
        onAddWeeklyTask = { monthlyTaskId, week, title, description ->
            viewModel.insertWeeklyTask(monthlyTaskId, week, title, description)
        },
        onToggleWeeklyTaskComplete = { task ->
            viewModel.updateWeeklyTask(task.copy(isCompleted = !task.isCompleted))
        }
    )
}