package com.francoherrero.expensetracker.ui.expense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject
import com.francoherrero.expensetracker.presentation.viewmodel.ExpenseListViewModel

@Composable
fun ExpenseListRoute(
    onAddClick: () -> Unit,
    onExpenseClick: (String) -> Unit
) {
    val viewModel: ExpenseListViewModel = koinInject()
    val state by viewModel.state.collectAsState()

    ExpenseListScreen(
        state = state,
        onAddClick = onAddClick,
        onExpenseClick = onExpenseClick
    )
}
