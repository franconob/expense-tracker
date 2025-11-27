package com.francoherrero.expensetracker.utils

expect class CurrencyFormatterImpl(): CurrencyFormatter {
    override fun format(amount: Long, currency: String): String
}