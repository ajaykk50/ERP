package com.erp.salespruchase.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erp.salespruchase.Vendor
import com.erp.salespruchase.viewmodel.VendorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorManagementScreen(viewModel: VendorViewModel = hiltViewModel()) {
    val vendors by viewModel.vendors.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedVendor by remember { mutableStateOf<Vendor?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadVendors()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Vendor Management") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedVendor = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Vendor")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            LazyColumn {
                items(vendors) { vendor ->
                    VendorCard(
                        vendor = vendor,
                        onEdit = {
                            selectedVendor = vendor
                            showDialog = true
                        },
                        onDelete = { viewModel.deleteVendor(vendor.id, {}, {}) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        VendorDialog(
            vendor = selectedVendor,
            onDismiss = { showDialog = false },
            onSave = { vendor ->
                if (vendor.id.isEmpty()) {
                    viewModel.addVendor(vendor, { showDialog = false }, {})
                } else {
                    viewModel.updateVendor(vendor, { showDialog = false }, {})
                }
            }
        )
    }
}

@Composable
fun VendorCard(vendor: Vendor, onEdit: (Vendor) -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onEdit(vendor) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = vendor.name, style = MaterialTheme.typography.titleSmall)
                Text(text = vendor.phone, style = MaterialTheme.typography.bodySmall)
                Text(text = vendor.address, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Vendor")
            }
        }
    }
}

@Composable
fun VendorDialog(
    vendor: Vendor?,
    onDismiss: () -> Unit,
    onSave: (Vendor) -> Unit
) {
    var name by remember { mutableStateOf(vendor?.name.orEmpty()) }
    var phone by remember { mutableStateOf(vendor?.phone.orEmpty()) }
    var address by remember { mutableStateOf(vendor?.address.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (vendor == null) "Add Vendor" else "Edit Vendor") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    Vendor(
                        id = vendor?.id.orEmpty(),
                        name = name,
                        phone = phone,
                        address = address
                    )
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
