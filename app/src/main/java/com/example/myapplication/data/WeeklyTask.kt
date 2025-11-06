package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "weekly_tasks",
    foreignKeys = [
        ForeignKey(
            entity = MonthlyTask::class,
            parentColumns = ["id"],
            childColumns = ["monthlyTaskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WeeklyTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val monthlyTaskId: Long,
    val weekNumber: Int, // Week number in the year
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false
)
