package com.francoherrero.expensetracker.ui.expense

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.francoherrero.expensetracker.domain.model.Expense
import com.francoherrero.expensetracker.presentation.state.ExpenseListState
import kotlin.time.ExperimentalTime

@Composable
fun ExpenseListScreen(
    state: ExpenseListState,
    onAddClick: () -> Unit,
    onExpenseClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.error != null -> {
                Text(
                    text = state.error ?: "Unknown error",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.items.isEmpty() -> {
                Text(
                    text = "No expenses yet.\nTap + to add one.",
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(state.items) { expense ->
                        ExpenseRow(
                            expense = expense,
                            onClick = { onExpenseClick(expense.id) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun ExpenseRow(
    expense: Expense,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = expense.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!expense.category.isNullOrBlank()) {
                Text(
                    text = expense.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${expense.money.currency} ${(expense.money.amountCents / 100.0)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = expense.createdAt.toString(), // you can pretty-format later
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
