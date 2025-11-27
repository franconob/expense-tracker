package com.francoherrero.expensetracker.utils

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle

actual class CurrencyFormatterImpl: CurrencyFormatter {
    actual override fun format(amount: Long, currency: String): String {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterCurrencyStyle
            this.currencyCode = currency
        }

        val number = NSNumber(amount / 100.0)

        return formatter.stringFromNumber(number) ?: ""
    }
}