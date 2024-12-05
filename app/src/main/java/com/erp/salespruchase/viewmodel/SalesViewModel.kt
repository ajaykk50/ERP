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

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    private val _selectedProducts = MutableStateFlow<List<SelectedProduct>>(emptyList())
    val selectedProducts: StateFlow<List<SelectedProduct>> = _selectedProducts

    val totalPrice = selectedProducts.map { products ->
        products.sumOf { it.quantity * it.price }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    private val _quantity = MutableStateFlow(0)
    val quantity: StateFlow<Int> = _quantity

//    val totalPrice = selectedProduct.combine(quantity) { product, quantity ->
//        product?.price?.times(quantity) ?: 0.0
//    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    fun selectCustomer(customer: Customer?) {
        _selectedCustomer.value = customer
    }

    fun selectProduct(product: Product?) {
        _selectedProduct.value = product
    }

    fun updateQuantity(newQuantity: Int) {
        _quantity.value = newQuantity
    }

    fun updatePrice(newPrice: Double) {
        _selectedProduct.value = _selectedProduct.value?.copy(price = newPrice)
    }

     fun addProduct() {
         viewModelScope.launch {
             val firstProduct = products.firstOrNull()
             _selectedProducts.value += SelectedProduct(
                 id = firstProduct?.firstOrNull()?.id!!,
                 name = firstProduct.firstOrNull()?.name!!,
                 quantity = 0,
                 price = firstProduct.firstOrNull()?.price!!
             )
         }
    }

    fun addProduct(product: Product) {
        val selectedProduct = SelectedProduct(
            id = product.id,
            name = product.name,
            quantity = 0,
            price = product.price
        )
        _selectedProducts.value = _selectedProducts.value + selectedProduct
    }

    fun updateQuantityForProduct(productId: String, quantity: Int) {
        _selectedProducts.value = _selectedProducts.value.map {
            if (it.id == productId) it.copy(quantity = quantity) else it
        }
    }

    fun updatePriceForProduct(productId: String, price: Double) {
        _selectedProducts.value = _selectedProducts.value.map {
            if (it.id == productId) it.copy(price = price) else it
        }
    }

    fun removeProduct(productId: String) {
        _selectedProducts.value = _selectedProducts.value.filter { it.id != productId }
    }

    fun saveSale(onSuccess: () -> Unit, onError: () -> Unit) {
        val customer = _selectedCustomer.value
        val product = _selectedProduct.value
        val quantity = _quantity.value

        if (customer != null && product != null && quantity > 0) {
            val sale = Sale(
                id = UUID.randomUUID().toString(),
                customerId = customer.id,
                productId = product.id,
                quantity = quantity,
                totalAmount = product.price * quantity,
                date = System.currentTimeMillis()
            )

            salesRepository.addSale(sale, onSuccess = {
                productRepository.updateStock(product.id, product.stock - quantity)
                onSuccess()
            }, onError)
        } else {
            onError()
        }
    }
}
