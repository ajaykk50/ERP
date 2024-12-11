package com.erp.salespruchase.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.erp.salespruchase.DisplaySale
import com.erp.salespruchase.viewmodel.SalesViewModel
import com.erp.salespruchase.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllSalesScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    viewModel: SalesViewModel = hiltViewModel(),
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
                    sharedViewModel.setDisplaySale(null)
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
                        SaleItemCard(sale, {
                            sharedViewModel.setDisplaySale(it)
                            navController.navigate("sales_management")
                        }, {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "- ${item.productName}: ${item.quantity}",
                    style = MaterialTheme.typography.bodySmall
                )
                IconButton(onClick = {
                    //onProductEdit(item, quantityState, priceState)
                }) {
                    Icon(
                        Icons.Default.Edit, contentDescription = "Edit Product",
                        modifier = Modifier.size(14.dp)
                    )
                }

                IconButton(onClick = {
                    // onProductDelete(item)
                }) {
                    Icon(
                        Icons.Default.Delete, contentDescription = "Delete Product",
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

//        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
//            IconButton(onClick = { onEditClick(sale) }) {
//                Icon(Icons.Default.Edit, contentDescription = "Edit Sale",
//                    modifier = Modifier.size(14.dp))
//            }
//            IconButton(onClick = { onDeleteClick(sale) }) {
//                Icon(Icons.Default.Delete, contentDescription = "Delete Sale",
//                    modifier = Modifier.size(14.dp))
//            }
//     }
    }
}
