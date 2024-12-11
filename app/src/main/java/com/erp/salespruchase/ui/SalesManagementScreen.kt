package com.erp.salespruchase.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavController
import com.erp.salespruchase.DisplaySale
import com.erp.salespruchase.viewmodel.SalesViewModel
import com.erp.salespruchase.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesManagementScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    viewModel: SalesViewModel = hiltViewModel(),
) {
    val customers by viewModel.customers.collectAsState(emptyList())
    val products by viewModel.products.collectAsState(emptyList())
    val category by viewModel.category.collectAsState(emptyList())
    val selectedCustomer by viewModel.selectedCustomer.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val quantity by viewModel.quantity.collectAsState()
    val price by viewModel.price.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val saleItems by viewModel.saleItems.collectAsState()
    val productSearchQuery by viewModel.productSearchQuery.collectAsState()
    val context = LocalContext.current

    println("sharedViewModel.displaySale" + sharedViewModel.displaySale)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sales Management") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                    value = quantity,
                    onValueChange = { viewModel.updateQuantity(it) },
                    label = { Text("Enter Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Enter Price
                TextField(
                    value = price,
                    onValueChange = { viewModel.updatePrice(it) },
                    label = { Text("Enter Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Add Product to Sale
                Button(
                    onClick = {
                        selectedProduct?.let { product ->
                            viewModel.addProductToSale(product, quantity, price = price)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Product to Sale")
                }

                // Sale Items Summary
                Column(modifier = Modifier.fillMaxWidth()) {
                    saleItems.forEach { saleItem ->
                        Text("${saleItem.product?.name}: ${saleItem.quantity} x ${price}")
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
                                Toast.makeText(
                                    context,
                                    "Sale saved successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onError = {
                                Toast.makeText(context, "Error saving sale", Toast.LENGTH_SHORT)
                                    .show()
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
