package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "monthly_tasks",
    foreignKeys = [
        ForeignKey(
            entity = QuarterOKR::class,
            parentColumns = ["id"],
            childColumns = ["okrId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MonthlyTask(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val okrId: Long,
    val month: Int, // 1-12
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false
)
