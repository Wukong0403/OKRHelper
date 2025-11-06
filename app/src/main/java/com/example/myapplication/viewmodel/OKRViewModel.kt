package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

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

    fun insertOKR(year: Int, quarter: Int, objective: String) {
        viewModelScope.launch {
            val okr = QuarterOKR(
                year = year,
                quarter = quarter,
                objective = objective
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

    // KeyResult operations
    fun getKeyResultsForOKR(okrId: Long): Flow<List<KeyResult>> =
        repository.getKeyResultsForOKR(okrId)

    fun insertKeyResult(okrId: Long, title: String, description: String = "") {
        viewModelScope.launch {
            val keyResult = KeyResult(
                okrId = okrId,
                title = title,
                description = description
            )
            repository.insertKeyResult(keyResult)
        }
    }

    fun updateKeyResult(keyResult: KeyResult) {
        viewModelScope.launch {
            repository.updateKeyResult(keyResult)
        }
    }

    fun deleteKeyResult(keyResult: KeyResult) {
        viewModelScope.launch {
            repository.deleteKeyResult(keyResult)
        }
    }

    // Monthly Task operations
    fun getMonthlyTasksForKeyResult(keyResultId: Long): Flow<List<MonthlyTask>> =
        repository.getMonthlyTasksForKeyResult(keyResultId)

    fun getMonthlyTasksByYearMonth(year: Int, month: Int): Flow<List<MonthlyTask>> =
        repository.getMonthlyTasksByYearMonth(year, month)

    fun getMonthlyTaskById(id: Long): Flow<MonthlyTask?> =
        repository.getMonthlyTaskById(id)

    fun insertMonthlyTask(keyResultId: Long, year: Int, month: Int, title: String, description: String = "") {
        viewModelScope.launch {
            val task = MonthlyTask(
                keyResultId = keyResultId,
                year = year,
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

    fun getWeeklyTasksByYearWeek(year: Int, weekNumber: Int): Flow<List<WeeklyTask>> =
        repository.getWeeklyTasksByYearWeek(year, weekNumber)

    fun insertWeeklyTask(monthlyTaskId: Long, year: Int, weekNumber: Int, title: String, description: String = "") {
        viewModelScope.launch {
            val task = WeeklyTask(
                monthlyTaskId = monthlyTaskId,
                year = year,
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

    // Helper functions
    fun getCurrentWeekOfYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.WEEK_OF_YEAR)
    }

    fun getCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun getCurrentMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-based
    }
}
