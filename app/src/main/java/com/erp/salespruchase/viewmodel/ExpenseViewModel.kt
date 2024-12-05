package com.erp.salespruchase.viewmodel

import androidx.lifecycle.ViewModel
import com.erp.salespruchase.Expense
import com.erp.salespruchase.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    // State flows for expense data
    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _expenseName = MutableStateFlow("")
    val expenseName: StateFlow<String> = _expenseName

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _date = MutableStateFlow(0L)
    val date: StateFlow<Long> = _date

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    // Setters for expense fields
    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setExpenseName(name: String) {
        _expenseName.value = name
    }

    fun setAmount(amount: String) {
        _amount.value = amount
    }

    fun setDate(date: Long) {
        _date.value = date
    }

    // Save expense to the repository
    fun saveExpense(
        name: String,
        amount: Double,
        date: Long,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val expense = Expense(
            name = name,
            amount = amount,
            date = date
        )
        expenseRepository.addExpense(expense, onSuccess, onError)
    }

    // Fetch expenses (usually this would be called in onStart/onResume)
    fun fetchExpenses() {
        expenseRepository.getExpenses { expenses ->
            _expenses.value = expenses
        }
    }
}
