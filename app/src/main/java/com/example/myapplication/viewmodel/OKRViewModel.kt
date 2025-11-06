package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OKRViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: OKRRepository
    val allOKRs: StateFlow<List<QuarterOKR>>

    init {
        val database = OKRDatabase.getDatabase(application)
        repository = OKRRepository(database.okrDao())
        allOKRs = repository.getAllOKRs()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    // OKR operations
    fun getOKRById(id: Long): Flow<QuarterOKR?> = repository.getOKRById(id)

    fun insertOKR(year: Int, quarter: Int, objective: String, keyResults: String) {
        viewModelScope.launch {
            val okr = QuarterOKR(
                year = year,
                quarter = quarter,
                objective = objective,
                keyResults = keyResults
            )
            repository.insertOKR(okr)
        }
    }

    fun updateOKR(okr: QuarterOKR) {
        viewModelScope.launch {
            repository.updateOKR(okr)
        }
    }

    fun deleteOKR(okr: QuarterOKR) {
        viewModelScope.launch {
            repository.deleteOKR(okr)
        }
    }

    // Monthly Task operations
    fun getMonthlyTasksForOKR(okrId: Long): Flow<List<MonthlyTask>> =
        repository.getMonthlyTasksForOKR(okrId)

    fun getMonthlyTaskById(id: Long): Flow<MonthlyTask?> =
        repository.getMonthlyTaskById(id)

    fun insertMonthlyTask(okrId: Long, month: Int, title: String, description: String = "") {
        viewModelScope.launch {
            val task = MonthlyTask(
                okrId = okrId,
                month = month,
                title = title,
                description = description
            )
            repository.insertMonthlyTask(task)
        }
    }

    fun updateMonthlyTask(task: MonthlyTask) {
        viewModelScope.launch {
            repository.updateMonthlyTask(task)
        }
    }

    fun deleteMonthlyTask(task: MonthlyTask) {
        viewModelScope.launch {
            repository.deleteMonthlyTask(task)
        }
    }

    // Weekly Task operations
    fun getWeeklyTasksForMonth(monthlyTaskId: Long): Flow<List<WeeklyTask>> =
        repository.getWeeklyTasksForMonth(monthlyTaskId)

    fun insertWeeklyTask(monthlyTaskId: Long, weekNumber: Int, title: String, description: String = "") {
        viewModelScope.launch {
            val task = WeeklyTask(
                monthlyTaskId = monthlyTaskId,
                weekNumber = weekNumber,
                title = title,
                description = description
            )
            repository.insertWeeklyTask(task)
        }
    }

    fun updateWeeklyTask(task: WeeklyTask) {
        viewModelScope.launch {
            repository.updateWeeklyTask(task)
        }
    }

    fun deleteWeeklyTask(task: WeeklyTask) {
        viewModelScope.launch {
            repository.deleteWeeklyTask(task)
        }
    }
}
