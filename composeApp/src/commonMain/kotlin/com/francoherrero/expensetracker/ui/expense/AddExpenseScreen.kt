package com.francoherrero.expensetracker.ui.expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.francoherrero.expensetracker.presentation.controller.UIEventController
import com.francoherrero.expensetracker.presentation.state.AddExpenseState

@Composable
fun AddExpenseScreen(
    modifier: Modifier,
    state: AddExpenseState,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {

    val uiEventController = remember { UIEventController() }

    LaunchedEffect(state.error, state.saved) {
        val error = state.error
        if (error != null) {
            uiEventController.showSnackbar(error)
        } else if (state.saved) {
            uiEventController.showSnackbar("Expense saved successfully")
        }
    }

    Box(
        modifier =
            modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = onTitleChange,
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.amountInput,
                onValueChange = onAmountChange,
                label = { Text("Amount") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.currency,
                onValueChange = onCurrencyChange,
                label = { Text("Currency") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.category,
                onValueChange = onCategoryChange,
                label = { Text("Category (optional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.notes,
                onValueChange = onNotesChange,
                label = { Text("Notes (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSaveClick,
                enabled = !state.isSaving,
                modifier = Modifier.align(Alignment.End)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                }
                Text("Save")
            }
        }
    }
}

