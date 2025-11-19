package com.francoherrero.expensetracker.data.mapper

import com.francoherrero.expensetracker.db.Expense
import com.francoherrero.expensetracker.domain.model.Money
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Expense.toDomain () = com.francoherrero.expensetracker.domain.model.Expense(
    id = id,
    title = title,
    money = Money(amount_cents, currency),
    category = category,
    notes = notes,
    createdAt = Instant.parse(created_at),
    updatedAt = Instant.parse(updated_at)
)