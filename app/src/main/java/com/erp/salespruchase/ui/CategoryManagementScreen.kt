package com.erp.salespruchase.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.erp.salespruchase.Category
import com.erp.salespruchase.viewmodel.CategoryViewModel
import com.erp.salespruchase.viewmodel.CustomerViewModel
import java.util.UUID

@Composable
fun CategoryManagementScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val categoryName by viewModel.categoryName.collectAsState()
    val editingCategory by viewModel.editingCategory.collectAsState()

    var isDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchCategory()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.clearEditingState()
                isDialogOpen = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(
                        category = category,
                        onEditClick = {
                            viewModel.setEditingCategory(category)
                            isDialogOpen = true
                        },
                        onDeleteClick = {
                            viewModel.deleteCategory(
                                categoryId = category.id,
                                onSuccess = { /* Success handling */ },
                                onError = { /* Error handling */ }
                            )
                        }
                    )
                }
            }
        }
    }

    if (isDialogOpen) {
        CategoryDialog(
            categoryName = categoryName,
            onCategoryNameChange = { viewModel.setCategoryName(it) },
            onSave = {
                if (editingCategory == null) {
                    viewModel.saveCategory(
                        id = UUID.randomUUID().toString(),
                        name = categoryName,
                        onSuccess = {
                            isDialogOpen = false
                        },
                        onError = { /* Error handling */ }
                    )
                } else {
                    editingCategory?.id?.let {
                        editingCategory?.copy(name = categoryName)?.let { it1 ->
                            viewModel.editCategory(
                                categoryId = it,
                                updatedCategory = it1,
                                onSuccess = {
                                    isDialogOpen = false
                                    viewModel.clearEditingState()
                                    viewModel.fetchCategory()
                                },
                                onError = { /* Error handling */ }
                            )
                        }
                    }
                }
            },
            onDismiss = { isDialogOpen = false }
        )
    }
}

@Composable
fun CategoryItem(category: Category, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = category.name, style = MaterialTheme.typography.bodyMedium)

            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Category")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Category")
                }
            }
        }
    }
}

@Composable
fun CategoryDialog(
    categoryName: String,
    onCategoryNameChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Category") },
        text = {
            Column {
                TextField(
                    value = categoryName,
                    onValueChange = onCategoryNameChange,
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onSave) {
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
