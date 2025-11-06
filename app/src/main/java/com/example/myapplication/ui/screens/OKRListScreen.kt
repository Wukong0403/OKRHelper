package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.MonthlyTask
import com.example.myapplication.data.QuarterOKR
import com.example.myapplication.data.WeeklyTask
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OKRListScreen(
    okrs: List<QuarterOKR>,
    getMonthlyTasks: (Long) -> List<MonthlyTask>,
    getWeeklyTasks: (Long) -> List<WeeklyTask>,
    onAddOKR: (Int, Int, String, String) -> Unit,
    onToggleOKRComplete: (QuarterOKR) -> Unit,
    onAddMonthlyTask: (Long, Int, String, String) -> Unit,
    onToggleMonthlyTaskComplete: (MonthlyTask) -> Unit,
    onAddWeeklyTask: (Long, Int, String, String) -> Unit,
    onToggleWeeklyTaskComplete: (WeeklyTask) -> Unit,
    modifier: Modifier = Modifier
) {
    var showOKRDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("季度OKR规划") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showOKRDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "添加OKR")
            }
        }
    ) { paddingValues ->
        if (okrs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无OKR，点击 + 添加",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(okrs) { okr ->
                    ExpandableOKRCard(
                        okr = okr,
                        monthlyTasks = getMonthlyTasks(okr.id),
                        getWeeklyTasks = getWeeklyTasks,
                        onToggleOKRComplete = { onToggleOKRComplete(okr) },
                        onAddMonthlyTask = { month, title, description ->
                            onAddMonthlyTask(okr.id, month, title, description)
                        },
                        onToggleMonthlyTaskComplete = onToggleMonthlyTaskComplete,
                        onAddWeeklyTask = onAddWeeklyTask,
                        onToggleWeeklyTaskComplete = onToggleWeeklyTaskComplete
                    )
                }
            }
        }

        if (showOKRDialog) {
            AddOKRDialog(
                onDismiss = { showOKRDialog = false },
                onConfirm = { year, quarter, objective, keyResults ->
                    onAddOKR(year, quarter, objective, keyResults)
                    showOKRDialog = false
                }
            )
        }
    }
}

@Composable
fun ExpandableOKRCard(
    okr: QuarterOKR,
    monthlyTasks: List<MonthlyTask>,
    getWeeklyTasks: (Long) -> List<WeeklyTask>,
    onToggleOKRComplete: () -> Unit,
    onAddMonthlyTask: (Int, String, String) -> Unit,
    onToggleMonthlyTaskComplete: (MonthlyTask) -> Unit,
    onAddWeeklyTask: (Long, Int, String, String) -> Unit,
    onToggleWeeklyTaskComplete: (WeeklyTask) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var showMonthDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${okr.year}年 Q${okr.quarter}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "收起" else "展开"
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = okr.objective,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                IconButton(onClick = onToggleOKRComplete) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "完成状态",
                        tint = if (okr.isCompleted)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "关键结果: ${okr.keyResults}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "月度任务",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        TextButton(onClick = { showMonthDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("添加月度任务")
                        }
                    }

                    if (monthlyTasks.isEmpty()) {
                        Text(
                            text = "暂无月度任务",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        monthlyTasks.forEach { monthlyTask ->
                            ExpandableMonthlyTaskItem(
                                task = monthlyTask,
                                weeklyTasks = getWeeklyTasks(monthlyTask.id),
                                onToggleComplete = { onToggleMonthlyTaskComplete(monthlyTask) },
                                onAddWeeklyTask = { week, title, description ->
                                    onAddWeeklyTask(monthlyTask.id, week, title, description)
                                },
                                onToggleWeeklyTaskComplete = onToggleWeeklyTaskComplete
                            )
                        }
                    }
                }
            }
        }
    }

    if (showMonthDialog) {
        AddMonthlyTaskDialog(
            onDismiss = { showMonthDialog = false },
            onConfirm = { month, title, description ->
                onAddMonthlyTask(month, title, description)
                showMonthDialog = false
            }
        )
    }
}

@Composable
fun ExpandableMonthlyTaskItem(
    task: MonthlyTask,
    weeklyTasks: List<WeeklyTask>,
    onToggleComplete: () -> Unit,
    onAddWeeklyTask: (Int, String, String) -> Unit,
    onToggleWeeklyTaskComplete: (WeeklyTask) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var showWeekDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${task.month}月",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "收起" else "展开",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (task.description.isNotBlank()) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
                IconButton(onClick = onToggleComplete, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "完成状态",
                        tint = if (task.isCompleted)
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "周任务",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        TextButton(
                            onClick = { showWeekDialog = true },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("添加周任务", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    if (weeklyTasks.isEmpty()) {
                        Text(
                            text = "暂无周任务",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.padding(4.dp)
                        )
                    } else {
                        weeklyTasks.forEach { weeklyTask ->
                            WeeklyTaskItem(
                                task = weeklyTask,
                                onToggleComplete = { onToggleWeeklyTaskComplete(weeklyTask) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showWeekDialog) {
        AddWeeklyTaskDialog(
            onDismiss = { showWeekDialog = false },
            onConfirm = { week, title, description ->
                onAddWeeklyTask(week, title, description)
                showWeekDialog = false
            }
        )
    }
}

@Composable
fun WeeklyTaskItem(
    task: WeeklyTask,
    onToggleComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "第${task.weekNumber}周",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodySmall
                )
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.6f)
                    )
                }
            }
            IconButton(onClick = onToggleComplete, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "完成状态",
                    tint = if (task.isCompleted)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun AddOKRDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, String, String) -> Unit
) {
    var year by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var quarter by remember { mutableStateOf(1) }
    var objective by remember { mutableStateOf("") }
    var keyResults by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加季度OKR") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = year.toString(),
                    onValueChange = { year = it.toIntOrNull() ?: year },
                    label = { Text("年份") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quarter.toString(),
                    onValueChange = { quarter = it.toIntOrNull()?.coerceIn(1, 4) ?: quarter },
                    label = { Text("季度 (1-4)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = objective,
                    onValueChange = { objective = it },
                    label = { Text("目标 (Objective)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = keyResults,
                    onValueChange = { keyResults = it },
                    label = { Text("关键结果 (Key Results)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(year, quarter, objective, keyResults) },
                enabled = objective.isNotBlank() && keyResults.isNotBlank()
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
