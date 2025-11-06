package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow

class OKRRepository(private val dao: OKRDao) {
    // QuarterOKR
    fun getAllOKRs(): Flow<List<QuarterOKR>> = dao.getAllOKRs()
    fun getOKRById(id: Long): Flow<QuarterOKR?> = dao.getOKRById(id)
    suspend fun insertOKR(okr: QuarterOKR): Long = dao.insertOKR(okr)
    suspend fun updateOKR(okr: QuarterOKR) = dao.updateOKR(okr)
    suspend fun deleteOKR(okr: QuarterOKR) = dao.deleteOKR(okr)

    // KeyResult
    fun getKeyResultsForOKR(okrId: Long): Flow<List<KeyResult>> = dao.getKeyResultsForOKR(okrId)
    fun getKeyResultById(id: Long): Flow<KeyResult?> = dao.getKeyResultById(id)
    suspend fun insertKeyResult(keyResult: KeyResult): Long = dao.insertKeyResult(keyResult)
    suspend fun updateKeyResult(keyResult: KeyResult) = dao.updateKeyResult(keyResult)
    suspend fun deleteKeyResult(keyResult: KeyResult) = dao.deleteKeyResult(keyResult)

    // MonthlyTask
    fun getMonthlyTasksForKeyResult(keyResultId: Long): Flow<List<MonthlyTask>> =
        dao.getMonthlyTasksForKeyResult(keyResultId)
    fun getMonthlyTasksByYearMonth(year: Int, month: Int): Flow<List<MonthlyTask>> =
        dao.getMonthlyTasksByYearMonth(year, month)
    fun getMonthlyTaskById(id: Long): Flow<MonthlyTask?> = dao.getMonthlyTaskById(id)
    suspend fun insertMonthlyTask(task: MonthlyTask): Long = dao.insertMonthlyTask(task)
    suspend fun updateMonthlyTask(task: MonthlyTask) = dao.updateMonthlyTask(task)
    suspend fun deleteMonthlyTask(task: MonthlyTask) = dao.deleteMonthlyTask(task)

    // WeeklyTask
    fun getWeeklyTasksForMonth(monthlyTaskId: Long): Flow<List<WeeklyTask>> =
        dao.getWeeklyTasksForMonth(monthlyTaskId)
    fun getWeeklyTasksByYearWeek(year: Int, weekNumber: Int): Flow<List<WeeklyTask>> =
        dao.getWeeklyTasksByYearWeek(year, weekNumber)
    fun getWeeklyTaskById(id: Long): Flow<WeeklyTask?> = dao.getWeeklyTaskById(id)
    suspend fun insertWeeklyTask(task: WeeklyTask): Long = dao.insertWeeklyTask(task)
    suspend fun updateWeeklyTask(task: WeeklyTask) = dao.updateWeeklyTask(task)
    suspend fun deleteWeeklyTask(task: WeeklyTask) = dao.deleteWeeklyTask(task)
}
