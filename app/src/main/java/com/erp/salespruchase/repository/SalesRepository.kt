package com.erp.salespruchase.repository

import com.erp.salespruchase.Sale
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class SalesRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {
    private val salesRef = firebaseDatabase.getReference("sales")

    fun addSales(sales: List<Sale>, onSuccess: () -> Unit, onError: () -> Unit) {
        val updates = sales.associate { it.id to it }
        salesRef.updateChildren(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }
    }
}
