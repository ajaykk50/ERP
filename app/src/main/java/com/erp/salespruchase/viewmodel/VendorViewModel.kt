package com.erp.salespruchase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erp.salespruchase.Vendor
import com.erp.salespruchase.repository.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorViewModel @Inject constructor(
    private val vendorRepository: VendorRepository
) : ViewModel() {

    private val _vendors = MutableStateFlow<List<Vendor>>(emptyList())
    val vendors: StateFlow<List<Vendor>> = _vendors

    fun loadVendors() {
        viewModelScope.launch {
            vendorRepository.getVendors().collect {
                _vendors.value = it
            }
        }
    }

    fun addVendor(vendor: Vendor, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        vendorRepository.addVendor(vendor, onSuccess, onError)
    }

    fun updateVendor(vendor: Vendor, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        vendorRepository.updateVendor(vendor, onSuccess, onError)
    }

    fun deleteVendor(vendorId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        vendorRepository.deleteVendor(vendorId, onSuccess, onError)
    }
}
