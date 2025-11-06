package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quarter_okrs")
data class QuarterOKR(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val year: Int,
    val quarter: Int, // 1, 2, 3, 4
    val objective: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
)
