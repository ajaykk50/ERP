package com.erp.salespruchase.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.erp.salespruchase.Customer
import com.erp.salespruchase.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val repository: CustomerRepository
) : ViewModel() {

    val customers = repository.getCustomers()

    var isDialogOpen by mutableStateOf(false)
        private set

    var selectedCustomer by mutableStateOf<Customer?>(null)
        private set

    fun openCustomerDialog(customer: Customer? = null) {
        selectedCustomer = customer
        isDialogOpen = true
    }

    fun closeCustomerDialog() {
        selectedCustomer = null
        isDialogOpen = false
    }

    fun saveCustomer(customer: Customer) {
        if (selectedCustomer == null) {
            repository.addCustomer(customer)
        } else {
            repository.updateCustomer(customer)
        }
    }

    fun deleteCustomer(customerId: String) {
        repository.deleteCustomer(customerId)
    }
}
