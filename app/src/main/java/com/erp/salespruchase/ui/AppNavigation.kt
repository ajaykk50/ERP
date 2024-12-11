package com.erp.salespruchase.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.erp.salespruchase.DisplaySale
import com.erp.salespruchase.viewmodel.SharedViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onProductClick = { navController.navigate("product_management") },
                onCustomerClick = { navController.navigate("customer_management") },
                onSalesClick = { navController.navigate("sales_management") },
                onPurchaseClick = { navController.navigate("purchase_management") },
                onExpenseClick = { navController.navigate("expense_management") },
                onVendorClick = { navController.navigate("vendor_management") },
                onCategoryClick = { navController.navigate("category_management") },
                onSlesListClick = { navController.navigate("sales_list") }
            )
        }
        composable("product_management") { ProductManagementScreen(navController) }
        composable("customer_management") { CustomerManagementScreen(navController) }
        composable("sales_management") { SalesManagementScreen(navController,sharedViewModel) }
        composable("vendor_management") { VendorManagementScreen(navController) }
        composable("purchase_management") { PurchaseManagementScreen(navController) }
        composable("expense_management") { ExpenseManagementScreen(navController) }
        composable("category_management") { CategoryManagementScreen(navController) }
        composable("sales_list") { ViewAllSalesScreen(navController,sharedViewModel) }


    }
}