package com.francoherrero.expensetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.francoherrero.expensetracker.domain.model.Expense
import com.francoherrero.expensetracker.presentation.state.ExpenseDetailState
import com.francoherrero.expensetracker.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ExpenseDetailViewModel(private val expenseRepository: ExpenseRepository) : ViewModel() {

    private val _state = MutableStateFlow(ExpenseDetailState())
    val state: StateFlow<ExpenseDetailState> = _state

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun getExpense(expenseId: String) {
        scope.launch {
            expenseRepository.getExpense(expenseId)
                .onStart {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                }
                .onEmpty {
                    _state.value = _state.value.copy(isLoading = false, error = "Expense not found")
                }
                .firstOrNull {
                    _state.value = _state.value.copy(isLoading = false, error = null, expense = it)
                    true
                }
        }
    }
}