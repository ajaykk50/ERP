package com.erp.salespruchase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erp.salespruchase.Category
import com.erp.salespruchase.Customer
import com.erp.salespruchase.Product
import com.erp.salespruchase.Sale
import com.erp.salespruchase.SaleItem
import com.erp.salespruchase.SelectedProduct
import com.erp.salespruchase.repository.CategoryRepository
import com.erp.salespruchase.repository.CustomerRepository
import com.erp.salespruchase.repository.ProductRepository
import com.erp.salespruchase.repository.SalesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository,
    private val salesRepository: SalesRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val customers = customerRepository.getCustomers()
    val products = productRepository.getProducts()
    val category = categoryRepository.getCategory()

    private val _selectedCustomer = MutableStateFlow<Customer?>(null)
    val selectedCustomer: StateFlow<Customer?> = _selectedCustomer

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    private val _quantity = MutableStateFlow(0)
    val quantity: StateFlow<Int> = _quantity

    private val _productSearchQuery = MutableStateFlow("")
    val productSearchQuery: StateFlow<String> = _productSearchQuery

    private val _saleItems = MutableStateFlow<List<SaleItem>>(emptyList())
    val saleItems: StateFlow<List<SaleItem>> = _saleItems

    val totalPrice = _saleItems.combine(_selectedProduct) { saleItems, selectedProduct ->
        saleItems.sumOf { it.product.price * it.quantity } + (selectedProduct?.price ?: 0.0 * _quantity.value)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    fun selectCustomer(customer: Customer?) {
        _selectedCustomer.value = customer
    }

    fun selectCategory(category: Category?) {
        _selectedCategory.value = category
    }


    fun selectProduct(product: Product?) {
        _selectedProduct.value = product
    }

    fun updateQuantity(newQuantity: Int) {
        _quantity.value = newQuantity
    }

    fun updateProductSearchQuery(query: String) {
        _productSearchQuery.value = query
    }

    fun addProductToSale(product: Product, quantity: Int) {
        val newSaleItem = SaleItem(product, quantity)
        _saleItems.value = _saleItems.value + newSaleItem
    }

    fun saveSale(onSuccess: () -> Unit, onError: () -> Unit) {
        val customer = _selectedCustomer.value
        val saleItems = _saleItems.value
        if (customer != null && saleItems.isNotEmpty()) {
            val saleId = UUID.randomUUID().toString()
            val sale = Sale(
                id = saleId,
                customerId = customer.id,
                saleItems = saleItems.map { it.product.id to it.quantity },
                totalAmount = saleItems.sumOf { it.product.price * it.quantity },
                date = System.currentTimeMillis()
            )

            // Save sale
            salesRepository.addSales(sale, onSuccess = {
                // Update product stock after sale
                saleItems.forEach { saleItem ->
                    productRepository.updateStock(saleItem.product.id, saleItem.product.stock - saleItem.quantity)
                }
                onSuccess()
            }, onError)
        } else {
            onError()
        }
    }
}
