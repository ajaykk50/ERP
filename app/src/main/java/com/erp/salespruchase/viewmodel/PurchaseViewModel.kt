package com.erp.salespruchase.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erp.salespruchase.Customer
import com.erp.salespruchase.Product
import com.erp.salespruchase.Purchase
import com.erp.salespruchase.Vendor
import com.erp.salespruchase.repository.ProductRepository
import com.erp.salespruchase.repository.PurchaseRepository
import com.erp.salespruchase.repository.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val vendorRepository: VendorRepository,
    private val purchaseRepository: PurchaseRepository
) : ViewModel() {

    val vendors = vendorRepository.getVendors()
    val products = productRepository.getProducts()

    private val _selectedVendor = MutableStateFlow<Vendor?>(null)
    val selectedVendor: StateFlow<Vendor?> = _selectedVendor

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    fun selectVendor(vendor: Vendor?) {
        _selectedVendor.value = vendor
    }

    fun selectProduct(product: Product?) {
        _selectedProduct.value = product
    }


//    fun savePurchase(product: Product, vendor: Vendor, quantity: Int, onComplete: () -> Unit) {
//        viewModelScope.launch {
//            val updatedStock = product.stock + quantity
//            val purchase = Purchase(
//                id = "",
//                productId = product.id,
//                vendorId = vendor.id,
//                quantity = quantity,
//                date = System.currentTimeMillis()
//            )
//
//            productRepository.updateStock(product.id, updatedStock) { success ->
//                if (success) {
//                    purchaseRepository.addPurchase(purchase, onComplete)
//                } else {
//                    onComplete()
//                }
//            }
//        }
//    }

    fun savePurchase(
        product: Product,
        vendor: Vendor,
        quantity: Int,
        onSuccess: () -> Unit, onError: () -> Unit
    ) {
        viewModelScope.launch {
            val updatedStock = product.stock + quantity
            val purchase = Purchase(
                id = "",
                productId = product.id,
                vendorId = vendor.id,
                quantity = quantity,
                date = System.currentTimeMillis()
            )

            productRepository.updateStock(
                productId = product.id,
                newStock = updatedStock,
                onSuccess = {
                    purchaseRepository.addPurchase(purchase, onSuccess = {
                        productRepository.updateStock(product.id, updatedStock)
                        onSuccess()
                    }, onError)
                },
                onError = { exception ->
                    // Log the error or handle it in the UI
                    Log.e("PurchaseViewModel", "Failed to update stock: ${exception.message}")
                    onError()
                }
            )
        }
    }
}
