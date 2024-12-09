package com.erp.salespruchase.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.erp.salespruchase.DisplaySale
import com.erp.salespruchase.viewmodel.SalesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllSalesScreen(
    navController: NavController,
    viewModel: SalesViewModel = hiltViewModel()
) {
    val allSales by viewModel.allSales.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredSales by viewModel.filteredSales.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllSales()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View All Sales") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("sales_management")
                },
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::updateSearchQuery,
                    label = { Text("Search Sales") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(filteredSales) { sale ->
                        SaleItemCard(sale, {}, {
                            viewModel.deleteSale(it.id)
                        })
                    }
                }
            }
        }
    )
}

@Composable
fun SaleItemCard(
    sale: DisplaySale,
    onEditClick: (DisplaySale) -> Unit,
    onDeleteClick: (DisplaySale) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(8.dp)
    ) {
        Text("Customer: ${sale.customerName}", style = MaterialTheme.typography.bodyMedium)
        Text(
            "Date: ${java.text.SimpleDateFormat("dd/MM/yyyy").format(sale.date)}",
            style = MaterialTheme.typography.bodySmall
        )
        Text("Total Amount: ${sale.totalAmount}", style = MaterialTheme.typography.bodyMedium)
        Text("Items:", style = MaterialTheme.typography.bodyMedium)
        sale.saleItems.forEach { item ->
            Text(
                "- ${item.productName}: ${item.quantity}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(horizontalArrangement = Arrangement.End) {
            IconButton(onClick = { onEditClick(sale) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Sale")
            }
            IconButton(onClick = { onDeleteClick(sale) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Sale")
            }
        }
    }
}
