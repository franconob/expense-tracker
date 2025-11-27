package com.francoherrero.expensetracker.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

actual class CurrencyFormatterImpl: CurrencyFormatter {
    private val fallbackCurrency = "ARS"

    actual override fun format(amount: Long, currency: String): String {
        val locale = Locale.getDefault()
        val numberFormat = NumberFormat.getCurrencyInstance(locale).apply {
            try {
                this.currency = Currency.getInstance(currency)
            } catch (e: Exception) {
                this.currency = Currency.getInstance(fallbackCurrency)
            }
        }

        return numberFormat.format(amount / 100)
    }
}