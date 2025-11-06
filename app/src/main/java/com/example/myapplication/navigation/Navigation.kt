package com.example.myapplication.navigation

sealed class Screen(val route: String) {
    object OKRList : Screen("okr_list")
    object OKRDetail : Screen("okr_detail/{okrId}") {
        fun createRoute(okrId: Long) = "okr_detail/$okrId"
    }
    object MonthDetail : Screen("month_detail/{monthlyTaskId}") {
        fun createRoute(monthlyTaskId: Long) = "month_detail/$monthlyTaskId"
    }
}
