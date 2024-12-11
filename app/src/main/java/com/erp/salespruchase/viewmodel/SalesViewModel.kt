package com.erp.salespruchase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erp.salespruchase.Category
import com.erp.salespruchase.Customer
import com.erp.salespruchase.DisplaySale
import com.erp.salespruchase.DisplaySaleItem
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

    private val _quantity = MutableStateFlow("0")
    val quantity: StateFlow<String> = _quantity

    private val _price = MutableStateFlow("0")
    val price: StateFlow<String> = _price

    private val _productSearchQuery = MutableStateFlow("")
    val productSearchQuery: StateFlow<String> = _productSearchQuery

    private val _saleItems = MutableStateFlow<List<SaleItem>>(emptyList())
    val saleItems: StateFlow<List<SaleItem>> = _saleItems


    private val _allSales = MutableStateFlow<List<DisplaySale>>(emptyList())
    val allSales: StateFlow<List<DisplaySale>> = _allSales

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val filteredSales: StateFlow<List<DisplaySale>> = searchQuery
        .combine(_allSales) { query, sales ->
            if (query.isBlank()) sales
            else sales.filter { sale ->
                sale.customerName.contains(query, ignoreCase = true) ||
                        sale.saleItems.any { it.productName.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun fetchAllSales() {
        viewModelScope.launch {
            salesRepository.getAllSales().collect { sales ->
                val displaySales = sales.map { sale ->
                    val customer = customerRepository.getCustomerById(sale.customerId).firstOrNull()
                    DisplaySale(
                        id = sale.id,
                        customerName = customer?.name ?: "Unknown",
                        saleItems = sale.saleItems.map { saleItem ->
                            val product =
                                saleItem.product?.id?.let {
                                    productRepository.getProductById(it).firstOrNull()
                                }
                            DisplaySaleItem(
                                productName = product?.name ?: "Unknown",
                                quantity = saleItem.quantity,
                                price = saleItem.price
                            )
                        },
                        totalAmount = sale.totalAmount,
                        date = sale.date
                    )
                }
                _allSales.value = displaySales
            }
        }
    }


    val totalPrice = _saleItems.combine(_selectedProduct) { saleItems, selectedProduct ->
        if (_price.value.toDouble() > 0.0) {
            saleItems.sumOf { _price.value.toDouble() * it.quantity.toDouble() }
        } else {
            saleItems.sumOf { it.product?.price?.toDouble()!! * it.quantity.toDouble() }
        }
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

    fun updateQuantity(newQuantity: String) {
        _quantity.value = newQuantity
    }

    fun updatePrice(price: String) {
        _price.value = price
    }

    fun updateProductSearchQuery(query: String) {
        _productSearchQuery.value = query
    }

    fun addProductToSale(product: Product, quantity: String, price: String) {
        val newSaleItem = SaleItem(product, quantity, price)
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
                saleItems = saleItems,
                totalAmount = saleItems.sumOf { it.product?.price?.toDouble()!! * it.quantity.toDouble() }
                    .toString(),
                date = System.currentTimeMillis()
            )

            // Save sale
            salesRepository.addSales(sale, onSuccess = {
                // Update product stock after sale
                saleItems.forEach { saleItem ->
                    saleItem.product?.id?.let {
                        productRepository.updateStock(
                            it,
                            (saleItem.product.stock.toDouble() - saleItem.quantity.toDouble()).toString()
                        )
                    }
                }
                onSuccess()
            }, onError)
        } else {
            onError()
        }
    }

    fun deleteSale(saleId: String) {
        viewModelScope.launch {
            salesRepository.deleteSale(saleId) {
                if (it) {
                    fetchAllSales() // Refresh the list
                }
            }
        }
    }
}
