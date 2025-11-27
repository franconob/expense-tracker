package com.francoherrero.expensetracker.navigation

import kotlinx.serialization.Serializable

@Serializable
object ExpenseListDestination

@Serializable
object AddExpenseDestination

@Serializable
data class ExpenseDetailDestination(val expenseId: String)

// Later you can add:
// @Serializable data class ExpenseDetailDestination(val id: String)
