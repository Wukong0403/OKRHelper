package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.screens.CalendarScreen
import com.example.myapplication.ui.screens.NewOKRListScreen
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
fun OKRApp(viewModel: OKRViewModel) {
    var selectedTab by remember { mutableStateOf(0) }

    val okrs by viewModel.allOKRs.collectAsState()

    // 缓存数据
    val keyResultsMap = remember { mutableStateMapOf<Long, List<com.example.myapplication.data.KeyResult>>() }
    val monthlyTasksMap = remember { mutableStateMapOf<Long, List<com.example.myapplication.data.MonthlyTask>>() }
    val weeklyTasksMap = remember { mutableStateMapOf<Long, List<com.example.myapplication.data.WeeklyTask>>() }

    // 加载所有OKR的关键结果
    okrs.forEach { okr ->
        val krs by viewModel.getKeyResultsForOKR(okr.id).collectAsState(initial = emptyList())
        LaunchedEffect(krs) { keyResultsMap[okr.id] = krs }

        // 为每个关键结果加载月度任务
        krs.forEach { kr ->
            val monthlyTasks by viewModel.getMonthlyTasksForKeyResult(kr.id).collectAsState(initial = emptyList())
            LaunchedEffect(monthlyTasks) { monthlyTasksMap[kr.id] = monthlyTasks }

            // 为每个月度任务加载周任务
            monthlyTasks.forEach { mt ->
                val weeklyTasks by viewModel.getWeeklyTasksForMonth(mt.id).collectAsState(initial = emptyList())
                LaunchedEffect(weeklyTasks) { weeklyTasksMap[mt.id] = weeklyTasks }
            }
        }
    }

    // 当前月和周的任务
    val currentYear = viewModel.getCurrentYear()
    val currentMonth = viewModel.getCurrentMonth()
    val currentWeek = viewModel.getCurrentWeekOfYear()

    val currentMonthTasks by viewModel.getMonthlyTasksByYearMonth(currentYear, currentMonth)
        .collectAsState(initial = emptyList())
    val currentWeekTasks by viewModel.getWeeklyTasksByYearWeek(currentYear, currentWeek)
        .collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "OKR") },
                    label = { Text("OKR") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarToday, contentDescription = "日历") },
                    label = { Text("日历") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> NewOKRListScreen(
                modifier = Modifier.padding(padding),
                okrs = okrs,
                getKeyResults = { okrId -> keyResultsMap[okrId] ?: emptyList() },
                getMonthlyTasks = { krId -> monthlyTasksMap[krId] ?: emptyList() },
                getWeeklyTasks = { mtId -> weeklyTasksMap[mtId] ?: emptyList() },
                onAddOKR = { year, quarter, objective ->
                    viewModel.insertOKR(year, quarter, objective)
                },
                onToggleOKR = { okr ->
                    viewModel.updateOKR(okr)
                },
                onAddKeyResult = { okrId, title, desc ->
                    viewModel.insertKeyResult(okrId, title, desc)
                },
                onToggleKeyResult = { kr ->
                    viewModel.updateKeyResult(kr)
                },
                onAddMonthlyTask = { krId, year, month, title, desc ->
                    viewModel.insertMonthlyTask(krId, year, month, title, desc)
                },
                onToggleMonthlyTask = { task ->
                    viewModel.updateMonthlyTask(task)
                },
                onAddWeeklyTask = { mtId, year, week, title, desc ->
                    viewModel.insertWeeklyTask(mtId, year, week, title, desc)
                },
                onToggleWeeklyTask = { task ->
                    viewModel.updateWeeklyTask(task)
                }
            )
            1 -> CalendarScreen(
                modifier = Modifier.padding(padding),
                monthlyTasks = currentMonthTasks,
                weeklyTasks = currentWeekTasks,
                onToggleMonthlyTask = { task ->
                    viewModel.updateMonthlyTask(task)
                },
                onToggleWeeklyTask = { task ->
                    viewModel.updateWeeklyTask(task)
                },
                currentYear = currentYear,
                currentMonth = currentMonth,
                currentWeek = currentWeek
            )
        }
    }
}
