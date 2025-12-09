package com.francoherrero.expensetracker.presentation.viewmodel

import com.francoherrero.expensetracker.domain.model.Expense
import com.francoherrero.expensetracker.domain.model.Money
import com.francoherrero.expensetracker.presentation.state.ExpenseFormState
import com.francoherrero.expensetracker.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
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
        ExpenseFormState(
            createdAt = kotlin.time.Clock.System.now()
        )
    )
    val state: StateFlow<ExpenseFormState> = _state

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

    fun loadExpenseById(expenseId: String) {
        scope.launch {
            val expense = repo.getExpense(expenseId).firstOrNull()
            if (expense != null) {
                _state.value = _state.value.copy(
                    existingExpenseId = expenseId,
                    title = expense.title,
                    amountInput = (expense.money.amountCents / 100.0).toString(),
                    currency = expense.money.currency,
                    category = expense.category ?: "",
                    notes = expense.notes ?: "",
                    createdAt = expense.createdAt,
                    updatedAt = kotlin.time.Clock.System.now()
                )
            }
        }
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

        val expenseId = current.existingExpenseId ?: Uuid.random().toString()
        val isEditMode = !current.existingExpenseId.isNullOrEmpty()

        val now: kotlin.time.Instant = kotlin.time.Clock.System.now()
        val expense = Expense(
            id = expenseId,
            title = current.title.trim(),
            money = Money(
                amountCents = amountCents,
                currency = current.currency.trim()
            ),
            category = current.category.ifBlank { null },
            notes = current.notes.ifBlank { null },
            createdAt = if(isEditMode) current.createdAt else now,
            updatedAt = now
        )

        _state.value = current.copy(isSaving = true, error = null)

        scope.launch {
            try {
                if(isEditMode) {
                    repo.updateExpense(expense)
                } else {
                    repo.addExpense(expense)
                }

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
