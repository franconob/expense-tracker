package com.francoherrero.expensetracker.presentation.state

import com.francoherrero.expensetracker.domain.model.Expense

data class ExpenseListState(
    val isLoading: Boolean = false,
    val items: List<Expense> = emptyList(),
    val error: String? = null
)