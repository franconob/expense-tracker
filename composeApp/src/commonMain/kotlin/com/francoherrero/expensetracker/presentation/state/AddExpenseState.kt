package com.francoherrero.expensetracker.presentation.state

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class AddExpenseState @OptIn(ExperimentalTime::class) constructor(
    val title: String = "",
    val amountInput: String = "",
    val currency: String = "ARS",
    val category: String = "",
    val description: String = "",
    val notes: String = "",
    val createdAt: Instant = Clock.System.now(),
    val isSaving: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false,
)
