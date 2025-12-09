package com.francoherrero.expensetracker.ui.expense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.francoherrero.expensetracker.domain.model.Expense
import com.francoherrero.expensetracker.utils.CurrencyFormatter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import org.koin.compose.koinInject
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ExpenseDetailScreen(
    expense: Expense,
    modifier: Modifier = Modifier,
    onEditClick: (expenseId: String) -> Unit = {},
    onDeleteClick: (expenseId: String) -> Unit = {}
) {
    val currencyFormatter = koinInject<CurrencyFormatter>()
    val formatDate = LocalDate.Format {
        day(); char('/'); monthNumber(); char('/'); year();
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title + amount card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = currencyFormatter.format(expense.money.amountCents, expense.money.currency),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = expense.createdAt.format(DateTimeComponents.Format {
                        date(formatDate)
                    }),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (!expense.category.isNullOrBlank()) {
                    Text(
                        text = "Category: ${expense.category}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Notes / extra info
        if (!expense.notes.isNullOrBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = expense.notes,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Divider()

        // Actions (you can wire these later)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onEditClick(expense.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit")
            }

            Button(
                onClick = { onDeleteClick(expense.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
