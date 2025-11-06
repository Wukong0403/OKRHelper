package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow

class OKRRepository(private val dao: OKRDao) {
    // QuarterOKR
    fun getAllOKRs(): Flow<List<QuarterOKR>> = dao.getAllOKRs()
    fun getOKRById(id: Long): Flow<QuarterOKR?> = dao.getOKRById(id)
    suspend fun insertOKR(okr: QuarterOKR): Long = dao.insertOKR(okr)
    suspend fun updateOKR(okr: QuarterOKR) = dao.updateOKR(okr)
    suspend fun deleteOKR(okr: QuarterOKR) = dao.deleteOKR(okr)

    // MonthlyTask
    fun getMonthlyTasksForOKR(okrId: Long): Flow<List<MonthlyTask>> =
        dao.getMonthlyTasksForOKR(okrId)
    fun getMonthlyTaskById(id: Long): Flow<MonthlyTask?> = dao.getMonthlyTaskById(id)
    suspend fun insertMonthlyTask(task: MonthlyTask): Long = dao.insertMonthlyTask(task)
    suspend fun updateMonthlyTask(task: MonthlyTask) = dao.updateMonthlyTask(task)
    suspend fun deleteMonthlyTask(task: MonthlyTask) = dao.deleteMonthlyTask(task)

    // WeeklyTask
    fun getWeeklyTasksForMonth(monthlyTaskId: Long): Flow<List<WeeklyTask>> =
        dao.getWeeklyTasksForMonth(monthlyTaskId)
    fun getWeeklyTaskById(id: Long): Flow<WeeklyTask?> = dao.getWeeklyTaskById(id)
    suspend fun insertWeeklyTask(task: WeeklyTask): Long = dao.insertWeeklyTask(task)
    suspend fun updateWeeklyTask(task: WeeklyTask) = dao.updateWeeklyTask(task)
    suspend fun deleteWeeklyTask(task: WeeklyTask) = dao.deleteWeeklyTask(task)
}
