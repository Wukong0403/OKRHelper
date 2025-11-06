package com.example.myapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.QuarterOKR
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OKRListScreen(
    okrs: List<QuarterOKR>,
    onOKRClick: (Long) -> Unit,
    onAddOKR: (Int, Int, String, String) -> Unit,
    onToggleComplete: (QuarterOKR) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

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
            FloatingActionButton(onClick = { showDialog = true }) {
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
                    OKRCard(
                        okr = okr,
                        onClick = { onOKRClick(okr.id) },
                        onToggleComplete = { onToggleComplete(okr) }
                    )
                }
            }
        }

        if (showDialog) {
            AddOKRDialog(
                onDismiss = { showDialog = false },
                onConfirm = { year, quarter, objective, keyResults ->
                    onAddOKR(year, quarter, objective, keyResults)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun OKRCard(
    okr: QuarterOKR,
    onClick: () -> Unit,
    onToggleComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
                    text = "${okr.year}年 Q${okr.quarter}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onToggleComplete) {
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
                text = okr.objective,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "关键结果: ${okr.keyResults}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
