package com.francoherrero.expensetracker.presentation.viewmodel

import com.francoherrero.expensetracker.domain.model.Expense
import com.francoherrero.expensetracker.domain.model.Money
import com.francoherrero.expensetracker.presentation.state.AddExpenseState
import com.francoherrero.expensetracker.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToLong
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
class AddExpenseViewModel(
    private val repo: ExpenseRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(
        AddExpenseState(
            createdAt = kotlin.time.Clock.System.now()
        )
    )
    val state: StateFlow<AddExpenseState> = _state

    fun onTitleChange(value: String) {
        _state.value = _state.value.copy(title = value, error = null, saved = false)
    }

    fun onAmountChange(value: String) {
        _state.value = _state.value.copy(amountInput = value, error = null, saved = false)
    }

    fun onCurrencyChange(value: String) {
        _state.value = _state.value.copy(currency = value, error = null, saved = false)
    }

    fun onCategoryChange(value: String) {
        _state.value = _state.value.copy(category = value, error = null, saved = false)
    }

    fun onNotesChange(value: String) {
        _state.value = _state.value.copy(notes = value, error = null, saved = false)
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onSaveClick() {
        val current = _state.value

        if (current.title.isBlank()) {
            _state.value = current.copy(error = "Title is required")
            return
        }

        val amountCents = parseAmountToCents(current.amountInput)
        if (amountCents == null || amountCents <= 0L) {
            _state.value = current.copy(error = "Enter a valid amount")
            return
        }

        val now: kotlin.time.Instant = kotlin.time.Clock.System.now()
        val expense = Expense(
            id = Uuid.random().toString(),
            title = current.title.trim(),
            money = Money(
                amountCents = amountCents,
                currency = current.currency.trim()
            ),
            category = current.category.ifBlank { null },
            notes = current.notes.ifBlank { null },
            createdAt = now,
            updatedAt = now
        )

        _state.value = current.copy(isSaving = true, error = null)

        scope.launch {
            try {
                repo.addExpense(expense)
                _state.value = _state.value.copy(
                    isSaving = false,
                    saved = true
                )
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = t.message ?: "Failed to save expense"
                )
            }
        }
    }

    fun onSavedHandled() {
        // To be called by the UI after reacting to saved = true
        _state.value = _state.value.copy(saved = false)
    }

    private fun parseAmountToCents(input: String): Long? {
        val normalized = input.replace(",", ".").trim()
        val value = normalized.toDoubleOrNull() ?: return null
        return (value * 100.0).roundToLong()
    }

    private companion object {
        fun today(): LocalDate {
            val now = kotlin.time.Clock.System.now()
            return now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
    }
}
