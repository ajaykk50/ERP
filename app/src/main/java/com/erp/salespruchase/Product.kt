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


//data class Sale(
//    val id: String = "",
//    val customerId: String = "",
//    val productId: String = "",
//    val quantity: Int = 0,
//    val totalAmount: Double = 0.0,
//    val date: Long = 0L
//)

//data class SaleItem(
//    val product: Product,
//    val quantity: Int
//)

//data class Sale(
//    val id: String = "",
//    val customerId: String = "",
//    val saleItems: List<Pair<String, Int>> = listOf(),
//    val totalAmount: Double = 0.0,
//    val date: Long = 0L
//)

data class SaleItem(
    val product: Product? = null, // Use nullable Product with a default value
    val quantity: Int = 0
)

data class Sale(
    val id: String = "",
    val customerId: String = "",
    val saleItems: List<SaleItem> = listOf(),
    val totalAmount: Double = 0.0,
    val date: Long = 0L
)

//data class Sale(
//    val id: String = "",
//    val customerId: String = "",
//    val productId: String = "",
//    val quantity: Int = 0,
//    val totalAmount: Double = 0.0,
//    val date: Long = 0L
//)
data class Vendor(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = ""
)

data class Purchase(
    val id: String = "",
    val productId: String,
    val vendorId: String,
    val quantity: Int,
    val date: Long
)

data class SelectedProduct(
    val id: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
)

data class Expense(
    val id: String = "",
    val name: String = "",
    val amount: Double = 0.0,
    val date: Long = 0L
)

data class Category(
    val id: String = "",
    val name: String = "",
)

data class DisplaySale(
    val id: String,
    val customerName: String,
    val saleItems: List<DisplaySaleItem>,
    val totalAmount: Double,
    val date: Long
)

data class DisplaySaleItem(
    val productName: String,
    val quantity: Int
)




