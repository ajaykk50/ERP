package com.erp.salespruchase.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.erp.salespruchase.Product
import java.util.UUID

@Composable
fun ProductDialog(
    initialProduct: Product?,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    val name = remember { mutableStateOf(initialProduct?.name ?: "") }
    val price = remember { mutableStateOf(initialProduct?.price ?: 0.0) }
    val stock = remember { mutableStateOf(initialProduct?.stock ?: 0) }
    val unit = remember { mutableStateOf(initialProduct?.unit ?: "") }
    val category = remember { mutableStateOf(initialProduct?.category ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialProduct == null) "Add Product" else "Edit Product") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth() // Optional: Adjust as needed
            ) {
                item {
                    TextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text("Name") }
                    )
                }
                item {
                    TextField(
                        value = price.value.toString(),
                        onValueChange = { price.value = it.toDoubleOrNull() ?: 0.0 },
                        label = { Text("Price") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
                item {
                    TextField(
                        value = stock.value.toString(),
                        onValueChange = { stock.value = it.toIntOrNull() ?: 0 },
                        label = { Text("Stock") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
                item {
                    TextField(
                        value = unit.value,
                        onValueChange = { unit.value = it },
                        label = { Text("Unit") }
                    )
                }
                item {
                    TextField(
                        value = category.value,
                        onValueChange = { category.value = it },
                        label = { Text("Category") }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val product = Product(
                    id = initialProduct?.id ?: UUID.randomUUID().toString(),
                    name = name.value,
                    price = price.value,
                    stock = stock.value,
                    unit = unit.value,
                    category = category.value
                )
                onSave(product)
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
