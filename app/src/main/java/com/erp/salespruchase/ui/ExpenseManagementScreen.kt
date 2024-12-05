package com.erp.salespruchase.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.erp.salespruchase.Expense
import com.erp.salespruchase.viewmodel.ExpenseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseManagementScreen(
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val expenseCategories = listOf("Food", "Transport", "Utilities", "Entertainment", "Other")
    val selectedCategory by viewModel.selectedCategory.collectAsState("")
    val expenseName by viewModel.expenseName.collectAsState("")
    val amount by viewModel.amount.collectAsState("")
    val date by viewModel.date.collectAsState(0L)
    val expenses by viewModel.expenses.collectAsState(emptyList())
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.fetchExpenses()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Management") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
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
                    value = expenseName,
                    onValueChange = { viewModel.setExpenseName(it) },
                    label = { Text("Expense Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Amount
                TextField(
                    value = amount,
                    onValueChange = { viewModel.setAmount(it) },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Date Picker
                DatePickerField(
                    selectedDate = date,
                    onDateSelected = { selectedDate -> viewModel.setDate(selectedDate) }
                )

                // Save Expense Button
                Button(
                    onClick = {
                        if (expenseName.isNotEmpty() && amount.isNotEmpty()) {
                            viewModel.saveExpense(
                                name = expenseName,
                                amount = amount.toDouble(),
                                date = date,
                                onSuccess = {
                                    viewModel.fetchExpenses()
                                    Toast.makeText(
                                        context,
                                        "Expense added successfully!",
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
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Expense")
                }

                // Expense List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    items(expenses) { expense ->
                        println("print expense")
                        ExpenseRow(expense)
                    }
                }
            }
        }
    )
}

@Composable
fun ExpenseRow(expense: Expense) {
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
                text = "Expense Name: ${expense.name}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Amount: ${expense.amount}", style = MaterialTheme.typography.titleSmall)
            Text(
                text = "Date: ${SimpleDateFormat("yyyy-MM-dd").format(Date(expense.date))}",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun DatePickerField(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    val datePickerDialog = remember { MutableStateFlow<Dialog?>(null) }

    TextField(
        value = SimpleDateFormat("yyyy-MM-dd").format(Date(selectedDate)),
        onValueChange = {},
        label = { Text("Select Date") },
        modifier = Modifier.fillMaxWidth(),
        enabled = false,
        trailingIcon = {
            IconButton(onClick = {
                val calendar = Calendar.getInstance()
                val datePicker = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val date = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }.timeInMillis
                        onDateSelected(date)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
            }
        }
    )
}
