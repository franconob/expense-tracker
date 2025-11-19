package com.francoherrero.expensetracker.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.francoherrero.expensetracker.db.ExpenseDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(ExpenseDatabase.Schema, "test.db")
}