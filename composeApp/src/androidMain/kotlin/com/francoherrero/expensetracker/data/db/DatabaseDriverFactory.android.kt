package com.francoherrero.expensetracker.data.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.francoherrero.expensetracker.db.ExpenseDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(ExpenseDatabase.Schema, context, "expenses.db")
}