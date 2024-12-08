package com.erp.salespruchase.repository

import com.erp.salespruchase.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    private val productRef = firebaseDatabase.getReference("products")
    fun getProducts(): Flow<List<Product>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
                trySend(products).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        productRef.addValueEventListener(listener)
        awaitClose { productRef.removeEventListener(listener) }
    }

    fun addProduct(product: Product) {
        productRef.child(product.id).setValue(product)
    }

    fun updateProduct(product: Product) {
        productRef.child(product.id).setValue(product)
    }

    fun deleteProduct(productId: String) {
        productRef.child(productId).removeValue()
    }

    fun updateStock(productId: String, newStock: Int, onSuccess: () -> Unit = {}, onError: (Exception) -> Unit = {}) {
        productRef.child(productId).child("stock").setValue(newStock)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun getProductById(productId: String): Flow<Product?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Retrieve the product using the provided productId
                val product = snapshot.getValue(Product::class.java)
                trySend(product).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if necessary
                close(error.toException())
            }
        }

        // Add a single value listener to fetch the product by ID
        productRef.child(productId).addListenerForSingleValueEvent(listener)

        // Ensure the listener is removed when the flow is closed
        awaitClose { productRef.child(productId).removeEventListener(listener) }
    }


}