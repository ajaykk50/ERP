package com.erp.salespruchase.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erp.salespruchase.SelectedProduct
import com.erp.salespruchase.viewmodel.SalesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesManagementScreen(
    viewModel: SalesViewModel = hiltViewModel()
) {
    val customers by viewModel.customers.collectAsState(emptyList())
    val products by viewModel.products.collectAsState(emptyList())
    val selectedCustomer by viewModel.selectedCustomer.collectAsState()
    val selectedProducts by viewModel.selectedProducts.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sales Management") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Select Customer
                DropdownMenu(
                    label = "Select Customer",
                    options = customers.map { it.name },
                    selectedOption = selectedCustomer?.name ?: "",
                    onOptionSelected = { name ->
                        viewModel.selectCustomer(customers.find { it.name == name })
                    }
                )

                // Searchable Dropdown for Products
                SearchableDropdown(
                    label = "Select Products",
                    options = products,
                    selectedOptions = selectedProducts,
                    onOptionSelected = { product ->
                        viewModel.addProduct(product)
                    },
                    onOptionRemoved = { product ->
                        viewModel.removeProduct(product)
                    }
                )

                // Display Selected Products with Quantities
                selectedProducts.forEach { (product, quantity) ->
                    ProductQuantityItem(
                        product = product,
                        quantity = quantity,
                        onQuantityChanged = { newQuantity ->
                            viewModel.updateProductQuantity(product, newQuantity)
                        }
                    )
                }

                // Total Amount
                Text(
                    text = "Total Amount: $totalAmount",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.fillMaxWidth()
                )

                // Save Button
                Button(
                    onClick = {
                        viewModel.saveSale(
                            onSuccess = {
                                Toast.makeText(context, "Sale saved successfully", Toast.LENGTH_SHORT).show()
                            },
                            onError = {
                                Toast.makeText(context, "Error saving sale", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Sale")
                }
            }
        }
    )
}

