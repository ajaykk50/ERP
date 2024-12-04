package com.erp.salespruchase.ui

import androidx.compose.runtime.Composable
import com.erp.salespruchase.viewmodel.ProductViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun ProductManagementScreen(
    viewModel:ProductViewModel = hiltViewModel()
) {
}
