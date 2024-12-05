package com.erp.salespruchase.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.erp.salespruchase.Customer
import java.util.UUID

@Composable
fun CustomerDialog(
    initialCustomer: Customer?,
    onDismiss: () -> Unit,
    onSave: (Customer) -> Unit
) {
    val name = remember { mutableStateOf(initialCustomer?.name ?: "") }
    val phone = remember { mutableStateOf(initialCustomer?.phone ?: "") }
    val address = remember { mutableStateOf(initialCustomer?.address ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialCustomer == null) "Add Customer" else "Edit Customer") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(value = name.value, onValueChange = { name.value = it }, label = { Text("Name") })
                TextField(value = phone.value, onValueChange = { phone.value = it }, label = { Text("Phone") })
                TextField(value = address.value, onValueChange = { address.value = it }, label = { Text("Address") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val customer = Customer(
                    id = initialCustomer?.id ?: UUID.randomUUID().toString(),
                    name = name.value,
                    phone = phone.value,
                    address = address.value
                )
                onSave(customer)
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
