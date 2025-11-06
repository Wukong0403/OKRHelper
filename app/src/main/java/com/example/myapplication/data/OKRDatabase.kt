package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [QuarterOKR::class, KeyResult::class, MonthlyTask::class, WeeklyTask::class],
    version = 2,
    exportSchema = false
)
abstract class OKRDatabase : RoomDatabase() {
    abstract fun okrDao(): OKRDao

    companion object {
        @Volatile
        private var INSTANCE: OKRDatabase? = null

        fun getDatabase(context: Context): OKRDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OKRDatabase::class.java,
                    "okr_database"
                )
                    .fallbackToDestructiveMigration() // 简单起见，重新创建数据库
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
