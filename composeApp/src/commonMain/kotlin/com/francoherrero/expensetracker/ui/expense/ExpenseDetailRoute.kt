package com.francoherrero.expensetracker.ui.expense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.francoherrero.expensetracker.presentation.viewmodel.ExpenseDetailViewModel
import org.koin.compose.koinInject

@Composable
fun ExpenseDetailRoute(
    expenseId: String
) {
    val viewmodel = koinInject<ExpenseDetailViewModel>()
    val uiState by viewmodel.state.collectAsState()

    LaunchedEffect(expenseId) {
        viewmodel.getExpense(expenseId)
    }

    uiState.expense?.let {
        ExpenseDetailScreen(expense = it)
    }
}