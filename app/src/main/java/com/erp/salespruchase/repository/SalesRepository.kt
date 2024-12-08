package com.erp.salespruchase.repository

import com.erp.salespruchase.Sale
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    fun getAllSales(): Flow<List<Sale>> {
        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(snapshot.children.mapNotNull { it.getValue(Sale::class.java) })
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }

            salesRef.orderByKey().addValueEventListener(listener)
            awaitClose { salesRef.removeEventListener(listener) }
        }
    }

}
