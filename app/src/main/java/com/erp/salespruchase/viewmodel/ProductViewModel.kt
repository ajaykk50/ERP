package com.erp.salespruchase.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.erp.salespruchase.Product
import com.erp.salespruchase.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    val products = repository.getProducts()

    var isDialogOpen by mutableStateOf(false)
        private set

    var selectedProduct by mutableStateOf<Product?>(null)
        private set

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
