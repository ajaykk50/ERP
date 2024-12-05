package com.erp.salespruchase.repository

import com.erp.salespruchase.Sale
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class SalesRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {
    private val salesRef = firebaseDatabase.getReference("sales")

    fun addSales(sale: Sale, onSuccess: () -> Unit, onError: () -> Unit) {
        salesRef.child(sale.id).setValue(sale)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }
    }
}
