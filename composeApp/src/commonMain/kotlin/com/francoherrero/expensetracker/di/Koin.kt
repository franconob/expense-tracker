package com.francoherrero.expensetracker.di

import app.cash.sqldelight.db.SqlDriver
import com.francoherrero.expensetracker.data.db.DatabaseDriverFactory
import com.francoherrero.expensetracker.db.ExpenseDatabase
import com.francoherrero.expensetracker.presentation.viewmodel.AddExpenseViewModel
import com.francoherrero.expensetracker.presentation.viewmodel.ExpenseListViewModel
import com.francoherrero.expensetracker.repository.ExpenseRepository
import com.francoherrero.expensetracker.repository.ExpenseRepositoryImpl
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(databaseDriverFactory: DatabaseDriverFactory): KoinApplication {
    val dataModule = module {
        single<DatabaseDriverFactory> { databaseDriverFactory }

        single<SqlDriver> { get<DatabaseDriverFactory>().createDriver() }

        single<ExpenseDatabase> {
            val driver: SqlDriver = get()
            ExpenseDatabase(driver)
        }

        single<ExpenseRepository> {
            val db: ExpenseDatabase = get()
            ExpenseRepositoryImpl(db)
        }
    }

    val presentationModule = module {
        single<ExpenseListViewModel> { ExpenseListViewModel(get()) }
        single<AddExpenseViewModel> { AddExpenseViewModel(get()) }
    }

    return startKoin { modules(dataModule, presentationModule)  }
}