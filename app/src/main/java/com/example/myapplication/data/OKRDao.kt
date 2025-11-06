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

    // KeyResult operations
    @Query("SELECT * FROM key_results WHERE okrId = :okrId ORDER BY id ASC")
    fun getKeyResultsForOKR(okrId: Long): Flow<List<KeyResult>>

    @Query("SELECT * FROM key_results WHERE id = :id")
    fun getKeyResultById(id: Long): Flow<KeyResult?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyResult(keyResult: KeyResult): Long

    @Update
    suspend fun updateKeyResult(keyResult: KeyResult)

    @Delete
    suspend fun deleteKeyResult(keyResult: KeyResult)

    // MonthlyTask operations
    @Query("SELECT * FROM monthly_tasks WHERE keyResultId = :keyResultId ORDER BY year ASC, month ASC")
    fun getMonthlyTasksForKeyResult(keyResultId: Long): Flow<List<MonthlyTask>>

    @Query("SELECT * FROM monthly_tasks WHERE year = :year AND month = :month ORDER BY keyResultId ASC")
    fun getMonthlyTasksByYearMonth(year: Int, month: Int): Flow<List<MonthlyTask>>

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

    @Query("SELECT * FROM weekly_tasks WHERE year = :year AND weekNumber = :weekNumber ORDER BY monthlyTaskId ASC")
    fun getWeeklyTasksByYearWeek(year: Int, weekNumber: Int): Flow<List<WeeklyTask>>

    @Query("SELECT * FROM weekly_tasks WHERE id = :id")
    fun getWeeklyTaskById(id: Long): Flow<WeeklyTask?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyTask(task: WeeklyTask): Long

    @Update
    suspend fun updateWeeklyTask(task: WeeklyTask)

    @Delete
    suspend fun deleteWeeklyTask(task: WeeklyTask)
}
