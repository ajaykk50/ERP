package com.erp.salespruchase.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erp.salespruchase.Category
import com.erp.salespruchase.Expense
import com.erp.salespruchase.repository.CategoryRepository
import com.erp.salespruchase.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    private val _editingCategory = MutableStateFlow<Category?>(null)
    val editingCategory: StateFlow<Category?> = _editingCategory

    private val _editingCategoryId = MutableStateFlow<String?>(null)
    val editingCategoryId: StateFlow<String?> = _editingCategoryId

    fun setEditingCategoryId(categoryId: String) {
        _editingCategoryId.value = categoryId
    }

    fun setCategoryName(name: String) {
        _categoryName.value = name
    }

    fun setEditingCategory(category: Category) {
        _editingCategory.value = category
        _categoryName.value = category.name
    }


    // Save expense to the repository
    fun saveCategory(
        id: String,
        name: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val category = Category(
            id = id,
            name = name
        )
        categoryRepository.addCategory(category, onSuccess, onError)
    }

    fun fetchCategory() {
        viewModelScope.launch {
            categoryRepository.getCategory().collectLatest {
                _categories.value = it
            }
        }
    }

    fun clearEditingState() {
        _editingCategoryId.value = null
        _categoryName.value = ""
    }

    fun editCategory(
        categoryId: String,
        updatedCategory: Category,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        categoryRepository.editCategory(categoryId, updatedCategory, onSuccess, onError)
    }

    fun deleteCategory(
        categoryId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        categoryRepository.deleteCategory(categoryId, onSuccess, onError)
    }
}
