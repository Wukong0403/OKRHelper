package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOKRListScreen(
    okrs: List<QuarterOKR>,
    getKeyResults: (Long) -> List<KeyResult>,
    getMonthlyTasks: (Long) -> List<MonthlyTask>,
    getWeeklyTasks: (Long) -> List<WeeklyTask>,
    onAddOKR: (Int, Int, String) -> Unit,
    onToggleOKR: (QuarterOKR) -> Unit,
    onAddKeyResult: (Long, String, String) -> Unit,
    onToggleKeyResult: (KeyResult) -> Unit,
    onAddMonthlyTask: (Long, Int, Int, String, String) -> Unit,
    onToggleMonthlyTask: (MonthlyTask) -> Unit,
    onAddWeeklyTask: (Long, Int, Int, String, String) -> Unit,
    onToggleWeeklyTask: (WeeklyTask) -> Unit,
    modifier: Modifier = Modifier
) {
    var showOKRDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OKR规划") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showOKRDialog = true }) {
                Icon(Icons.Default.Add, "添加OKR")
            }
        }
    ) { padding ->
        if (okrs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("暂无OKR，点击+添加")
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(okrs) { okr ->
                    OKRCard(
                        okr = okr,
                        keyResults = getKeyResults(okr.id),
                        getMonthlyTasks = getMonthlyTasks,
                        getWeeklyTasks = getWeeklyTasks,
                        onToggleOKR = onToggleOKR,
                        onAddKeyResult = onAddKeyResult,
                        onToggleKeyResult = onToggleKeyResult,
                        onAddMonthlyTask = onAddMonthlyTask,
                        onToggleMonthlyTask = onToggleMonthlyTask,
                        onAddWeeklyTask = onAddWeeklyTask,
                        onToggleWeeklyTask = onToggleWeeklyTask
                    )
                }
            }
        }

        if (showOKRDialog) {
            AddOKRDialog(
                onDismiss = { showOKRDialog = false },
                onConfirm = { year, quarter, objective ->
                    onAddOKR(year, quarter, objective)
                    showOKRDialog = false
                }
            )
        }
    }
}

@Composable
fun OKRCard(
    okr: QuarterOKR,
    keyResults: List<KeyResult>,
    getMonthlyTasks: (Long) -> List<MonthlyTask>,
    getWeeklyTasks: (Long) -> List<WeeklyTask>,
    onToggleOKR: (QuarterOKR) -> Unit,
    onAddKeyResult: (Long, String, String) -> Unit,
    onToggleKeyResult: (KeyResult) -> Unit,
    onAddMonthlyTask: (Long, Int, Int, String, String) -> Unit,
    onToggleMonthlyTask: (MonthlyTask) -> Unit,
    onAddWeeklyTask: (Long, Int, Int, String, String) -> Unit,
    onToggleWeeklyTask: (WeeklyTask) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showKRDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${okr.year}年 Q${okr.quarter}", style = MaterialTheme.typography.labelLarge)
                        Spacer(Modifier.width(8.dp))
                        Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null)
                    }
                    Text(okr.objective, style = MaterialTheme.typography.titleMedium)
                }
                IconButton(onClick = { onToggleOKR(okr.copy(isCompleted = !okr.isCompleted)) }) {
                    Icon(
                        Icons.Default.CheckCircle,
                        null,
                        tint = if (okr.isCompleted) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    )
                }
            }

            AnimatedVisibility(expanded, enter = expandVertically(), exit = shrinkVertically()) {
                Column(Modifier.padding(top = 12.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("关键结果", style = MaterialTheme.typography.titleSmall)
                        TextButton(onClick = { showKRDialog = true }) {
                            Icon(Icons.Default.Add, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("添加")
                        }
                    }

                    if (keyResults.isEmpty()) {
                        Text("暂无关键结果", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(8.dp))
                    } else {
                        keyResults.forEach { kr ->
                            KeyResultCard(
                                kr, getMonthlyTasks(kr.id), getWeeklyTasks,
                                onToggleKeyResult, onAddMonthlyTask, onToggleMonthlyTask,
                                onAddWeeklyTask, onToggleWeeklyTask
                            )
                        }
                    }
                }
            }
        }
    }

    if (showKRDialog) {
        AddKeyResultDialog(
            onDismiss = { showKRDialog = false },
            onConfirm = { title, desc ->
                onAddKeyResult(okr.id, title, desc)
                showKRDialog = false
            }
        )
    }
}

@Composable
fun KeyResultCard(
    kr: KeyResult,
    monthlyTasks: List<MonthlyTask>,
    getWeeklyTasks: (Long) -> List<WeeklyTask>,
    onToggle: (KeyResult) -> Unit,
    onAddMonth: (Long, Int, Int, String, String) -> Unit,
    onToggleMonth: (MonthlyTask) -> Unit,
    onAddWeek: (Long, Int, Int, String, String) -> Unit,
    onToggleWeek: (WeeklyTask) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showMonthDialog by remember { mutableStateOf(false) }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Row {
                        Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(kr.title, style = MaterialTheme.typography.bodyMedium)
                    }
                    if (kr.description.isNotBlank()) {
                        Text(kr.description, style = MaterialTheme.typography.bodySmall)
                    }
                }
                IconButton(onClick = { onToggle(kr.copy(isCompleted = !kr.isCompleted)) }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.CheckCircle, null, Modifier.size(20.dp),
                        tint = if (kr.isCompleted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline)
                }
            }

            AnimatedVisibility(expanded, enter = expandVertically(), exit = shrinkVertically()) {
                Column(Modifier.padding(top = 8.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("月度任务", style = MaterialTheme.typography.labelSmall)
                        TextButton(onClick = { showMonthDialog = true }, contentPadding = PaddingValues(8.dp, 4.dp)) {
                            Icon(Icons.Default.Add, null, Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("添加", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    if (monthlyTasks.isEmpty()) {
                        Text("暂无", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(4.dp))
                    } else {
                        monthlyTasks.forEach { mt ->
                            MonthlyTaskCard(mt, getWeeklyTasks(mt.id), onToggleMonth, onAddWeek, onToggleWeek)
                        }
                    }
                }
            }
        }
    }

    if (showMonthDialog) {
        AddMonthlyTaskDialog(
            onDismiss = { showMonthDialog = false },
            onConfirm = { year, month, title, desc ->
                onAddMonth(kr.id, year, month, title, desc)
                showMonthDialog = false
            }
        )
    }
}

@Composable
fun MonthlyTaskCard(
    mt: MonthlyTask,
    weeklyTasks: List<WeeklyTask>,
    onToggle: (MonthlyTask) -> Unit,
    onAddWeek: (Long, Int, Int, String, String) -> Unit,
    onToggleWeek: (WeeklyTask) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showWeekDialog by remember { mutableStateOf(false) }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row(Modifier.fillMaxWidth().clickable { expanded = !expanded }, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Row {
                        Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("${mt.year}年${mt.month}月", style = MaterialTheme.typography.labelSmall)
                    }
                    Text(mt.title, style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = { onToggle(mt.copy(isCompleted = !mt.isCompleted)) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.CheckCircle, null, Modifier.size(18.dp),
                        tint = if (mt.isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline)
                }
            }

            AnimatedVisibility(expanded, enter = expandVertically(), exit = shrinkVertically()) {
                Column(Modifier.padding(top = 4.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("周任务", style = MaterialTheme.typography.labelSmall)
                        TextButton(onClick = { showWeekDialog = true }, contentPadding = PaddingValues(4.dp)) {
                            Icon(Icons.Default.Add, null, Modifier.size(12.dp))
                            Spacer(Modifier.width(2.dp))
                            Text("添加", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    if (weeklyTasks.isEmpty()) {
                        Text("暂无", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(2.dp))
                    } else {
                        weeklyTasks.forEach { wt ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(Modifier.weight(1f)) {
                                    Text("第${wt.weekNumber}周: ${wt.title}", style = MaterialTheme.typography.bodySmall)
                                }
                                IconButton(onClick = { onToggleWeek(wt.copy(isCompleted = !wt.isCompleted)) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.CheckCircle, null, Modifier.size(16.dp),
                                        tint = if (wt.isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showWeekDialog) {
        AddWeeklyTaskDialog(
            onDismiss = { showWeekDialog = false },
            onConfirm = { year, week, title, desc ->
                onAddWeek(mt.id, year, week, title, desc)
                showWeekDialog = false
            }
        )
    }
}
