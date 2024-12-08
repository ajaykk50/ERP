package com.erp.salespruchase.repository

import com.erp.salespruchase.Category
import com.erp.salespruchase.Expense
import com.erp.salespruchase.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase // Firebase example
) {

    private val categoryRef = firebaseDatabase.reference.child("category")

    // Save expense to Firebase
    fun addCategory(category: Category, onSuccess: () -> Unit, onError: () -> Unit) {
        categoryRef.child(category.id).setValue(category).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onError()
        }
    }


    fun getCategory(): Flow<List<Category>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val category = snapshot.children.mapNotNull { it.getValue(Category::class.java) }
                trySend(category).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        categoryRef.addValueEventListener(listener)
        awaitClose { categoryRef.removeEventListener(listener) }
    }

    fun editCategory(categoryId: String, updatedCategory: Category, onSuccess: () -> Unit, onError: () -> Unit) {
        categoryRef.child(categoryId).setValue(updatedCategory).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onError()
        }
    }

    fun deleteCategory(categoryId: String, onSuccess: () -> Unit, onError: () -> Unit) {
        categoryRef.child(categoryId).removeValue().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onError()
        }
    }
}
