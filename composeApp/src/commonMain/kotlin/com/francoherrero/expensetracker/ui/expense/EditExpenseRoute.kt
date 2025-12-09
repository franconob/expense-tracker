package com.francoherrero.expensetracker.ui.expense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.francoherrero.expensetracker.presentation.viewmodel.AddExpenseViewModel
import org.koin.compose.koinInject

@Composable
fun EditExpenseRoute(
    expenseId: String,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val viewModel = koinInject<AddExpenseViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(expenseId) {
        viewModel.loadExpenseById(expenseId)
    }

    state.existingExpenseId?.let {
        ExpenseFormScreen(
            state = state,
            modifier = Modifier,
            onTitleChange = viewModel::onTitleChange,
            onAmountChange = viewModel::onAmountChange,
            onCurrencyChange = viewModel::onCurrencyChange,
            onCategoryChange = viewModel::onCategoryChange,
            onNotesChange = viewModel::onNotesChange,
            onSaveClick = {
                viewModel.onSaveClick()
                onSave()
            },
        )
    }
}