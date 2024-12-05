package com.erp.salespruchase.repository

import com.erp.salespruchase.Customer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {
    private val customerRef = firebaseDatabase.getReference("customers")

    fun getCustomers(): Flow<List<Customer>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val customers = snapshot.children.mapNotNull { it.getValue(Customer::class.java) }
                trySend(customers).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        customerRef.addValueEventListener(listener)
        awaitClose { customerRef.removeEventListener(listener) }
    }

    fun addCustomer(customer: Customer) {
        customerRef.child(customer.id).setValue(customer)
    }

    fun updateCustomer(customer: Customer) {
        customerRef.child(customer.id).setValue(customer)
    }

    fun deleteCustomer(customerId: String) {
        customerRef.child(customerId).removeValue()
    }
}
