package com.erp.salespruchase.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onProductClick: () -> Unit,
    onCustomerClick: () -> Unit,
    onSalesClick: () -> Unit,
    onPurchaseClick: () -> Unit,
    onExpenseClick: () -> Unit,
    onVendorClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onSlesListClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Management Dashboard") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ManagementCard(
                    title = "Product Management",
                    description = "Add, edit, and view products",
                    icon = Icons.Default.ShoppingCart,
                    onClick = onProductClick
                )

            }
            item {
                ManagementCard(
                    title = "Customer Management",
                    description = "Add, edit, and view customers",
                    icon = Icons.Default.Person,
                    onClick = onCustomerClick
                )
            }
            item {


                ManagementCard(
                    title = "Vendor Management",
                    description = "Add, edit, and view customers",
                    icon = Icons.Default.Person,
                    onClick = onVendorClick
                )
            }

            item {
                ManagementCard(
                    title = "Category Management",
                    description = "Add, edit, and view category",
                    icon = Icons.Default.Person,
                    onClick = onCategoryClick
                )
            }


//            item {
//                ManagementCard(
//                    title = "Sales Management",
//                    description = "Create sales",
//                    icon = Icons.Default.Person,
//                    onClick = onSalesClick
//                )
//            }

            item {
                ManagementCard(
                    title = "Sales Management",
                    description = "Add and view sales",
                    icon = Icons.Default.Person,
                    onClick = onSlesListClick
                )
            }

            item {
                ManagementCard(
                    title = "Purchase Management",
                    description = "Track product purchases",
                    icon = Icons.Default.Person,
                    onClick = onPurchaseClick
                )
            }

            item {
                ManagementCard(
                    title = "Expense Management",
                    description = "Add and view expenses",
                    icon = Icons.Default.Person,
                    onClick = onExpenseClick
                )
            }
        }
    }
}
