package com.erp.salespruchase

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val unit: String = "",
    val category: String = ""
)

data class Customer(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = ""
)


data class Sale(
    val customerId: String = "",
    val productId: String = "",
    val quantity: Int = 0,
    val date: String = ""
)

