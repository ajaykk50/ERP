package com.erp.salespruchase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erp.salespruchase.Customer
import com.erp.salespruchase.Product
import com.erp.salespruchase.Sale
import com.erp.salespruchase.SelectedProduct
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
    private val salesRepository: SalesRepository
) : ViewModel() {

    val customers = customerRepository.getCustomers()
    val products = productRepository.getProducts()

    private val _selectedCustomer = MutableStateFlow<Customer?>(null)
    val selectedCustomer: StateFlow<Customer?> = _selectedCustomer

    private val _selectedProducts = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val selectedProducts: StateFlow<Map<Product, Int>> = _selectedProducts

    val totalAmount = _selectedProducts.map { products ->
        products.entries.sumOf { (product, quantity) -> product.price * quantity }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    fun selectCustomer(customer: Customer?) {
        _selectedCustomer.value = customer
    }

    fun addProduct(product: Product) {
        _selectedProducts.value = _selectedProducts.value.toMutableMap().apply {
            put(product, 1)
        }
    }

    fun removeProduct(product: Product) {
        _selectedProducts.value = _selectedProducts.value.toMutableMap().apply {
            remove(product)
        }
    }

    fun updateProductQuantity(product: Product, quantity: Int) {
        _selectedProducts.value = _selectedProducts.value.toMutableMap().apply {
            this[product] = quantity
        }
    }

    fun saveSale(onSuccess: () -> Unit, onError: () -> Unit) {
        val customer = _selectedCustomer.value
        val products = _selectedProducts.value

        if (customer != null && products.isNotEmpty()) {
            val salesList = products.map { (product, quantity) ->
                Sale(
                    id = UUID.randomUUID().toString(),
                    customerId = customer.id,
                    productId = product.id,
                    quantity = quantity,
                    totalAmount = product.price * quantity,
                    date = System.currentTimeMillis()
                )
            }

            salesRepository.addSales(salesList, onSuccess = {
                salesList.forEach { sale ->
                    productRepository.updateStock(sale.productId, -sale.quantity)
                }
                onSuccess()
            }, onError)
        } else {
            onError()
        }
    }
}

