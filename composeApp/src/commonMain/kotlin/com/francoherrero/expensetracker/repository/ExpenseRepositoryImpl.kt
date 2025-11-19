package com.francoherrero.expensetracker.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.francoherrero.expensetracker.data.mapper.toDomain
import com.francoherrero.expensetracker.db.ExpenseDatabase
import com.francoherrero.expensetracker.domain.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ExpenseRepositoryImpl(db: ExpenseDatabase): ExpenseRepository {

    private val queries = db.expenseQueries

    override fun observeExpense(): Flow<List<Expense>>  =
        queries.selectAllExpenses().asFlow().mapToList(Dispatchers.Default).map { rows -> rows.map { it.toDomain()} }


    override suspend fun getExpense(id: String): Expense? =
        queries.selectExpenseById(id).asFlow().mapToOneOrNull(Dispatchers.Default).map { it?.toDomain() }.firstOrNull()


    override suspend fun addExpense(expense: Expense) {
        val now = Clock.System.now()

        queries.insertExpense(
            id = expense.id,
            title = expense.title,
            amount_cents = expense.money.amountCents,
            currency = expense.money.currency,
            category = expense.category,
            notes = expense.notes,
            created_at = now.toString(),
            updated_at = now.toString(),
        )
    }

    override suspend fun updateExpense(expense: Expense) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteExpense(id: String) {
        queries.deleteExpenseById(id)
    }
}