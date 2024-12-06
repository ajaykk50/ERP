package com.erp.salespruchase.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.erp.salespruchase.Category
import com.erp.salespruchase.Product
import com.erp.salespruchase.repository.CategoryRepository
import com.erp.salespruchase.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val products = repository.getProducts()
    val category = categoryRepository.getCategory()

    var isDialogOpen by mutableStateOf(false)
        private set

    var selectedProduct by mutableStateOf<Product?>(null)
        private set

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    fun selectCategory(category: Category?) {
        _selectedCategory.value = category
    }

    fun openProductDialog(product: Product? = null) {
        selectedProduct = product
        isDialogOpen = true
    }

    fun closeProductDialog() {
        selectedProduct = null
        isDialogOpen = false
    }

    fun saveProduct(product: Product) {
        if (selectedProduct == null) {
            repository.addProduct(product)
        } else {
            repository.updateProduct(product)
        }
    }

    fun deleteProduct(productId: String) {
        repository.deleteProduct(productId)
    }
}
