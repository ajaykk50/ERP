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
    val category by viewModel.category.collectAsState(emptyList())
    val selectedCustomer by viewModel.selectedCustomer.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val quantity by viewModel.quantity.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val saleItems by viewModel.saleItems.collectAsState()
    val productSearchQuery by viewModel.productSearchQuery.collectAsState()
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

                DropdownMenu(
                    label = "Select Category",
                    options = category.map { it.name },
                    selectedOption = selectedCategory?.name ?: "",
                    onOptionSelected = { name ->
                        viewModel.selectCategory(category.find { it.name == name })
                    }
                )

                // Search Product
                TextField(
                    value = productSearchQuery,
                    onValueChange = { viewModel.updateProductSearchQuery(it) },
                    label = { Text("Search Product") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Product Dropdown based on search query
                val filteredProducts = products.filter { it.name.contains(productSearchQuery, ignoreCase = true) }
//                DropdownMenu(
//                    label = "Select Product",
//                    options = filteredProducts.map { it.name },
//                    selectedOption = selectedProduct?.name ?: "",
//                    onOptionSelected = { name ->
//                        viewModel.selectProduct(filteredProducts.find { it.name == name })
//                    }
//                )

                SearchableDropdownMenu(
                    label = "Select Product",
                    options = products.map { it.name },
                    selectedOption = selectedProduct?.name ?: "",
                    onOptionSelected = { name ->
                        viewModel.selectProduct(products.find { it.name == name })
                    }
                )

                // Enter Quantity
                TextField(
                    value = quantity.toString(),
                    onValueChange = { viewModel.updateQuantity(it.toIntOrNull() ?: 0) },
                    label = { Text("Enter Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Add Product to Sale
                Button(
                    onClick = {
                        selectedProduct?.let { product ->
                            viewModel.addProductToSale(product, quantity)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Product to Sale")
                }

                // Sale Items Summary
                Column(modifier = Modifier.fillMaxWidth()) {
                    saleItems.forEach { saleItem ->
                        Text("${saleItem.product.name}: ${saleItem.quantity} x ${saleItem.product.price}")
                    }
                }

                // Display Total Price
                Text(
                    text = "Total: $totalPrice",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.fillMaxWidth()
                )

                // Save Sale Button
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
