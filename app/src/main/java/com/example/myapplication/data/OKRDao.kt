package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OKRDao {
    // QuarterOKR operations
    @Query("SELECT * FROM quarter_okrs ORDER BY year DESC, quarter DESC")
    fun getAllOKRs(): Flow<List<QuarterOKR>>

    @Query("SELECT * FROM quarter_okrs WHERE id = :id")
    fun getOKRById(id: Long): Flow<QuarterOKR?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOKR(okr: QuarterOKR): Long

    @Update
    suspend fun updateOKR(okr: QuarterOKR)

    @Delete
    suspend fun deleteOKR(okr: QuarterOKR)

    // MonthlyTask operations
    @Query("SELECT * FROM monthly_tasks WHERE okrId = :okrId ORDER BY month ASC")
    fun getMonthlyTasksForOKR(okrId: Long): Flow<List<MonthlyTask>>

    @Query("SELECT * FROM monthly_tasks WHERE id = :id")
    fun getMonthlyTaskById(id: Long): Flow<MonthlyTask?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthlyTask(task: MonthlyTask): Long

    @Update
    suspend fun updateMonthlyTask(task: MonthlyTask)

    @Delete
    suspend fun deleteMonthlyTask(task: MonthlyTask)

    // WeeklyTask operations
    @Query("SELECT * FROM weekly_tasks WHERE monthlyTaskId = :monthlyTaskId ORDER BY weekNumber ASC")
    fun getWeeklyTasksForMonth(monthlyTaskId: Long): Flow<List<WeeklyTask>>

    @Query("SELECT * FROM weekly_tasks WHERE id = :id")
    fun getWeeklyTaskById(id: Long): Flow<WeeklyTask?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyTask(task: WeeklyTask): Long

    @Update
    suspend fun updateWeeklyTask(task: WeeklyTask)

    @Delete
    suspend fun deleteWeeklyTask(task: WeeklyTask)
}
