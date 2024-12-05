package com.erp.salespruchase.repository

import com.erp.salespruchase.Purchase
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class PurchaseRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {
    private val purchasesRef = firebaseDatabase.getReference("purchases")

    fun addPurchase(purchase: Purchase, onSuccess: () -> Unit, onError: () -> Unit) {
        val purchaseId = purchasesRef.push().key ?: return
        purchasesRef.child(purchaseId).setValue(purchase.copy(id = purchaseId))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError() }
    }
}