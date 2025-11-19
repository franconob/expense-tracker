package com.francoherrero.expensetracker.repository

import com.francoherrero.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun observeExpense(): Flow<List<Expense>>
    suspend fun getExpense(id: String): Expense?
    suspend fun addExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(id: String)
}