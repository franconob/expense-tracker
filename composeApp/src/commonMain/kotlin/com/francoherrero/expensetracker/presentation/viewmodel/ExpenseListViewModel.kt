package com.francoherrero.expensetracker.presentation.viewmodel

import com.francoherrero.expensetracker.presentation.state.ExpenseListState
import com.francoherrero.expensetracker.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ExpenseListViewModel(private val expenseRepository: ExpenseRepository) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(ExpenseListState())
    val state: StateFlow<ExpenseListState> = _state

    init {
        observeExpenses()
    }

    private fun observeExpenses() {
        scope.launch {
            expenseRepository.observeExpense().onStart {
                _state.value = _state.value.copy(isLoading = true, error = null)
            }
                .catch {
                    _state.value = _state.value.copy(isLoading = false, error = it.message)
                }
                .onEach { expenses ->
                    _state.value =
                        _state.value.copy(isLoading = false, error = null, items = expenses)
                }
                .collect {}
        }
    }
}