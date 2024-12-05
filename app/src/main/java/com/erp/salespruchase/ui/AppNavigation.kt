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
                onExpenseClick = { navController.navigate("expense_management") }
            )
        }
        composable("product_management") { ProductManagementScreen() }
        composable("customer_management") { CustomerManagementScreen() }
    }
}