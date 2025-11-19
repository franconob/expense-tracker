package com.francoherrero.expensetracker.ui.expense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import com.francoherrero.expensetracker.presentation.viewmodel.AddExpenseViewModel

@Composable
fun AddExpenseRoute(
    modifier: Modifier,
    onDone: () -> Unit
) {
    val viewModel: AddExpenseViewModel = koinInject()
    val state by viewModel.state.collectAsState()

    // When saved = true, notify caller once and reset flag
    LaunchedEffect(state.saved) {
        if (state.saved) {
            onDone()
            viewModel.onSavedHandled()
        }
    }

    AddExpenseScreen(
        modifier,
        state = state,
        onTitleChange = viewModel::onTitleChange,
        onAmountChange = viewModel::onAmountChange,
        onCurrencyChange = viewModel::onCurrencyChange,
        onCategoryChange = viewModel::onCategoryChange,
        onNotesChange = viewModel::onNotesChange,
        onSaveClick = viewModel::onSaveClick
        // Date picker wiring could be added later
    )
}
