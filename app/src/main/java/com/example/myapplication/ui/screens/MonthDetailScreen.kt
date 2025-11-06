package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.MonthlyTask
import com.example.myapplication.data.WeeklyTask

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthDetailScreen(
    monthlyTask: MonthlyTask?,
    weeklyTasks: List<WeeklyTask>,
    onNavigateBack: () -> Unit,
    onAddWeeklyTask: (Int, String, String) -> Unit,
    onToggleTaskComplete: (WeeklyTask) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (monthlyTask != null) "${monthlyTask.month}月 - 周计划"
                        else "加载中..."
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "添加周任务")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (monthlyTask != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "月度任务",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = monthlyTask.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (monthlyTask.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = monthlyTask.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            if (weeklyTasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无周任务，点击 + 添加",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(weeklyTasks) { task ->
                        WeeklyTaskCard(
                            task = task,
                            onToggleComplete = { onToggleTaskComplete(task) }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AddWeeklyTaskDialog(
                onDismiss = { showDialog = false },
                onConfirm = { week, title, description ->
                    onAddWeeklyTask(week, title, description)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun WeeklyTaskCard(
    task: WeeklyTask,
    onToggleComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "第${task.weekNumber}周",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onToggleComplete) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "完成状态",
                        tint = if (task.isCompleted)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium
            )
            if (task.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AddWeeklyTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, String, String) -> Unit
) {
    var weekNumber by remember { mutableStateOf(1) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加周任务") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = weekNumber.toString(),
                    onValueChange = { weekNumber = it.toIntOrNull()?.coerceIn(1, 53) ?: weekNumber },
                    label = { Text("周数 (1-53)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("任务标题") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("任务描述") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(weekNumber, title, description) },
                enabled = title.isNotBlank()
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
