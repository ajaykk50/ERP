package com.erp.salespruchase.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onProductClick = { navController.navigate("product_management") },
                onCustomerClick = { navController.navigate("customer_management") },
                onSalesClick = { navController.navigate("sales_management") },
                onPurchaseClick = { navController.navigate("purchase_management") },
                onExpenseClick = { navController.navigate("expense_management") },
                onVendorClick = { navController.navigate("vendor_management") },
                onCategoryClick = { navController.navigate("category_management") }
            )
        }
        composable("product_management") { ProductManagementScreen() }
        composable("customer_management") { CustomerManagementScreen() }
        composable("sales_management") { SalesManagementScreen() }
        composable("vendor_management") { VendorManagementScreen() }
        composable("purchase_management") { PurchaseManagementScreen() }
        composable("expense_management") { ExpenseManagementScreen() }
        composable("category_management") { ExpenseManagementScreen() }


    }
}