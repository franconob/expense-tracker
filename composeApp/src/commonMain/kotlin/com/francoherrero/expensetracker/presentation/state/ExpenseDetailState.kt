package com.francoherrero.expensetracker.presentation.state

import com.francoherrero.expensetracker.domain.model.Expense

data class ExpenseDetailState(
    val isLoading: Boolean = false,
    val expense: Expense? = null,
    val error: String? = null
)
