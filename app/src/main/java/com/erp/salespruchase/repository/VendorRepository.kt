package com.erp.salespruchase.repository

import com.erp.salespruchase.Vendor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class VendorRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {
    private val vendorsRef = firebaseDatabase.getReference("vendors")

    fun addVendor(vendor: Vendor, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val vendorId = vendorsRef.push().key ?: return
        vendorsRef.child(vendorId).setValue(vendor.copy(id = vendorId))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun updateVendor(vendor: Vendor, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        vendorsRef.child(vendor.id).setValue(vendor)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun deleteVendor(vendorId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        vendorsRef.child(vendorId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun getVendors(): Flow<List<Vendor>> = callbackFlow {
        val listener = vendorsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val vendors = snapshot.children.mapNotNull { it.getValue(Vendor::class.java) }
                trySend(vendors).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        })

        awaitClose { vendorsRef.removeEventListener(listener) }
    }
}
