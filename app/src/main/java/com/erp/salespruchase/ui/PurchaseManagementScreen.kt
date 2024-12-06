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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.erp.salespruchase.Product
import com.erp.salespruchase.Vendor
import com.erp.salespruchase.viewmodel.PurchaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseManagementScreen(navController: NavController,
                             viewModel: PurchaseViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState(emptyList())
    val vendors by viewModel.vendors.collectAsState(emptyList())
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val selectedVendor by viewModel.selectedVendor.collectAsState()
    val context = LocalContext.current

    var quantity by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Purchase Management") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Product Dropdown
            DropdownMenu(
                label = "Select Product",
                options = products.map { it.name },
                selectedOption = selectedProduct?.name ?: "",
                onOptionSelected = { name ->
                    viewModel.selectProduct(products.find { it.name == name })
                }
            )

            // Vendor Dropdown
            DropdownMenu(
                label = "Select Vendor",
                options = vendors.map { it.name },
                selectedOption = selectedVendor?.name ?: "",
                onOptionSelected = { name ->
                    viewModel.selectVendor(vendors.find { it.name == name })
                }
            )

            // Quantity Input
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Enter Quantity") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Save Button
            Button(
                onClick = {
                    val product = selectedProduct
                    val vendor = selectedVendor
                    val qty = quantity.toIntOrNull()

                    if (product != null && vendor != null && qty != null && qty > 0) {
                        isSaving = true
                        viewModel.savePurchase(product, vendor, qty,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Purchase saved successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onError = {
                                Toast.makeText(context, "Error saving Purchase", Toast.LENGTH_SHORT)
                                    .show()
                            })
                    }
                },
                enabled = !isSaving && selectedProduct != null && selectedVendor != null && quantity.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isSaving) "Saving..." else "Save Purchase")
            }
        }
    }
}
