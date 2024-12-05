package com.erp.salespruchase.ui

import androidx.compose.foundation.layout.Box
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

@Composable
fun DropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            }
        )
        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                },
                    text = {
                        Text(option)
                    })
            }
        }
    }
}
