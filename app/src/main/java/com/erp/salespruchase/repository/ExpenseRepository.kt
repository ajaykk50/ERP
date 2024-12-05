package com.erp.salespruchase.repository

import com.erp.salespruchase.Expense
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase // Firebase example
) {

    private val expenseRef = firebaseDatabase.reference.child("expenses")

    // Save expense to Firebase
    fun addExpense(expense: Expense, onSuccess: () -> Unit, onError: () -> Unit) {
        expenseRef.push().setValue(expense).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onError()
        }
    }

    // Get all expenses from Firebase
    fun getExpenses(callback: (List<Expense>) -> Unit) {
        expenseRef.get().addOnSuccessListener { snapshot ->
            val expenses = snapshot.children.mapNotNull { it.getValue(Expense::class.java) }
            callback(expenses)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }
}
