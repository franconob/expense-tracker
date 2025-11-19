package com.francoherrero.expensetracker.navigation

import kotlinx.serialization.Serializable

interface Route {
    val route: String
}

@Serializable
object ExpenseListDestination: Route {
    override val route: String
        get() = "Expenses"
}

@Serializable
object AddExpenseDestination: Route {
    override val route: String
        get() = "Add expense"
}

// Later you can add:
// @Serializable data class ExpenseDetailDestination(val id: String)
