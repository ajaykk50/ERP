package com.erp.salespruchase.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.erp.salespruchase.Product

@Composable
fun SearchableDropdown(
    label: String,
    options: List<Product>,
    selectedOptions: Map<Product, Int>,
    onOptionSelected: (Product) -> Unit,
    onOptionRemoved: (Product) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Column {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            }
        )


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.filter { it.name.contains(searchText, ignoreCase = true) }
                .forEach { option ->
                    DropdownMenuItem(onClick = {
                        if (selectedOptions.containsKey(option)) {
                            onOptionRemoved(option)
                        } else {
                            onOptionSelected(option)
                        }
                        expanded = false
                    }) {
                        Text(option.name)
                    }
                }
        }
    }
}
