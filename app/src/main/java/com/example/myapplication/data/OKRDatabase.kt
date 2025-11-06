package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [QuarterOKR::class, MonthlyTask::class, WeeklyTask::class],
    version = 1,
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
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
