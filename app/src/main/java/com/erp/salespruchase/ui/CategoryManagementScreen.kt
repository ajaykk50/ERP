package com.erp.salespruchase.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.erp.salespruchase.Category
import com.erp.salespruchase.viewmodel.CategoryViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val categoryName by viewModel.categoryName.collectAsState("")
    val categories by viewModel.categories.collectAsState(emptyList())
    val editingCategory by viewModel.editingCategory.collectAsState(null)

    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.fetchExpenses()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Category Management") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Expense Name
                TextField(
                    value = categoryName,
                    onValueChange = { viewModel.setCategoryName(it) },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Save Expense Button
                Button(
                    onClick = {
                        if (categoryName.isNotEmpty()) {
                            if (editingCategory == null) {
                                viewModel.saveCategory(
                                    name = categoryName,
                                    onSuccess = {
                                        viewModel.fetchExpenses()
                                        Toast.makeText(
                                            context,
                                            "Category added successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = {
                                        Toast.makeText(
                                            context,
                                            "Error adding expense",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            } else {
                                viewModel.editCategory(

                                    category = editingCategory.copy(name = categoryName),
                                    onSuccess = {
                                        viewModel.fetchExpenses()
                                        Toast.makeText(
                                            context,
                                            "Category updated successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = {
                                        Toast.makeText(
                                            context,
                                            "Error updating category",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Category")
                }

                // Expense List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    items(categories) { category ->
                        CategoryRow(category,
                            onEdit = { selectedCategory ->
                                viewModel.setEditingCategory(selectedCategory)
                                // Open a dialog or navigate to an edit screen
//                                viewModel.editCategory(
//                                    categoryId = selectedCategory.id,
//                                    updatedCategory = selectedCategory.copy(name = selectedCategory.name),
//                                    onSuccess = {
//                                        Toast.makeText(context, "Category updated!", Toast.LENGTH_SHORT).show()
//                                    },
//                                    onError = {
//                                        Toast.makeText(context, "Failed to update category", Toast.LENGTH_SHORT).show()
//                                    }
//                                )
                            },
                            onDelete = { selectedCategory ->
                                viewModel.deleteCategory(
                                    categoryId = selectedCategory.id,
                                    onSuccess = {
                                        viewModel.fetchExpenses()
                                        Toast.makeText(
                                            context,
                                            "Category deleted!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = {
                                        Toast.makeText(
                                            context,
                                            "Failed to delete category",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            })
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryRow(
    category: Category, onEdit: (Category) -> Unit,
    onDelete: (Category) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Category Name: ${category.name}",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onEdit(category) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { onDelete(category) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
