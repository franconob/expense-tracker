package com.francoherrero.expensetracker.utils

interface CurrencyFormatter {
    fun format(amount: Long, currency: String): String
}