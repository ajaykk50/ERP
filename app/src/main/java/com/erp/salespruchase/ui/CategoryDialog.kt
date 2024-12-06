package com.erp.salespruchase.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.erp.salespruchase.Category
import java.util.UUID

@Composable
fun CategoryDialog(
    initialCategory: Category? = null,
    onDismiss: () -> Unit,
    onSave: (Category) -> Unit
) {
    // State variables for category fields
    val name = remember { mutableStateOf(initialCategory?.name ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialCategory == null) "Add Category" else "Edit Category") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Input field for category name
                item {
                    TextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text("Category Name") }
                    )
                }

            }
        },
        confirmButton = {
            Button(onClick = {
                val category = Category(
                    id = initialCategory?.id ?: UUID.randomUUID().toString(),
                    name = name.value,
                )
                onSave(category)
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
