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
        val expenseId = expenseRef.push().key ?: return
        expenseRef.child(expenseId).setValue(expense.copy(id = expenseId)).addOnSuccessListener {
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

    fun deleteExpense(expense: Expense, callback: (Boolean) -> Unit) {
        expenseRef.child(expense.id).removeValue().addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun updateExpense(expense: Expense, callback: (Boolean) -> Unit) {
        val expenseId = expense.id ?: return
        expenseRef.child(expenseId).setValue(expense).addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }


}
