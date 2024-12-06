package com.erp.salespruchase.viewmodel

import androidx.lifecycle.ViewModel
import com.erp.salespruchase.Category
import com.erp.salespruchase.Expense
import com.erp.salespruchase.repository.CategoryRepository
import com.erp.salespruchase.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    // State flows for expense data
    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _categoryName = MutableStateFlow("")
    val categoryName: StateFlow<String> = _categoryName

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories



    fun setCategoryName(name: String) {
        _categoryName.value = name
    }


    // Save expense to the repository
    fun saveCategory(
        name: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val category = Category(
            name = name
        )
        categoryRepository.addCategory(category, onSuccess, onError)
    }

    // Fetch expenses (usually this would be called in onStart/onResume)
    fun fetchExpenses() {
//        categoryRepository.getCategory { category ->
//            _categories.value = category
//        }
    }
}
