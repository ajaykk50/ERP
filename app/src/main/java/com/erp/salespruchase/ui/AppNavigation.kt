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
        composable("product_management") { ProductManagementScreen(navController) }
        composable("customer_management") { CustomerManagementScreen(navController) }
        composable("sales_management") { SalesManagementScreen(navController) }
        composable("vendor_management") { VendorManagementScreen(navController) }
        composable("purchase_management") { PurchaseManagementScreen(navController) }
        composable("expense_management") { ExpenseManagementScreen(navController) }
        composable("category_management") { CategoryManagementScreen(navController) }


    }
}